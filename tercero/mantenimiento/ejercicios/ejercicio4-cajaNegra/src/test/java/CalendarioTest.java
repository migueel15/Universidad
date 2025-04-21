import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.time.DayOfWeek;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import es.uma.informatica.mps.Calendario;

// | External Condition                                                                              | Valid equivalence classes            | Invalid equivalence classes          |
// | ----------------------------------------------------------------------------------------------- | ------------------------------------ | ------------------------------------ |
// | número de mes posterior al año 4                                                                | entre 1 y 12 (1)                     | <1 (2), >12 (3)                      |
// | número de mes en el año 4                                                                       | entre 3 y 12 (4)                     | <3 (5), >12 (6)                      |
// | día del mes de tipo A. Meses de 30 días                                                         | entre 1 y 30 (7)                     | <1 (8), >30 (9)                      |
// | día del mes de tipo B. Meses de 31 días                                                         | entre 1 y 31 (10)                    | <1 (11), >31 (12)                    |
// | año                                                                                             | >=4 (13)                             | <=4 (14)                             |
// | dia de febrero de año no bisiesto                                                               | entre 1 y 28 (15)                    | <1 (16), >28 (17)                    |
// | dia de febrero en un año bisiesto anterior al año 1582                                          | entre 1 y 29 (18)                    | <1 (19), >29 (20)                    |
// | dia de febrero en un año posterior al año 1582 (multiplo de 100 y no divisible entre 400)       | entre 1 y 28 (21)                    | <1 (22), >28 (23)                    |
// | dia de febrero en un año bisiesto posterior al año 1582 (multiplo de 100 y divisible entre 400) | entre 1 y 29 (24)                    | <1 (25), >29 (26)                    |
// | dia del mes de octubre de 1582                                                                  | entre 1 y 4 (27), entre 15 y 31 (28) | <1 (29), entre 5 y 14 (30), >31 (31) |

public class CalendarioTest {

  @Test
  @Tag("1")
  @Tag("10")
  @Tag("13")
  @Tag("28")
  @DisplayName("month between 1 and 12 in year 1582 (superior to 4)")
  public void monthBetween1And12InYear1582ExpectsException() {
    // given
    int dia = 18;
    int mes = 10;
    int anio = 1582;

    // when-then
    assertDoesNotThrow(() -> {
      Calendario.diaSemana(dia, mes, anio);
    });
  }

  @Test
  @Tag("1")
  @Tag("10")
  @Tag("13")
  @Tag("27")
  @DisplayName("month with 30 days in year superior to 4")
  public void monthWith30DaysInYearSuperiorTo4() {
    // given
    int dia = 2;
    int mes = 10;
    int anio = 1582;
    // when-then
    assertDoesNotThrow(() -> {
      Calendario.diaSemana(dia, mes, anio);
    });
  }

  @Test
  @Tag("4")
  @Tag("7")
  @DisplayName("month with 30 days in year 4")
  public void monthWith30DaysInYear4() {
    // given
    int dia = 2;
    int mes = 4;
    int anio = 4;
    // when-then
    assertDoesNotThrow(() -> {
      Calendario.diaSemana(dia, mes, anio);
    });
  }

  @Test
  @Tag("1")
  @Tag("13")
  @Tag("15")
  @DisplayName("day of february in not-leap year")
  public void dayOfFebruaryInNotLeapYear() {
    // given
    int dia = 3;
    int mes = 2;
    int anio = 2023;
    // when-then
    assertDoesNotThrow(() -> {
      Calendario.diaSemana(dia, mes, anio);
    });
  }

  @Test
  @Tag("1")
  @Tag("13")
  @Tag("18")
  @DisplayName("day of february in leap year before 1582")
  public void dayOfFebruaryInLeapYearBefore1582() {
    // given
    int dia = 3;
    int mes = 2;
    int anio = 1580;
    // when-then
    assertDoesNotThrow(() -> {
      Calendario.diaSemana(dia, mes, anio);
    });
  }

  @Test
  @Tag("1")
  @Tag("13")
  @Tag("21")
  @DisplayName("day of february in not-leap year after 1582")
  public void dayOfFebruaryInNotLeapYearAfter1582() {
    // given
    int dia = 3;
    int mes = 2;
    int anio = 2023;
    // when-then
    assertDoesNotThrow(() -> {
      Calendario.diaSemana(dia, mes, anio);
    });
  }

