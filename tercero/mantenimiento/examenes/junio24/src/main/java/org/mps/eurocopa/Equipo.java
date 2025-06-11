package org.mps.eurocopa;

public class Equipo implements Comparable<Equipo>{
    private String nombre;
    private int puntos;
    
    public Equipo (String nombre){
        this.nombre = nombre;
        this.puntos=0;
    }
    
    /*
     * Actualiza los puntos de un Equipo.
     */
    public void addPuntos(int puntos){
        if (puntos == Eurocopa.PUNTOS_EMPATE  || puntos == Eurocopa.PUNTOS_GANAR)        
            this.puntos+=puntos;
        else
            throw new EurocopaException("Puntos no validos");

    }

    /*
     * Obtiene los puntos de un Equipo.
     */
    public int getPuntos(){
        return puntos;
    }

    @Override
    public boolean equals (Object o){
        return (o instanceof Equipo) && (((Equipo)o).nombre.equals(nombre));
    }
    @Override
    public String toString(){
        return nombre+ "("+puntos+")";
    }

    @Override
    public int compareTo(Equipo o) {
        return nombre.compareTo(o.nombre);
    }
}
