<%@ page import="java.util.List" %>
<%@ page import="es.taw.movies.entity.People" %>
<%@ page import="es.taw.movies.entity.MovieCrew" %>
<%@ page import="es.taw.movies.entity.MovieCast" %>
<%@ page import="es.taw.movies.entity.Movies" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>

<%
    List<People> people = (List<People>) request.getAttribute("people");
%>

<body>
<table>
    <tr>
        <th>Nombre</th>
        <th>Peliculas - Personajes</th>
        <th>Peliculas - Cargos</th>
    </tr>
    <%
        for(People person: people) {
    %>
    <tr>
        <td><%=person.getName()%></td>
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

        <td>
            <%
                for(MovieCrew crew: person.getMovieCrewList()){
                    Movies movie = crew.getMovieId();
                    Integer movieId = movie.getId();
                    String movieTitle = movie.getTitle();


            %>
            <a href="movies/<%=movieId%>"><%=movieTitle%></a>
            <p><%=crew.getJob()%></p>
            <%
                }
            %>
        </td>
    </tr>
    <%
        }
    %>
</table>
</body>
</html>