  @Test
  @Tag("1")
  @Tag("13")
  @Tag("24")
  @DisplayName("day of february in leap year after 1582")
  public void dayOfFebruaryInLeapYearAfter1582() {
    // given
    int dia = 3;
    int mes = 2;
    int anio = 2024;
    // when-then
    assertDoesNotThrow(() -> {
      Calendario.diaSemana(dia, mes, anio);
    });
  }

  @Test
  @Tag("2")
  @Tag("13")
  @DisplayName("month less than 1 in year superior to 4")
  public void monthLessThan1ExpectsException() {
    // given
    int dia = 1;
    int mes = 0;
    int anio = 2023;

    // when-then
    assertThrows(IllegalArgumentException.class, () -> {
      Calendario.diaSemana(dia, mes, anio);
    });
  }

  @Test
  @Tag("3")
  @Tag("13")
  @DisplayName("month greater than 12 in year superior to 4")
  public void monthGreaterThan12ExpectsException() {
    // given
    int dia = 1;
    int mes = 13;
    int anio = 2023;

    // when-then
    assertThrows(IllegalArgumentException.class, () -> {
      Calendario.diaSemana(dia, mes, anio);
    });
  }

  @Test
  @Tag("1")
  @Tag("5")
  @Tag("13")
  @DisplayName("month less than 3 in year 4")
  public void monthLessThan3ExpectsException() {
    // given
    int dia = 1;
    int mes = 2;
    int anio = 4;

    // when-then
    assertThrows(IllegalArgumentException.class, () -> {
      Calendario.diaSemana(dia, mes, anio);
    });
  }

  @Test
  @Tag("1")
  @Tag("6")
  @Tag("13")
  @DisplayName("month greater than 12 in year 4")
  public void monthGreaterThan12InYear4ExpectsException() {
    // given
    int dia = 1;
    int mes = 13;
    int anio = 4;

    // when-then
    assertThrows(IllegalArgumentException.class, () -> {
      Calendario.diaSemana(dia, mes, anio);
    });
  }

  @Test
  @Tag("1")
  @Tag("8")
  @Tag("13")
  @DisplayName("day less than 1 in month of 30 days")
  public void dayLessThan1InMonthOf30DaysExpectsException() {
    // given
    int dia = 0;
    int mes = 4;
    int anio = 2023;

    // when-then
    assertThrows(IllegalArgumentException.class, () -> {
      Calendario.diaSemana(dia, mes, anio);
    });
  }

  @Test
  @Tag("1")
  @Tag("9")
  @Tag("13")
  @DisplayName("day greater than 30 in month of 30 days")
  public void dayGreaterThan30InMonthOf30DaysExpectsException() {
    // given
    int dia = 31;
    int mes = 4;
    int anio = 2023;

    // when-then
    assertThrows(IllegalArgumentException.class, () -> {
      Calendario.diaSemana(dia, mes, anio);
    });
  }

  @Test
  @Tag("1")
  @Tag("11")
  @Tag("13")
  @DisplayName("day less than 1 in month of 31 days")
  public void dayLessThan1InMonthOf31DaysExpectsException() {
    // given
    int dia = 0;
    int mes = 5;
    int anio = 2023;

    // when-then
    assertThrows(IllegalArgumentException.class, () -> {
      Calendario.diaSemana(dia, mes, anio);
    });
  }

  @Test
  @Tag("12")
  @DisplayName("day greater than 31 in month of 31 days")
  public void dayGreaterThan31InMonthOf31DaysExpectsException() {
    // given
    int dia = 32;
    int mes = 5;
    int anio = 2023;

    // when-then
    assertThrows(IllegalArgumentException.class, () -> {
      Calendario.diaSemana(dia, mes, anio);
    });
  }

  @Test
  @Tag("1")
  @Tag("13")
  @Tag("14")
  @DisplayName("year less than or equal to 4")
  public void yearLessThanOrEqualTo4ExpectsException() {
    // given
    int dia = 1;
    int mes = 1;
    int anio = 4;

    // when-then
    assertThrows(IllegalArgumentException.class, () -> {
      Calendario.diaSemana(dia, mes, anio);
    });
  }

  @Test
  @Tag("1")
  @Tag("13")
  @Tag("16")
  @DisplayName("day less than 1 in february of non-leap year")
  public void dayLessThan1InFebruaryOfNonLeapYearExpectsException() {
    // given
    int dia = 0;
    int mes = 2;
    int anio = 2023;

    // when-then
    assertThrows(IllegalArgumentException.class, () -> {
      Calendario.diaSemana(dia, mes, anio);
    });
  }

