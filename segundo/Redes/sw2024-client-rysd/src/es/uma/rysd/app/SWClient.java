package es.uma.rysd.app;

import com.google.gson.Gson;
import es.uma.rysd.entities.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import javax.net.ssl.HttpsURLConnection;

public class SWClient {
	private final String app_name = "Mi app";
	private final int year = 2024;

	private final String url_api = "https://swapi.dev/api/";

	// Auxiliary methods provided

	// Gets the URL of the resource id of the type resource
	public String buildResourceUrl(String resource, Integer id) {
		return url_api + resource + "/" + id + "/";
	}

	// Given a resource URL, gets its ID
	public Integer extractIdFromUrl(String url) {
		String[] parts = url.split("/");

		return Integer.parseInt(parts[parts.length - 1]);
	}

	// Queries a resource and returns how many elements it has
	public int countResources(String resource) {
		String url_completa = url_api + resource + "/";

		int numero = 0;

		try {
			URL service = new URL(url_completa);
			HttpsURLConnection connection = (HttpsURLConnection) service.openConnection();
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("User-Agent", "Mi aplicacion");
			connection.setRequestMethod("GET");

			int response_code = connection.getResponseCode();
			if (response_code / 100 != 2) {
				System.err.println();
				throw new RuntimeException();
			}

			Gson parser = new Gson();
			InputStream in = connection.getInputStream();
			ResourceCountResult c = parser.fromJson(new InputStreamReader(in), ResourceCountResult.class);
			numero = c.count;

		} catch (Error e) {
			System.err.println("Error al acceder al recurso.");
		} finally {
			return numero;
		}
	}

	public Person getPerson(String urlname) {
		Person p = null;
		// Just in case it comes as http, we change it to https
		urlname = urlname.replaceAll("http:", "https:");

		try {
			URL service = new URL(urlname);
			HttpsURLConnection connection = (HttpsURLConnection) service.openConnection();
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("User-Agent", "Mi aplicacion");
			connection.setRequestMethod("GET");

			int response_code = connection.getResponseCode();
			if (response_code / 100 != 2) {
				System.err.println();
				throw new RuntimeException();
			}

			Gson parser = new Gson();
			InputStream in = connection.getInputStream();
			p = parser.fromJson(new InputStreamReader(in), Person.class);

			if (p.homeworld != "") {
				p.homeplanet = getWorld(p.homeworld);
			}

			if (p.films != null) {
				p.movies = new Movie[p.films.length];
				for (int i = 0; i < p.films.length; i++) {
					p.movies[i] = getFilm(p.films[i]);
				}
			}

		} catch (Error e) {
			System.err.println("Error al acceder al recurso.");
		} finally {
			return p;
		}
	}

	public World getWorld(String urlname) {
		World p = null;
		// Just in case it comes as http, we change it to https
		urlname = urlname.replaceAll("http:", "https:");

		try {
			URL service = new URL(urlname);
			HttpsURLConnection connection = (HttpsURLConnection) service.openConnection();
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("User-Agent", "Mi aplicacion");
			connection.setRequestMethod("GET");

			int response_code = connection.getResponseCode();
			if (response_code / 100 != 2) {
				System.err.println();
				throw new RuntimeException();
			}

			Gson parser = new Gson();
			InputStream in = connection.getInputStream();
			p = parser.fromJson(new InputStreamReader(in), World.class);

		} catch (Error e) {
			System.err.println("Error al acceder al recurso.");
		} finally {
			return p;
		}
	}

	public Person searchPersonByName(String name) {
		Person p = null;
		name = name.replace(" ", "%20");
		String url_completa = url_api + "people/?search=" + name;

		try {
			URL service = new URL(url_completa);
			HttpsURLConnection connection = (HttpsURLConnection) service.openConnection();
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("User-Agent", "Mi aplicacion");
			connection.setRequestMethod("GET");

			int response_code = connection.getResponseCode();
			if (response_code / 100 != 2) {
				System.err.println();
				throw new RuntimeException();
			}

			Gson parser = new Gson();
			InputStream in = connection.getInputStream();
			QueryResponse q = parser.fromJson(new InputStreamReader(in), QueryResponse.class);
			p = q.results[0];

			if (p.homeworld != null) {
				p.homeplanet = getWorld(p.homeworld);
			}

			if (p.films != null) {
				p.movies = new Movie[p.films.length];
				for (int i = 0; i < p.films.length; i++) {
					p.movies[i] = getFilm(p.films[i]);
				}
			}

		} catch (Error e) {
			System.err.println("Error al acceder al recurso.");
		} finally {
			return p;
		}
	}

	public Movie getFilm(String urlname) {
		Movie f = null;
		// Just in case it comes as http, we change it to https
		urlname = urlname.replaceAll("http:", "https:");

		try {
			URL service = new URL(urlname);
			HttpsURLConnection connection = (HttpsURLConnection) service.openConnection();
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("User-Agent", "Mi aplicacion");
			connection.setRequestMethod("GET");

			int response_code = connection.getResponseCode();
			if (response_code / 100 != 2) {
				System.err.println();
				throw new RuntimeException();
			}

			Gson parser = new Gson();
			InputStream in = connection.getInputStream();
			f = parser.fromJson(new InputStreamReader(in), Movie.class);

		} catch (Error e) {
			System.err.println("Error al acceder al recurso.");
		} finally {
			return f;
		}
	}
}
