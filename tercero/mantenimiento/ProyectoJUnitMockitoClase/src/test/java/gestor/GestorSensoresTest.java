package gestor;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class GestorSensoresTest {
	GestorSensores gestorSensores;

	@BeforeEach
	void setup() {
		gestorSensores = new GestorSensores();
	}

	@Test
	@DisplayName("Al iniciar el gestor el numero de sensores debe ser 0")
	public void getNumeroSensores_Init0() {
		int expectedValue = 0;
		int currentValue = gestorSensores.getNumeroSensores();
		assertEquals(expectedValue, currentValue);
	}

	@Test
	@DisplayName("Solo puede existir un sensor con el mismo nombre")
	public void agregarSensor_ThrowsIfExists() {
		gestorSensores.agregarSensor("sensor1");
		assertThrows(IllegalArgumentException.class, () -> gestorSensores.agregarSensor("sensor1"));
	}

	@Test
	@DisplayName("Borrar sensor")
	public void borrarSenserIfExists() {
		gestorSensores.agregarSensor("sensor1");
		assertTrue(gestorSensores.borrarSensor("sensor1"));
	}

}
