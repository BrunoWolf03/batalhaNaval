package Model;

import java.util.Observable;

public class Jogador extends Observable {
    int matriz[][];
    int[][] matrizTiro;
    public String nome;
    private Tiro tiros;

    public Jogador() {
        this.matriz = new int[15][15];
        this.matrizTiro = new int[15][15];
        cria_tabuleiro();
        this.tiros = new Tiro(15);
    }

    public void cria_tabuleiro() {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                matriz[i][j] = 0;
                matrizTiro[i][j] = 0;
            }
        }
        setChanged();
        notifyObservers();
    }

    public boolean inserirNavio(int tipoNavio, int linhaInicial, int colunaInicial, String orientacao) {
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

    private boolean posicaoDisponivel(int linhaInicial, int colunaInicial, int tamanho, String orientacao) {
        if((linhaInicial <0 ) || (linhaInicial>15) || (colunaInicial>15) || (colunaInicial <0 )) {
            return false;
        }
        else{
            if (tamanho == 3) {
                if ((linhaInicial == 0) || (colunaInicial == 0) || (linhaInicial == 15) || (colunaInicial == 15)) {
                    System.out.println("Nao pode colocar hidroaviao na borda.");
                    return false;
                }
                if (colunaInicial + tamanho > 15) {
                    return false;
                }
            }
            if (orientacao.equalsIgnoreCase("horizontal")) {
                if (colunaInicial + tamanho > 15) {
                    return false;
                }
                for (int i = 0; i < tamanho; i++) {
                    if (matriz[linhaInicial][colunaInicial + i] != 0) {
                        return false;
                    }
                }
            } else if (orientacao.equalsIgnoreCase("vertical")) {
                if (linhaInicial + tamanho > 15) {
                    return false;
                }
                for (int i = 0; i < tamanho; i++) {
                    if (matriz[linhaInicial + i][colunaInicial] != 0) {
                        return false;
                    }
                }
            }
            for (int i = 0; i < tamanho; i++) {
                for (int j = 0; j < 3; j++) {
                    for (int k = 0; k < 3; k++) {
                        if ((linhaInicial - 1 + j > 15) || (colunaInicial - 1 + k > 15) || (linhaInicial - 1 + j < 0) || (colunaInicial - 1 + k < 0)) {
                            System.out.println("Esta fora da matriz");
                        } else {
                            if (matriz[linhaInicial - 1 + j][colunaInicial - 1 + k] != 0) {
                                return false;
                            }
                        }
                    }
                }
            }

            return true;
        }
    }

    public int[][] getMatriz() {
        return matriz;
    }

    public void setMatriz(int[][] matriz) {
        this.matriz = matriz;
        setChanged();
        notifyObservers();
    }

    public void salvarMatrizEmArquivo(String caminhoArquivo) {
        SalvadorMatriz.salvarMatriz(matriz, caminhoArquivo);
    }

    public void resetTabuleiro() {
        this.matriz = new int[15][15];
        setChanged();
        notifyObservers();
    }

    public int registrarTiro(int linha, int coluna) {
        int resultado = tiros.atirar(getMatriz(), linha, coluna);
        if (resultado != -1) {
            setChanged();
            notifyObservers();
        }
        return resultado;
    }

    public boolean[][] getTiros() {
        return tiros.getTiros();
    }
}