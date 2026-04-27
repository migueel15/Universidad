<%@ page import="java.util.List" %>
<%@ page import="es.taw.movies.entity.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>

<%
    Movies movie = (Movies) request.getAttribute("movie");
    List<Genres> genres = (List<Genres>) request.getAttribute("genres");
%>

<body>
<h1><%=movie.getTitle()%></h1>
<form method="post" action="/movies/edit">
    <div class="d-flex flex-column">
        <input hidden name="id" value="<%=movie.getId()%>">

    <label>
        Titulo:
        <input name="titulo" value="<%=movie.getTitle()%>" type="text">
    </label>

    <label>
        Duracion:
        <input name="duracion" value="<%=movie.getRuntime()%>" type="number"
               step="0.01">
    </label>

    <label>
        Recaudacion:
        <input name="recaudacion" value="<%=movie.getRevenue()%>" type="number">
    </label>

        <div>
            <%
                for(Genres genre : genres){
                  Integer genreId = genre.getId();
                          %>
            <label>
                <%=genre.getName()%>
                <input type="checkbox" <%=movie.getGenresList().contains(genre) ? "checked" : ""%>
                       name="generos" value="<%=genre.getId()%>">
            </label>
            <%
                }
            %>
        </div>

        <button type="submit">Guardar</button>
    </div>

</form>
</body>
</html>
