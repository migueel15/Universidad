package org.mps.eurocopa;

/*
 * Resultado de un partido de la Eurocopa.
 */
public interface Resultado{
    public String getGrupo();

    public Equipo getVisitante();
    
    public Equipo getLocal();
    
    public boolean empate();

    public boolean ganaLocal();
}