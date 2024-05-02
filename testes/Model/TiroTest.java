package Model;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class TiroTest {

    @Test
    public void testAtirar() {
        Tiro tiro = new Tiro();
        int[][] matriz = new int[][] {
                {0, 0, 0, 4, 5},
                {0, 2, 2, 4, 5},
                {0, 0, 1, 4, 5},
                {0, 0, 3, 4, 5},
                {0, 3, 0, 3, 5}
        };

        assertEquals(0, tiro.atirar(matriz, 0, 0));
        assertEquals(1, tiro.atirar(matriz, 2, 2));
        assertEquals(2, tiro.atirar(matriz, 1, 1));
        assertEquals(3, tiro.atirar(matriz, 3, 2));
        assertEquals(4, tiro.atirar(matriz, 0, 3));
        assertEquals(5, tiro.atirar(matriz, 0, 4));
        assertEquals(-1, tiro.atirar(matriz, 2, 2));
        assertEquals(-1, tiro.atirar(matriz, 0, 0));

    }

    // Método auxiliar para capturar a saída do sistema
    private String systemOut() {
        return outContent.toString().trim();
    }

    // Mantém o registro da saída do sistema para testes
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }
}
