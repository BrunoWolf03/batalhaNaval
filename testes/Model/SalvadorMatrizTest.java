package Model;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;

public class SalvadorMatrizTest {

    @Test
    public void testSalvarMatriz() {
        int[][] matriz = new int[][] {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        String caminhoArquivo = "teste.txt";

        SalvadorMatriz.salvarMatriz(matriz, caminhoArquivo);

        int[][] matrizCarregada = RecuperaMatriz.carregarMatriz(caminhoArquivo);

        // Verifica se as dimensões das matrizes são iguais
        assertEquals(matriz.length, matrizCarregada.length);
        for (int i = 0; i < matriz.length; i++) {
            assertArrayEquals(matriz[i], matrizCarregada[i]);
        }
    }
}
