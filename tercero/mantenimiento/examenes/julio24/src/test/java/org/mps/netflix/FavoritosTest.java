package org.mps.netflix;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FavoritosTest {
  Favoritos favoritos;
  Programa mockedProgram;

  @DisplayName("Constructor")
  @Test
  public void constructorTest() {
    Favoritos favs = new Favoritos();
    assertThat(favs).isNotNull();
  }

  @DisplayName("Tests de nuevoPrograma")
  @Nested
  public class nuevoProgramaTests {
    @BeforeEach
    public void setup() {
      favoritos = new Favoritos();
      mockedProgram = mock(Programa.class);
      when(mockedProgram.getId()).thenReturn(1);
    }

    @DisplayName("Programa null")
    @Test
    public void nuevoProgramaNullEsperaExcepcion() {
      assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> {
        favoritos.nuevoPrograma(null);
      });
    }

    @DisplayName("Programa valido posicion no existente se añade a la lista")
    @Test
    public void nuevoProgramaValidoNoExistenteEsperaInsercion() {
      favoritos.nuevoPrograma(mockedProgram);
      int expectedNumberPrograms = 1;

      assertThat(favoritos.totalProgramas()).isEqualTo(expectedNumberPrograms);
      verify(mockedProgram).getId();
    }

    @DisplayName("Programa valido pocion existente lo reemplaza")
    @Test
    public void nuevoProgramaValidoExistenteEsperaReemplazo() {
      favoritos.nuevoPrograma(mockedProgram);
      favoritos.nuevoPrograma(mockedProgram);
      int expectedNumberPrograms = 1;

      assertThat(favoritos.totalProgramas()).isEqualTo(expectedNumberPrograms);
      verify(mockedProgram, times(3)).getId();
    }
  }

  @DisplayName("Tests actualizarMinuto")
  @Nested
  public class actualizarMinutoTests {
    @BeforeEach
    public void setup() {
      favoritos = new Favoritos();
      mockedProgram = mock(Programa.class);
      when(mockedProgram.getId()).thenReturn(1);
      when(mockedProgram.getMinutoActual()).thenReturn(10);
    }

    @DisplayName("Actualizar minuto id no valido")
    @Test
    public void actualizarMinutoIdNoValidoEsperaExcepcion() {
      int programId = 1;
      int minutoActual = 20;

      assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> {
        favoritos.actualizarMinuto(programId, minutoActual);
      });
    }

    @DisplayName("Actualizar minuto de programa existente")
    @Test
    public void actualizarMinutoIdValidoEsperaLlamadaAMetodo() {
      int programId = 1;
      int minutoActual = 20;

      favoritos.nuevoPrograma(mockedProgram);
      favoritos.actualizarMinuto(programId, minutoActual);

      verify(mockedProgram, times(2)).getId();
      verify(mockedProgram).setMinutoActual(20);
    }
  }

  @DisplayName("Tests totalProgramas")
  @Nested
  public class totalProgramasTests {

    @DisplayName("Total de programas siendo 0")
    @Test
    public void totalProgramasWithNoProgramsReturnsZero() {
      favoritos = new Favoritos();
      int expectedValue = 0;

      int currentPrograms = favoritos.totalProgramas();

      assertThat(currentPrograms).isEqualTo(expectedValue);
    }

    @DisplayName("Total de programas siendo mayor a 0")
    @Test
    public void totalProgramasWithProgramsReturnsCorrectValue() {
      favoritos = new Favoritos();
      Programa mockedProgram1 = mock(Programa.class);
      Programa mockedProgram2 = mock(Programa.class);
      when(mockedProgram1.getId()).thenReturn(1);
      when(mockedProgram2.getId()).thenReturn(2);

      favoritos.nuevoPrograma(mockedProgram1);
      favoritos.nuevoPrograma(mockedProgram2);

      int currentPrograms = favoritos.totalProgramas();

      assertThat(currentPrograms).isEqualTo(2);
    }
  }

  @DisplayName("Tests actualizarTitulosConPatron")
  @Nested
  public class actualizarTitulosConPatronTests {
    @BeforeEach
    public void setup() {
      favoritos = new Favoritos();
    }

    @DisplayName("Actualizar titulos programa coincidente")
    @Test
    public void actualizarTitulosConPatronProgramaConPatronEsperaLlamadas() {
      String patron = "comedia";
      String newTitle = "nuevo";
      String title = "Programacomedia";
      mockedProgram = mock(Programa.class);
      when(mockedProgram.getId()).thenReturn(1);
      when(mockedProgram.getTitulo()).thenReturn(title);

      favoritos.nuevoPrograma(mockedProgram);
      favoritos.actualizarTitulosConPatron(patron, newTitle);

      verify(mockedProgram).getTitulo();
      verify(mockedProgram).setTitulo(newTitle);
    }

    @DisplayName("Actualizar titulos programa coincidente y no coincidentes")
    @Test
    public void actualizarTitulosConPatronProgramasVariadosEsperaLlamadas() {
      String patron = "comedia";
      String newTitle = "nuevo";
      String titleWithPatron = "Programacomedia";
      String titleWithoutPatron = "Programa";
      mockedProgram = mock(Programa.class);
      Programa mockedProgram2 = mock(Programa.class);
      when(mockedProgram.getId()).thenReturn(1);
      when(mockedProgram.getTitulo()).thenReturn(titleWithPatron);
      when(mockedProgram2.getId()).thenReturn(2);
      when(mockedProgram2.getTitulo()).thenReturn(titleWithoutPatron);

      favoritos.nuevoPrograma(mockedProgram);
      favoritos.nuevoPrograma(mockedProgram2);
      favoritos.actualizarTitulosConPatron(patron, newTitle);

      verify(mockedProgram).getTitulo();
      verify(mockedProgram).setTitulo(newTitle);
      verify(mockedProgram2).getTitulo();
      verify(mockedProgram2, times(0)).setTitulo(newTitle);
    }
  }

  @DisplayName("verPrograma Tests")
  @Nested
  public class verProgramaTests {
    @BeforeEach
    public void setup(){
      favoritos = new Favoritos();
    }

    @DisplayName("Ver programa no existente")
    @Test
    public void verProgramaNonValidIdExpectsException() {
      int id = 23;

      assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> {
        favoritos.verPrograma(id);
      });
    }

    @DisplayName("Ver programa existente")
    @Test
    public void verProgramaValidIdExpectsProgram() {
      int id = 1;
      mockedProgram = mock(Programa.class);
      when(mockedProgram.getId()).thenReturn(id);

      favoritos.nuevoPrograma(mockedProgram);
      Programa p = favoritos.verPrograma(id);

      verify(mockedProgram, times(2)).getId();
      assertThat(p).isEqualTo(mockedProgram);
    }
  }

  @DisplayName("eliminarPrograma Tests")
  @Nested
  public class eliminarProgramaTests{

    @BeforeEach
    public void setup(){
      favoritos = new Favoritos();
    }

    @DisplayName("Eliminar programa no existente")
    @Test
    public void eliminarProgramaNonExistingExpectsFalse(){
      int idAEliminar = 2;

      boolean res = favoritos.eliminarPrograma(idAEliminar);
      assertThat(res).isFalse();
    }

    @DisplayName("Eliminar programa existente")
    @Test
    public void eliminarProgramaExistingExpectsTrue(){
      int idAEliminar = 1;
      Programa mockedProgram = mock(Programa.class);
      when(mockedProgram.getId()).thenReturn(1);

      favoritos.nuevoPrograma(mockedProgram);
      boolean res = favoritos.eliminarPrograma(idAEliminar);
      assertThat(res).isTrue();
      assertThat(favoritos.totalProgramas()).isEqualTo(0);
      verify(mockedProgram,times(2)).getId();
    }
  }

  @DisplayName("empezados tests")
  @Nested
  public class empezadosTests {
    @BeforeEach
    public void setup() {
      favoritos = new Favoritos();
    }

    @DisplayName("Ningun programa empezado")
    @Test
    public void empezadosNoProgramExpectsEmptyList() {
      Programa notStartedProgramMock = mock(Programa.class);
      when(notStartedProgramMock.getId()).thenReturn(1);
      when(notStartedProgramMock.getMinutoActual()).thenReturn(0);

      favoritos.nuevoPrograma(notStartedProgramMock);
      List<Programa> listaEmpezados = favoritos.empezados();
      int size = listaEmpezados.size();

      assertThat(size).isEqualTo(0);
      verify(notStartedProgramMock).getId();
      verify(notStartedProgramMock).getMinutoActual();
    }

    @DisplayName("Todos los programas empezado")
    @Test
    public void empezadosAllProgramExpectsProgramsListSize() {
      Programa startedProgramMock = mock(Programa.class);
      when(startedProgramMock.getId()).thenReturn(1);
      when(startedProgramMock.getMinutoActual()).thenReturn(20);

      Programa startedProgramMock2 = mock(Programa.class);
      when(startedProgramMock2.getId()).thenReturn(2);
      when(startedProgramMock2.getMinutoActual()).thenReturn(45);

      favoritos.nuevoPrograma(startedProgramMock);
      favoritos.nuevoPrograma(startedProgramMock2);
      List<Programa> listaEmpezados = favoritos.empezados();
      int size = listaEmpezados.size();

      assertThat(size).isEqualTo(2);
      verify(startedProgramMock, times(2)).getId();
      verify(startedProgramMock).getMinutoActual();

      verify(startedProgramMock2).getId();
      verify(startedProgramMock2).getMinutoActual();
    }

    @DisplayName("Algunos empezado")
    @Test
    public void empezadosSomeProgramExpectsCorrectSize() {
      Programa startedProgramMock = mock(Programa.class);
      when(startedProgramMock.getId()).thenReturn(1);
      when(startedProgramMock.getMinutoActual()).thenReturn(20);

      Programa notStartedProgramMock = mock(Programa.class);
      when(notStartedProgramMock.getId()).thenReturn(2);
      when(notStartedProgramMock.getMinutoActual()).thenReturn(0);

      favoritos.nuevoPrograma(startedProgramMock);
      favoritos.nuevoPrograma(notStartedProgramMock);
      List<Programa> listaEmpezados = favoritos.empezados();
      int size = listaEmpezados.size();

      assertThat(size).isEqualTo(1);
      verify(startedProgramMock, times(2)).getId();
      verify(startedProgramMock).getMinutoActual();

      verify(notStartedProgramMock).getId();
      verify(notStartedProgramMock).getMinutoActual();

    }
  }
}
