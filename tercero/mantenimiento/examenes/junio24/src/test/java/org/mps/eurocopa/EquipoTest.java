package org.mps.eurocopa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EquipoTest {

  @DisplayName("Constructor de equipo correcto")
  @Test
  public void equipoConstructorCreatedCorrectly(){
    Equipo equipo = new Equipo("España");
    String expectedValue = "España(0)";

    String value = equipo.toString();

    assertThat(value).isEqualTo(expectedValue);
  }

  @DisplayName("Actualiza los puntos de un equipo que ha ganado")
  @Test
  public void addPuntos3PointsValueExpectsTeamUpdated(){
    Equipo equipo = new Equipo("España");
    String expectedValue = "España(3)";

    equipo.addPuntos(3);
    String equipoString = equipo.toString();

    assertThat(equipoString).isEqualTo(expectedValue);
  }

  @DisplayName("Actualiza los puntos de un equipo que ha empatado")
  @Test
  public void addPuntos1PointValueExpectsTeamUpdated(){
    Equipo equipo = new Equipo("España");
    String expectedValue = "España(1)";

    equipo.addPuntos(1);
    String equipoString = equipo.toString();

    assertThat(equipoString).isEqualTo(expectedValue);
  }

  @DisplayName("Actualiza los puntos de un equipo erroneamente")
  @Test
  public void addPuntosNonValidValueExpectsException(){
    Equipo equipo = new Equipo("España");

    assertThatExceptionOfType(EurocopaException.class).isThrownBy(()->{
      equipo.addPuntos(4);
    });
  }

  @DisplayName("Get puntos de equipo")
  @Test
  public void getPuntosExpectsCorrectValue(){
    Equipo equipo = new Equipo("España");
    int expectedPuntos = 4;

    equipo.addPuntos(Eurocopa.PUNTOS_GANAR);
    equipo.addPuntos(Eurocopa.PUNTOS_EMPATE);
    int puntos = equipo.getPuntos();

    assertThat(puntos).isEqualTo(expectedPuntos);
  }

  @DisplayName("Equals")
  @Test
  public void equalsSameItemsExpectsTrue(){
    Equipo equipo1 = new Equipo("España");
    Equipo equipo2 = new Equipo("España");

    assertThat(equipo1).isEqualTo(equipo2);
  }

  @DisplayName("Equals with different teams")
  @Test
  public void equalsDifferentItemsExpectsFalse(){
    Equipo equipo1 = new Equipo("España");
    Equipo equipo2 = new Equipo("Alemania");

    assertThat(equipo1).isNotEqualTo(equipo2);
  }

  @DisplayName("Equals with non Equipo object")
  @Test
  public void equalsNonTeamObjectExpectsFalse(){
    Equipo equipo = new Equipo("España");
    Object objeto = new Object();

    boolean value = equipo.equals(objeto);

    assertThat(value).isFalse();
  }

  @DisplayName("ToString del equipo")
  @Test
  public void toStringTeamExpectsCorrectPoints(){
    Equipo equipo = new Equipo("España");
    String expectedValue = "España(3)";

    equipo.addPuntos(Eurocopa.PUNTOS_GANAR);
    String res = equipo.toString();

    assertThat(res).isEqualTo(expectedValue);
  }

  @DisplayName("Compare to")
  @Test
  public void compareToSameNameEqualsTrue(){
    Equipo equipo = new Equipo("España");
    Equipo equipo2 = new Equipo("España");
    int expected = 0;

    int value = equipo.compareTo(equipo2);

    assertThat(value).isEqualTo(expected);
  }
}
