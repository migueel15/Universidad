package app;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import factorial.Factorial;


class ExamplesOfAssertionsTest {
    
    private double EPSILON = 1e-15 ;

    @Test  
    public void simpleAssertionExamples() {
        assertEquals(4, 3 + 1);
        assertEquals(4.0, 3.0 + 1.0, EPSILON);
        assertNotEquals(4, 5 + 2);
        assertTrue(6 > 5);
        
        assertTrue("hello".startsWith("h")) ;    
        assertTrue("hello".contains("e")) ;
        assertFalse("hello".contains("v"));

        assertThrows(RuntimeException.class, () -> new Factorial().factorial(-1)) ;
        assertTimeout(Duration.ofMillis(1), () -> new Factorial().factorial(50));
        
        double[] expectedArray = new double[]{1.0, 5.0, 5.2} ;
        assertArrayEquals(expectedArray, new double[]{1.0, 5.0, 5.2});
        double[] anotherArray = expectedArray ;
        assertSame(expectedArray, anotherArray);
        Object nullObject = null ;
        assertNull(nullObject) ;  
    }
}

