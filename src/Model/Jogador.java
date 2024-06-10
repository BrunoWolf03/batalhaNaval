package Model;

import java.util.Observable;

class Jogador extends Observable {
    int matriz[][];
    int[][] matrizTiro;
    public String nome;
    private Tiro tiros;

    protected Jogador() {
        this.matriz = new int[15][15];
        this.matrizTiro = new int[15][15];
        cria_tabuleiro();
        this.tiros = new Tiro(15);
    }

    protected void setNome(String nome) {
        this.nome = nome;
        setChanged();
        notifyObservers();
    }

    protected String getNome() {
        return nome;
    }

    protected void cria_tabuleiro() {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                matriz[i][j] = 0;
                matrizTiro[i][j] = 0;
            }
        }
        setChanged();
        notifyObservers();
    }

    protected boolean inserirNavio(int tipoNavio, int linhaInicial, int colunaInicial, String orientacao) {
        int tamanho = tipoNavio;
        if ((tamanho > 5) || (tamanho < 1)) {
            System.out.println("Esse tamanho não é válido.");
            return false;
        }

        if (!posicaoDisponivel(linhaInicial, colunaInicial, tamanho, orientacao)) {
            System.out.println("Posição indisponível para inserir o navio.");
            return false;
        }

        if (orientacao.equalsIgnoreCase("horizontal")) {
            if (tipoNavio == 3) {
                matriz[linhaInicial][colunaInicial] = 3;
                matriz[linhaInicial -1 ][colunaInicial + 1] = 3;
                matriz[linhaInicial ][colunaInicial + 2] = 3;
            } else {
                for (int i = 0; i < tamanho; i++) {
                    matriz[linhaInicial][colunaInicial + i] = tipoNavio;
                }
            }
        } else if (orientacao.equalsIgnoreCase("vertical")) {
            if (tipoNavio == 3) {
                matriz[linhaInicial][colunaInicial] = 3;
                matriz[linhaInicial + 1 ][colunaInicial + 1] = 3;
                matriz[linhaInicial + 2][colunaInicial ] = 3;
            } else {
                for (int i = 0; i < tamanho; i++) {
                    matriz[linhaInicial + i][colunaInicial] = tipoNavio;
                }
            }
        } else {
            System.out.println("Orientação inválida.");
            return false;
        }

        setChanged();
        notifyObservers();
        return true;
    }

    protected boolean posicaoDisponivel(int linhaInicial, int colunaInicial, int tamanho, String orientacao) {
        // Verificar se a posição inicial está dentro dos limites
        if ((linhaInicial < 0) || (linhaInicial >= 15) || (colunaInicial >= 15) || (colunaInicial < 0)) {
            return false;
        }

        // Verificar se a embarcação está completamente dentro dos limites
        if (orientacao.equalsIgnoreCase("horizontal")) {
            if (colunaInicial + tamanho > 15) {
                return false;
            }
        } else if (orientacao.equalsIgnoreCase("vertical")) {
            if (linhaInicial + tamanho > 15) {
                return false;
            }
        } else {
            return false; // Orientação inválida
        }

        // Verificar se todas as células da embarcação estão vazias
        if (orientacao.equalsIgnoreCase("horizontal")) {
            for (int i = 0; i < tamanho; i++) {
                if (matriz[linhaInicial][colunaInicial + i] != 0) {
                    return false;
                }
            }
        } else if (orientacao.equalsIgnoreCase("vertical")) {
            for (int i = 0; i < tamanho; i++) {
                if (matriz[linhaInicial + i][colunaInicial] != 0) {
                    return false;
                }
            }
        }

        // Verificar as células vizinhas
        for (int i = -1; i <= tamanho; i++) {
            for (int j = -1; j <= 1; j++) {
                int linha = linhaInicial + (orientacao.equalsIgnoreCase("horizontal") ? 0 : i);
                int coluna = colunaInicial + (orientacao.equalsIgnoreCase("horizontal") ? i : 0);
                if (orientacao.equalsIgnoreCase("horizontal")) {
                    linha = linhaInicial + j;
                    coluna = colunaInicial + i;
                } else {
                    linha = linhaInicial + i;
                    coluna = colunaInicial + j;
                }
                if (linha >= 0 && linha < 15 && coluna >= 0 && coluna < 15) {
                    if (matriz[linha][coluna] != 0) {
                        return false;
                    }
                }
            }
        }

        return true;
    }


    protected int[][] getMatriz() {
        return matriz;
    }

    protected void setMatriz(int[][] matriz) {
        this.matriz = matriz;
        setChanged();
        notifyObservers();
    }

    protected void salvarMatrizEmArquivo(String caminhoArquivo) {
        SalvadorMatriz.salvarMatriz(matriz, caminhoArquivo);
    }

    protected void resetTabuleiro() {
        this.matriz = new int[15][15];
        setChanged();
        notifyObservers();
    }

    protected int registrarTiro(int linha, int coluna) {
        int resultado = tiros.atirar(getMatriz(), linha, coluna);
        if (resultado != -1) {
            setChanged();
            notifyObservers();
        }
        return resultado;
    }

    protected boolean[][] getTiros() {
        return tiros.getTiros();
    }
}