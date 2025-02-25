package app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class HelloTest {
    
    @Test
    @DisplayName("El metodo hello world debe de devolver Hello World 🙋🏻‍♀️")
    public void helloWorld_StringHelloWorld_ReturnTrue(){
        String input = "Hello + World";
        
        String compute = helloWorld();
        
        assertEquals(input, compute);    
    }

    public String helloWorld() {
        String helloWorld = "Hello +" + " World";
        return helloWorld;
    }
}
