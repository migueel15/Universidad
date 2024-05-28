package es.uma.rysd.entities;

// Class obtained when querying for a character's information

public class Person {
	public String name;
	public String birth_year;
	public String eye_color;
	public String gender;
	public String hair_color;
	public String height;
	public String mass;
	public String skin_color;
	public String homeworld;
	public String[] films;
	public String[] species;
	public String[] starships;
	public String[] vehicles;

	// All the previous attributes are obtained directly from the object returned by the query
	// The following attributes must be filled if necessary by querying the URLs returned in the query
	// in the respective previous fields.
	public World homeplanet = null;
	public Movie[] movies = null;
	public Specie[] speciesDetails = null;
	public SpaceShip[] spaceships = null;
	public Vehicle[] vehiclesDetails = null;

	public String toString() {
		String text = name + " (" + gender + ") was born in the year " + birth_year;
		if (homeplanet != null) text += " on " + homeplanet;
		text += "\nWeight: " + mass + " Kg and height: " + height + " cm\n";
		if (movies != null) {
			text += "Appears in:\n";
			for (Movie f : movies) {
				text += "- " + f + "\n";
			}
		}
		return text;
	}
}
