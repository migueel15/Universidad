package es.uma.rysd.app;

import es.uma.rysd.app.SWClient;
import es.uma.rysd.entities.Movie;
import es.uma.rysd.entities.Person;
import es.uma.rysd.entities.World;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {
  private static Random rand; // for random numbers
  private static Scanner sc;  // for reading from keyboard
  private final static String proxy = "proxy.lcc.uma.es";
  private final static String proxy_port = "3128";

  public static void main(String[] args) {
    // Uncomment the following lines if you are testing in the lab and accessing
    // the Internet using the proxy System.setProperty("https.proxyHost",proxy);
    // System.setProperty("https.proxyPort",proxy_port);

    SWClient sw = new SWClient();
    String response = null;
    rand = new Random();
    sc = new Scanner(System.in);

    do {
      // tallest(sw);
      // whoBornIn1(sw);
      // whoBornIn2(sw);
      whoAppearsIn(sw);
      System.out.println("Desea otra ronda (s/n)?");
      response = sc.nextLine();
    } while (response.equals("s"));
    sc.close();
  }

  // Generates a number between 0 and max-1 that has not been used previously
  // (the used numbers are in l)
  public static Integer getRandomResource(int max, List<Integer> l) {
    if (max == l.size())
      return null;

    Integer p = rand.nextInt(max);
    while (l.contains(p)) {
      p = (p + 1) % max;
    }
    return p;
  }

  // Question that obtains two identical resources (characters in this case) and
  // compares them
  public static void tallest(SWClient sw) {
    // Getting the number of stored people
    int max_people = sw.countResources("people");
    if (max_people == 0) {
      System.out.println("No people found.");
      return;
    }

    System.out.println("Generating new question...");
    // Picking two random people without repeating
    List<Integer> used = new ArrayList<Integer>();
    List<Person> people = new ArrayList<Person>();
    int counter = 0;
    while (counter < 2) {
      Integer p = getRandomResource(max_people, used);
      Person person = sw.getPerson(sw.buildResourceUrl("people", p));
      if (person == null) {
        System.out.println("Hubo un error al encontrar el recurso " + p);
      } else {
        people.add(person);
        counter++;
      }
      used.add(p);
    }

    // Writing the question and reading the option
    Integer n = null;
    do {
      System.out.println("¿Quién es más alto? [0] " + people.get(0).name +
                         " o [1] " + people.get(1).name);
      try {
        n = Integer.parseInt(sc.nextLine());
      } catch (NumberFormatException ex) {
        n = -1;
      }
    } while (n != 0 && n != 1);

    // Showing information about the chosen characters
    for (Person p : people) {
      System.out.println(p.name + " mide " + p.height);
    }

    // Result
    if (Double.parseDouble(people.get(n).height) >=
        Double.parseDouble(people.get((n + 1) % 2).height)) {
      System.out.println("Enhorabuena!!! " +
                         success[rand.nextInt(success.length)]);
    } else {
      System.out.println("Fallaste :( " + error[rand.nextInt(error.length)]);
    }
  }

  // Question that relates multiple resources:
  // - Chooses a resource (planet in this case)
  // - Asks about a related resource (person who was born or created there)
  // - Searches for that resource and checks if it is related to the first one
  // (if the searched person has the original planet)
  public static void whoBornIn1(SWClient sw) {
    // Getting the number of planets
    int max_planet = sw.countResources("planets");
    if (max_planet == 0) {
      System.out.println("No se encontraron planetas.");
      return;
    }

    System.out.println("Generando nueva pregunta...");
    // Getting the planet (that has people)
    List<Integer> used = new ArrayList<Integer>();
    World world = null;
    do {
      Integer p = getRandomResource(max_planet, used);
      world = sw.getWorld(sw.buildResourceUrl("planets", p));
      if (world == null) {
        System.out.println("Hubo un error al encontrar el recurso " + p);
      }
      used.add(p);
    } while (world == null || world.residents == null ||
             world.residents.length < 1 || world.name.equals("unknown"));

    // Posing the question
    String s = null;
    System.out.println("¿Quién nació o fue creado en " + world.name + "?");
    s = sc.nextLine();
    System.out.println(s);
    // Searching for the indicated person
    Person p = sw.searchPersonByName(s);

    // Validating the answer and displaying their information
    if (p == null) {
      System.out.println("No hay nadie con ese nombre");
    } else {
      System.out.println(p.name + " nació en " + p.homeplanet.name);
    }

    // Results
    if (p != null && p.homeplanet.name.equals(world.name)) {
      System.out.println("Enhorabuena!!! " +
                         success[rand.nextInt(success.length)]);
    } else {
      System.out.println("Fallaste :( " + error[rand.nextInt(error.length)]);
    }
  }

  // Similar to the previous one but instead of asking to write, alternatives
  // are offered:
  // - A correct person from the planet is chosen randomly from the available
  // ones
  // - Three others that are not related to the resource are chosen randomly
  // (the incorrect ones)
  // - The correct one is inserted randomly
  public static void whoBornIn2(SWClient sw) {

    // Getting the number of planets and people
    int max_people = sw.countResources("people");
    int max_planet = sw.countResources("planets");
    if (max_people == 0 || max_planet == 0) {
      System.out.println("No se encontraron personas o planetas.");
      return;
    }

    System.out.println("Generando nueva pregunta...");
    // Getting the planet (with people)
    List<Integer> used = new ArrayList<Integer>();
    World world = null;
    do {
      Integer p = getRandomResource(max_planet, used);
      world = sw.getWorld(sw.buildResourceUrl("planets", p));
      if (world == null) {
        System.out.println("Hubo un error al encontrar el recurso " + p);
      }
      used.add(p);
    } while (world == null || world.residents == null ||
             world.residents.length < 1 || world.name.equals("unknown"));
    used.clear();
    // Picking one randomly as the correct answer
    String[] residents = world.residents;
    Person correct = sw.getPerson(residents[rand.nextInt(residents.length)]);
    // Marking all people from the planet as used so they won't be selected
    // again
    for (String s : residents) {
      used.add(sw.extractIdFromUrl(s));
    }

    // Finding 3 more
    List<Person> people = new ArrayList<Person>();
    int contador = 0;
    while (contador < 3) {
      Integer p = getRandomResource(max_people, used);
      Person person = sw.getPerson(sw.buildResourceUrl("people", p));
      if (person == null) {
        System.out.println("Hubo un error al encontrar el recurso " + p);
      } else {
        people.add(person);
        contador++;
      }
      used.add(p);
    }
    // Inserting the correct one randomly
    int pos_correct = rand.nextInt(4);
    people.add(pos_correct, correct);

    // Reading the option
    Integer n = null;
    do {
      System.out.print("¿Quién nació o fue fabricado en " + world.name + "?");
      for (int i = 0; i < 4; i++) {
        System.out.print(" [" + i + "] " + people.get(i).name);
      }
      System.out.println();
      try {
        n = Integer.parseInt(sc.nextLine());
      } catch (NumberFormatException ex) {
        n = -1;
      }
    } while (n < 0 || n > 3);

    // Displaying the results
    for (Person p : people) {
      System.out.println(p.name + " nació en " + p.homeplanet.name);
    }

    // Results
    if (n == pos_correct) {
      System.out.println("Enhorabuena!!! " +
                         success[rand.nextInt(success.length)]);
    } else {
      System.out.println("Fallaste :( " + error[rand.nextInt(error.length)]);
    }
  }

  public static void whoAppearsIn(SWClient sw) {
    int max_people = sw.countResources("people");
    int max_films = sw.countResources("films");
    if (max_people == 0 || max_films == 0) {
      System.out.println("No se encontraron personas o peliculas.");
      return;
    }

    System.out.println("Generando nueva pregunta...");
    // Getting the planet (with people)
    List<Integer> used = new ArrayList<Integer>();
    Movie film = null;
    do {
      Integer p = getRandomResource(max_films, used);
      film = sw.getFilm(sw.buildResourceUrl("films", p));
      if (film == null) {
        System.out.println("Hubo un error al encontrar el recurso " + p);
      }
      used.add(p);
    } while (film == null || film.characters == null ||
             film.characters.length < 1 || film.title.equals("unknown"));
    used.clear();
    // Picking one randomly as the correct answer
    String[] characters = film.characters;
    Person correct = sw.getPerson(characters[rand.nextInt(characters.length)]);
    // Marking all people from the film as used so they won't be selected
    // again
    for (String s : characters) {
      used.add(sw.extractIdFromUrl(s));
    }

    // Finding 3 more
    List<Person> people = new ArrayList<Person>();
    int contador = 0;
    while (contador < 3) {
      Integer p = getRandomResource(max_people, used);
      Person person = sw.getPerson(sw.buildResourceUrl("people", p));
      if (person == null) {
        System.out.println("Hubo un error al encontrar el recurso " + p);
      } else {
        people.add(person);
        contador++;
      }
      used.add(p);
    }
    // Inserting the correct one randomly
    int pos_correct = rand.nextInt(4);
    people.add(pos_correct, correct);

    // Reading the option
    Integer n = null;
    do {
      System.out.print("¿Quién apareció en la película " + film.title + "?");
      for (int i = 0; i < 4; i++) {
        System.out.print(" [" + i + "] " + people.get(i).name);
      }
      System.out.println();
      try {
        n = Integer.parseInt(sc.nextLine());
      } catch (NumberFormatException ex) {
        n = -1;
      }
    } while (n < 0 || n > 3);

    // Displaying the results
    int idx = 0;
    String greenFormat = "\u001B[32m";
    String redFormat = "\u001B[31m";
    String yellowFormat = "\u001B[33m";
    String defaultColor = "\u001B[0m";
    for (Person p : people) {
      String personaje = "";
      if (idx == pos_correct) {
        personaje += greenFormat + p.name + " [" + idx + "]" + defaultColor +
                     " apareció en:\n\t";
      } else if (idx == n) {
        personaje += redFormat + p.name + " [" + idx + "]" + defaultColor +
                     " apareció en:\n\t";
      } else {
        personaje += yellowFormat + p.name + " [" + idx + "]" + defaultColor +
                     " apareció en:\n\t";
      }
      for (Movie m : p.movies) {
        personaje += " - ";
        if (m.title.equals(film.title)) {
          personaje += greenFormat;
        }
        personaje += m.title;

        if (m.title.equals(film.title)) {
          personaje += defaultColor;
        }
      }
      System.out.println(personaje);
      idx++;
    }

    // Results
    if (n == pos_correct) {
      System.out.println("Enhorabuena!!! " +
                         success[rand.nextInt(success.length)]);
    } else {
      System.out.println("Fallaste :( " + error[rand.nextInt(error.length)]);
    }
  }

  private static String[] success = {
      "This is the way",
      "Eres uno con la Fuerza. La Fuerza est� contigo",
      "Que la fuerza te acompañe",
      "Nada ocurre por accidente",
      "Sin duda, maravillosa tu mente es",
      "Cuando te fuiste, no era m�s que el aprendiz. Ahora eres el maestro",
      "La Fuerza te está llamando, déjala entrar",
      "Tu cantidad de midiclorianos debe ser muy alta",
      "Una lecci�n aprendida es una lección ganada",
      "Debes creer en ti mismo o nadie lo haré",
      "El camino a la sabiduria es sencillo para aquellos que no se dejan cegar por el ego"};
  private static String[] error = {
      "El mejor profesor, el fracaso es.",
      "El miedo es el camino hacia el Lado Oscuro. El miedo lleva a la ira, la ira lleva al odio, el odio lleva al sufrimiento. Percibo mucho miedo en ti",
      "Tu carencia de fe resulta molesta",
      "La capacidad de hablar no te hace inteligente",
      "Concéntrate en el momento. Siente, no pienses, usa tu instinto",
      "No lo intentes. Hazlo, o no lo hagas, pero no lo intentes",
      "Paciencia, utiliza la Fuerza. Piensa",
      "Siento una perturbación en la fuerza",
      "El lado oscurso se intensifica en ti",
      "El primer paso para corregir un error es la paciencia",
      "El exceso de confianza es el mas peligroso de los descuidos",
      "El camino de la ignorancia es guiado por el miedo"};
}
