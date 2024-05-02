package Model;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;

public class RecuperaMatrizTest {

    @Test
    public void testCarregarMatriz() {
        int[][] matrizEsperada = new int[][] {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        int[][] matrizCarregada = RecuperaMatriz.carregarMatriz("teste.txt");

        // Verifica se as dimensões das matrizes são iguais
        assertEquals(matrizEsperada.length, matrizCarregada.length);
        for (int i = 0; i < matrizEsperada.length; i++) {
            assertArrayEquals(matrizEsperada[i], matrizCarregada[i]);
        }
    }
}
