package org.mps.eurocopa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EurocopaTest {
  @DisplayName("Prueba constructor eurocopa")
  @Test
  public void eurocopaConstructorInitializeCorrectly (){
    Eurocopa eurocopa = new Eurocopa();
    assertThat(eurocopa).isNotNull();
  }


  @DisplayName("Add group empty string")
  @Test
  public void anyadirGrupoEmptyStringExpectsException(){
    Eurocopa eurocopa = new Eurocopa();
    String line = "";

    assertThatExceptionOfType(EurocopaException.class).isThrownBy(()->{
      eurocopa.anyadirGrupo(line);
    });
  }

  @DisplayName("Add correct group")
  @Test
  public void anyadirGrupoCorrectGroupExpectsGruposUpdated(){
    Eurocopa eurocopa = new Eurocopa();
    String nuevoGrupo = "GrupoA:Alemania,Escocia,Hungria,Suiza";
    String expectedPrint ="GrupoA:Alemania(0)Escocia(0)Hungria(0)Suiza(0)";

    eurocopa.anyadirGrupo(nuevoGrupo);

    assertThat(eurocopa.toString()).isEqualTo(expectedPrint);
  }

  @DisplayName("Add group that already exists")
  @Test
  public void anyadirGrupoAddExistingGroupExpectsException(){
    Eurocopa eurocopa = new Eurocopa();
    String nuevoGrupo = "GrupoA:Alemania,Escocia,Hungria,Suiza";

    eurocopa.anyadirGrupo(nuevoGrupo);

    assertThatExceptionOfType(EurocopaException.class).isThrownBy(()->{
      eurocopa.anyadirGrupo(nuevoGrupo);
    });
  }

  @DisplayName("Add result to a non existing group")
  @Test
  public void anyadirResultadoNonExistingGroupExpectsException(){
    Eurocopa eurocopa = new Eurocopa();
    Resultado resultadoMock = mock(Resultado.class);
    when(resultadoMock.getGrupo()).thenReturn("GrupoZ");

    assertThatExceptionOfType(EurocopaException.class).isThrownBy(()->{
      eurocopa.anyadirResultado(resultadoMock);
    });
  }

  @DisplayName("Result with local winning")
  @Test
  public void anyadirResultadoLocalWinningExpectsCorrectValue(){
    Eurocopa eurocopa = new Eurocopa();
    String grupo = "GrupoA:Alemania,Suiza";
    String expectedValue = "GrupoA:Alemania(3)Suiza(0)";
    Equipo equipoLocal = new Equipo("Alemania");
    Equipo equipoVisitante = new Equipo("Suiza");
    Resultado resultadoMock = mock(Resultado.class);
    when(resultadoMock.getGrupo()).thenReturn("GrupoA");
    when(resultadoMock.getLocal()).thenReturn(equipoLocal);
    when(resultadoMock.getVisitante()).thenReturn(equipoVisitante);
    when(resultadoMock.ganaLocal()).thenReturn(true);

    eurocopa.anyadirGrupo(grupo);
    eurocopa.anyadirResultado(resultadoMock);

    assertThat(eurocopa.toString()).isEqualTo(expectedValue);
  }

  @DisplayName("Result with visitant winning")
  @Test
  public void anyadirResultadoVisitantWinningExpectsCorrectValue(){
    Eurocopa eurocopa = new Eurocopa();
    String grupo = "GrupoA:Alemania,Suiza";
    String expectedValue = "GrupoA:Alemania(0)Suiza(3)";
    Equipo equipoLocal = new Equipo("Alemania");
    Equipo equipoVisitante = new Equipo("Suiza");
    Resultado resultadoMock = mock(Resultado.class);
    when(resultadoMock.getGrupo()).thenReturn("GrupoA");
    when(resultadoMock.getLocal()).thenReturn(equipoLocal);
    when(resultadoMock.getVisitante()).thenReturn(equipoVisitante);
    when(resultadoMock.empate()).thenReturn(false);
    when(resultadoMock.ganaLocal()).thenReturn(false);

    eurocopa.anyadirGrupo(grupo);
    eurocopa.anyadirResultado(resultadoMock);

    assertThat(eurocopa.toString()).isEqualTo(expectedValue);
  }

  @DisplayName("Result with tie winning")
  @Test
  public void anyadirResultadoTieExpectsCorrectValue(){
    Eurocopa eurocopa = new Eurocopa();
    String grupo = "GrupoA:Alemania,Suiza";
    String expectedValue = "GrupoA:Alemania(1)Suiza(1)";
    Equipo equipoLocal = new Equipo("Alemania");
    Equipo equipoVisitante = new Equipo("Suiza");
    Resultado resultadoMock = mock(Resultado.class);
    when(resultadoMock.getGrupo()).thenReturn("GrupoA");
    when(resultadoMock.getLocal()).thenReturn(equipoLocal);
    when(resultadoMock.getVisitante()).thenReturn(equipoVisitante);
    when(resultadoMock.empate()).thenReturn(true);
    when(resultadoMock.ganaLocal()).thenReturn(false);

    eurocopa.anyadirGrupo(grupo);
    eurocopa.anyadirResultado(resultadoMock);

    assertThat(eurocopa.toString()).isEqualTo(expectedValue);
  }

  @DisplayName("Get equipo con grupo no existente")
  @Test
  public void getEquipoIncorrectGroupExpectsException(){
    Eurocopa eurocopa = new Eurocopa();
    String grupo = "GrupoZ";
    Equipo equipo= new Equipo("Suiza");

    assertThatExceptionOfType(EurocopaException.class).isThrownBy(()->{
      eurocopa.getEquipo(grupo, equipo);
    });
  }

  @DisplayName("Get equipo con equipo no existente")
  @Test
  public void getEquipoIncorrectTeamExpectsNull(){
    Eurocopa eurocopa = new Eurocopa();
    String grupo = "GrupoA:Alemania,Italia";
    String grupoNombre = "GrupoA";
    Equipo equipo= new Equipo("Suiza");

    eurocopa.anyadirGrupo(grupo);
    Equipo result = eurocopa.getEquipo(grupoNombre,equipo);

    assertThat(result).isNull();
  }

  @DisplayName("Get equipo correcto")
  @Test
  public void getEquipoCorrectInputsExpectsEquipo(){
    Eurocopa eurocopa = new Eurocopa();
    String grupo = "GrupoA:Alemania,Italia";
    String grupoNombre = "GrupoA";
    Equipo equipo= new Equipo("Italia");

    eurocopa.anyadirGrupo(grupo);
    Equipo result = eurocopa.getEquipo(grupoNombre,equipo);

    assertThat(result).isNotNull();
    assertThat(result.toString()).isEqualTo(equipo.toString());
  }

  @DisplayName("Get numero de equipos con 0 equipos")
  @Test
  public void getNumeroEquiposWithZeroTeamsExpectsZero(){
    Eurocopa eurocopa = new Eurocopa();
    int expectedValue = 0;

    int result = eurocopa.getNumeroEquipos();

    assertThat(result).isEqualTo(expectedValue);
  }

  @DisplayName("Get numero de equipos con equipos")
  @Test
  public void getNumeroEquiposWithTeamsExpectsCorrectValue(){
    Eurocopa eurocopa = new Eurocopa();
      String grupoA = "GrupoA:Alemania,Escocia,Hungria,Suiza";
      String grupoB = "GrupoB:España,Croacia,Italia,Albania";
      String grupoC = "GrupoC:Eslovenia,Dinamarca,Serbia,Inglaterra";
    int expectedValue = 12;

    eurocopa.anyadirGrupo(grupoA);
    eurocopa.anyadirGrupo(grupoB);
    eurocopa.anyadirGrupo(grupoC);
    int result = eurocopa.getNumeroEquipos();

    assertThat(result).isEqualTo(expectedValue);
  }

  @DisplayName("ToString de Eurocopa")
  @Test
  public void toStringExpectsCorrectOutput(){
    Eurocopa eurocopa = new Eurocopa();
    String grupo = "GrupoA:Alemania,Suiza,España";
    String expectedValue = "GrupoA:Alemania(0)España(0)Suiza(0)";

    eurocopa.anyadirGrupo(grupo);
    String output = eurocopa.toString();

    assertThat(output).isEqualTo(expectedValue);
  }

}
