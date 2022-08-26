package examples;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.*;

// The Test class should start or end with "Test"
public class CalculatorTest {

    static Calculator calculator;

    @BeforeAll
    public static void createCal(){
        calculator = new Calculator();
    }

    /*@BeforeEach
    public void setUp() throws Exception {
        calculator = new Calculator();
    }*/

    @Test
    @DisplayName("Simple multiplication should work")
    public void testMultiply() {
        assertEquals(20, calculator.multiply(4, 5));
    }
    @Test
    public void testAdd() {
        assertEquals(9, calculator.add(4, 5));
    }

    @AfterEach
    public void tearDown(){
        System.out.println("Testing closed");
    }


}
