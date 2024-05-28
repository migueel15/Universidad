package es.uma.rysd.entities;

// Class obtained when querying for a planet's information

public class World {
	public String name;
	public String diameter;
	public String rotation_period;
	public String orbital_period;
	public String gravity;
	public String population;
	public String climate;
	public String terrain;
	public String surface_water;
	public String[] residents;

	public String toString() {
		return name + " (" + terrain + " - " + climate + ")";
	}
}