  @Test
  @Tag("1")
  @Tag("13")
  @Tag("17")
  @DisplayName("day greater than 28 in february of non-leap year")
  public void dayGreaterThan28InFebruaryOfNonLeapYearExpectsException() {
    // given
    int dia = 29;
    int mes = 2;
    int anio = 2023;

    // when-then
    assertThrows(IllegalArgumentException.class, () -> {
      Calendario.diaSemana(dia, mes, anio);
    });
  }

  @Test
  @Tag("1")
  @Tag("13")
  @Tag("19")
  @DisplayName("day less than 1 in february of leap year before 1582")
  public void dayLessThan1InFebruaryOfLeapYearBefore1582ExpectsException() {
    // given
    int dia = 0;
    int mes = 2;
    int anio = 1580;

    // when-then
    assertThrows(IllegalArgumentException.class, () -> {
      Calendario.diaSemana(dia, mes, anio);
    });
  }

  @Test
  @Tag("1")
  @Tag("13")
  @Tag("20")
  @DisplayName("day greater than 29 in february of leap year before 1582")
  public void dayGreaterThan29InFebruaryOfLeapYearBefore1582ExpectsException() {
    // given
    int dia = 30;
    int mes = 2;
    int anio = 1580;

    // when-then
    assertThrows(IllegalArgumentException.class, () -> {
      Calendario.diaSemana(dia, mes, anio);
    });
  }

  @Test
  @Tag("1")
  @Tag("13")
  @Tag("22")
  @DisplayName("day less than 1 in february of non-leap year after 1582")
  public void dayLessThan1InFebruaryOfNonLeapYearAfter1582ExpectsException() {
    // given
    int dia = 0;
    int mes = 2;
    int anio = 2023;

    // when-then
    assertThrows(IllegalArgumentException.class, () -> {
      Calendario.diaSemana(dia, mes, anio);
    });
  }

  @Test
  @Tag("1")
  @Tag("13")
  @Tag("23")
  @DisplayName("day greater than 28 in february of non-leap year after 1582")
  public void dayGreaterThan28InFebruaryOfNonLeapYearAfter1582ExpectsException() {
    // given
    int dia = 29;
    int mes = 2;
    int anio = 2023;

    // when-then
    assertThrows(IllegalArgumentException.class, () -> {
      Calendario.diaSemana(dia, mes, anio);
    });
  }

  @Test
  @Tag("1")
  @Tag("13")
  @Tag("25")
  @DisplayName("day less than 1 in february of leap year after 1582")
  public void dayLessThan1InFebruaryOfLeapYearAfter1582ExpectsException() {
    // given
    int dia = 0;
    int mes = 2;
    int anio = 2024;

    // when-then
    assertThrows(IllegalArgumentException.class, () -> {
      Calendario.diaSemana(dia, mes, anio);
    });
  }

  @Test
  @Tag("1")
  @Tag("13")
  @Tag("26")
  @DisplayName("day greater than 29 in february of leap year after 1582")
  public void dayGreaterThan29InFebruaryOfLeapYearAfter1582ExpectsException() {
    // given
    int dia = 30;
    int mes = 2;
    int anio = 2024;

    // when-then
    assertThrows(IllegalArgumentException.class, () -> {
      Calendario.diaSemana(dia, mes, anio);
    });
  }

  @Test
  @Tag("1")
  @Tag("13")
  @Tag("29")
  @DisplayName("day less than 1 in october of year 1582")
  public void dayLessThan1InOctoberOfYear1582ExpectsException() {
    // given
    int dia = 0;
    int mes = 10;
    int anio = 1582;

    // when-then
    assertThrows(IllegalArgumentException.class, () -> {
      Calendario.diaSemana(dia, mes, anio);
    });
  }

  @Test
  @Tag("1")
  @Tag("13")
  @Tag("30")
  @DisplayName("day between 5 and 14 in october of year 1582")
  public void dayBetween5And14InOctoberOfYear1582ExpectsException() {
    // given
    int dia = 10;
    int mes = 10;
    int anio = 1582;

    // when-then
    assertThrows(IllegalArgumentException.class, () -> {
      Calendario.diaSemana(dia, mes, anio);
    });
  }

  @Test
  @Tag("1")
  @Tag("13")
  @Tag("31")
  @DisplayName("day greater than 31 in october of year 1582")
  public void dayGreaterThan31InOctoberOfYear1582ExpectsException() {
    // given
    int dia = 32;
    int mes = 10;
    int anio = 1582;

    // when-then
    assertThrows(IllegalArgumentException.class, () -> {
      Calendario.diaSemana(dia, mes, anio);
    });
  }
}
