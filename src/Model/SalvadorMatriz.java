package Model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

class SalvadorMatriz {

    protected static void salvarMatriz(int[][] matriz, String caminhoArquivo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivo))) {
            for (int i = 0; i < matriz.length; i++) {
                for (int j = 0; j < matriz[0].length; j++) {
                    writer.write(matriz[i][j] + " "); // Escreve o valor da célula seguido de um espaço
                }
                writer.newLine(); // Nova linha para a próxima linha da matriz
            }
            System.out.println("Matriz salva com sucesso em: " + caminhoArquivo);
        } catch (IOException e) {
            System.out.println("Erro ao salvar a matriz em arquivo: " + e.getMessage());
        }
    }
}
