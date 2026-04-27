<%@ page import="java.util.List" %>
<%@ page import="es.taw.movies.entity.People" %>
<%@ page import="es.taw.movies.entity.MovieCrew" %>
<%@ page import="es.taw.movies.entity.MovieCast" %>
<%@ page import="es.taw.movies.entity.Movies" %>
<%@ page import="es.taw.movies.dto.FilterRequest" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>

<%
    List<People> people = (List<People>) request.getAttribute("people");
    FilterRequest filter = (FilterRequest) request.getAttribute("filter");
%>

<body>
<form method="get" action="/">
    <label>
        <input type="radio" name="type" value="REPARTO" <%=filter.getType()
        == FilterRequest.PersonType.REPARTO ? "checked": ""%>>
        Reparto
    </label>

    <label>
        <input type="radio" name="type" value="TRABAJADOR" <%=filter.getType() == FilterRequest.PersonType.TRABAJADOR ? "checked": ""%>>
        Trabajadores
    </label>
    y contiene
    <input name="content" value="<%=filter.getContent() != null ?
    filter.getContent() : ""%>">
    <button type="submit">Filtrar</button>
    <a href="/">Limpiar filtro</a>
</form>
<table>
    <tr>
        <th>Nombre</th>
        <%
            if (filter.getType() == null || filter.getType() == FilterRequest.PersonType.REPARTO){
        %>
        <th>Peliculas - Personajes</th>
        <%
            }
        %>

        <%
            if (filter.getType() == null || filter.getType() ==
                    FilterRequest.PersonType.TRABAJADOR){
        %>
        <th>Peliculas - Cargos</th>
        <%
            }
        %>
    </tr>
    <%
        for(People person: people) {
    %>
    <tr>
        <td><%=person.getName()%></td>
        <%
            if (filter.getType() == null || filter.getType() == FilterRequest.PersonType.REPARTO){
        %>
        <td>
            <%
                for(MovieCast cast: person.getMovieCastList()){
                    Movies movie = cast.getMovieId();
                  Integer movieId = movie.getId();
                  String movieTitle = movie.getTitle();
                  %>
            <a
                    href="movies/<%=movieId%>"><%=movieTitle%></a> -
            <%=cast.getCharacterName()%>
            <%
                }
            %>
        </td>
        <%
            }
        %>

        <%
            if (filter.getType() == null || filter.getType() ==
                    FilterRequest.PersonType.TRABAJADOR){
        %>
        <td>
            <%
                for(MovieCrew crew: person.getMovieCrewList()){
                    Movies movie = crew.getMovieId();
                    Integer movieId = movie.getId();
                    String movieTitle = movie.getTitle();


            %>
            <a href="movies/<%=movieId%>"><%=movieTitle%></a> -
            <%=crew.getJob()%>
            <%
                }
            %>
        </td>
        <%
            }
        %>
    </tr>
    <%
        }
    %>
</table>
</body>
</html>
