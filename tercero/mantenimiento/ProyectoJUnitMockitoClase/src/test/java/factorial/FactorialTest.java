package factorial;
/*
  Test cases
  1. factorial 0 -> 1
  2. factorial 1 -> 1
*/

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class FactorialTest {
  Factorial factorial  ;

  @BeforeEach
  void setup() {
    factorial = new Factorial() ;
  }


  @Test
  @DisplayName("factorial de cero es 1")
  void factorial_OfZero_IsOne() {
    //Arrange
    int expectedValue = 1 ;

    //Act
    int obtainedValue = factorial.factorial(0) ;
    
    //Assert
    assertEquals(expectedValue, obtainedValue) ;
  }

  @Test
  @DisplayName("1️⃣❗ = 1️⃣")
  void factorial_OfOne_IsOne() {
    //Arrange
    int expectedValue = 1 ;
    
    //Act
    int obtainedValue = factorial.factorial(1) ;
    
    //Assert
    assertEquals(expectedValue, obtainedValue) ;
  }

}