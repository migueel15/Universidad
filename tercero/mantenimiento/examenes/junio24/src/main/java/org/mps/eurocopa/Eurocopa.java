package org.mps.eurocopa;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Eurocopa {

    public static final int PUNTOS_GANAR = 3;
    public static final int PUNTOS_EMPATE = 1;
    private Map<String, Set<Equipo>> grupos;

    public Eurocopa() {
        grupos = new TreeMap<>();
    }

    /*
     * Anyade un grupo y el listado de equipos del grupo a la Eurocopa.
     * El formato es: GrupoX:Equipo1,Equipo2,Equipo3
     * 
     * Por ejemplo:
     * GrupoA:Alemania,Escocia,Hungria,Suiza
     * GrupoB:España,Croacia,Italia,Albania
     * GrupoC:Eslovenia,Dinamarca,Serbia,Inglaterra
     */
    public void anyadirGrupo(String line) {
        String nombreGrupo;
        String[] parse;
        try {
            parse = line.split(":");

            // Obtenemos el nombre del grupo
            nombreGrupo = parse[0];
            if (nombreGrupo.isEmpty())
                throw new EurocopaException("El nombre del grupo no es valido");
            if (grupos.containsKey(nombreGrupo))
                throw new EurocopaException("El grupo ya existe");

            // Obtenemos todos los equipos
            String[] equipos = parse[1].split(",");

            Set<Equipo> grupo = new TreeSet<>();
            for (String equipo : equipos) {
                // Anyadimos el equipo al grupo
                grupo.add(new Equipo(equipo));
            }
            // Anyadimos el grupo y los equipos a la Eurocopa
            grupos.put(nombreGrupo, grupo);
        } catch (Exception e) {
            throw new EurocopaException("Datos de los grupos y equipos no validos");
        }
    }

    /*
     * Anyadimos el resultado de un partido a la Eurocopa y actualizamos los puntos
     * de los equipos local y visitante.
     */
    public void anyadirResultado(Resultado resultado) {
        String grupo = resultado.getGrupo();
        if (!grupos.containsKey(grupo))
            throw new EurocopaException("El grupo no existe");

        Equipo equipoLocal = getEquipo(grupo, resultado.getLocal());
        Equipo equipoVisitante = getEquipo(grupo, resultado.getVisitante());
        
        if (resultado.ganaLocal()) {
            equipoLocal.addPuntos(PUNTOS_GANAR);
        } else if (resultado.empate()) {
            equipoLocal.addPuntos(PUNTOS_EMPATE);
            equipoVisitante.addPuntos(PUNTOS_EMPATE);
        } else {
            equipoVisitante.addPuntos(PUNTOS_GANAR);
        }
    }
    /*
     * Obtiene un equipo de la Eurocopa dado un grupo.
     */
    public Equipo getEquipo(String grupo, Equipo equipoABuscar) {
        if (!grupos.containsKey(grupo))
            throw new EurocopaException("El grupo no existe");
        
        for (Equipo equipo : grupos.get(grupo)) {
            if (equipo.equals(equipoABuscar))
                return equipo;
        }
        return null;
    }

    /*
     * Obtiene el numero total de equipos de la Eurocopa.
     */
    public int getNumeroEquipos() {
        int result=0;
        for (String grupo: grupos.keySet()){
            result+=grupos.get(grupo).size();
        }
        return result;
    }

    /*
     * toString() de los resultados Eurocopa. Devuelve los grupos y los equipos con sus 
     * puntos de forma ordenada, por ejemplo:
     * GrupoB:Albania(2)Croacia(4)España(6)Italia(4)
     */
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (String grupo : grupos.keySet()) {
            result.append(grupo + ":");
            for (Equipo equipo : grupos.get(grupo)) {
                result.append(equipo);
            }
        }
        return result.toString();
    }
}
