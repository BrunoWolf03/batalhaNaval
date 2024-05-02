package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class RecuperaMatriz {

    public static int[][] carregarMatriz(String caminhoArquivo) {
        int[][] matriz = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            int linhas = 0;
            int colunas = 0;

            // Conta o número de linhas e o máximo de colunas
            while ((linha = reader.readLine()) != null) {
                linhas++;
                String[] valores = linha.trim().split("\\s+");
                colunas = Math.max(colunas, valores.length);
            }

            // Reinicia a leitura do arquivo
            reader.close();
            BufferedReader newReader = new BufferedReader(new FileReader(caminhoArquivo));

            // Cria a matriz com as dimensões corretas
            matriz = new int[linhas][colunas];
            int linhaAtual = 0;

            // Preenche a matriz com os valores do arquivo
            while ((linha = newReader.readLine()) != null) {
                String[] valores = linha.trim().split("\\s+");
                for (int coluna = 0; coluna < valores.length; coluna++) {
                    matriz[linhaAtual][coluna] = Integer.parseInt(valores[coluna]);
                }
                linhaAtual++;
            }

            newReader.close();
            System.out.println("Matriz carregada com sucesso do arquivo: " + caminhoArquivo);
        } catch (IOException e) {
            System.out.println("Erro ao carregar a matriz do arquivo: " + e.getMessage());
        }


        return matriz;
    }
}
