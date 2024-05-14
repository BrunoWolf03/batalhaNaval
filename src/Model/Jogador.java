package Model;

class Jogador {
    int matriz[][];
    int[][] matrizTiro;

    public Jogador() {
        this.matriz = new int[15][15];
        this.matrizTiro = new int[15][15];
    }

    public void cria_tabuleiro(){
        for(int i = 0 ; i<15; i++) {
            for(int j = 0; j<15; j++) {
                matriz[i][j]=0;
                matrizTiro[i][j]=0;
            }
        }
    }


    public boolean inserirNavio(int tipoNavio, int linhaInicial, int colunaInicial, String orientacao) {
        int tamanho = tipoNavio;
        if ((tamanho > 5) || (tamanho < 1)) {
            System.out.println("Esse tamanho não é válido.");
            return false;
        }

        // Verifica se o navio pode ser inserido na posição desejada
        if (!posicaoDisponivel(linhaInicial, colunaInicial, tamanho, orientacao)) {
            System.out.println("Posição indisponível para inserir o navio.");
            return false;
        }

        // Insere o navio no tabuleiro
        if (orientacao.equalsIgnoreCase("horizontal")) {
            if (tipoNavio == 3) {//hidroavião
                matriz[linhaInicial][colunaInicial] = 3;
                matriz[linhaInicial + 1][colunaInicial - 1] = 3;
                matriz[linhaInicial + 1][colunaInicial + 1] = 3;
            } else {
                for (int i = 0; i < tamanho; i++) {
                    matriz[linhaInicial][colunaInicial + i] = tipoNavio;
                }
            }
        } else if (orientacao.equalsIgnoreCase("vertical")) {
            if (tipoNavio == 3) {//hidroavião
                matriz[linhaInicial][colunaInicial] = 3;
                matriz[linhaInicial - 1][colunaInicial + 1] = 3;
                matriz[linhaInicial + 1][colunaInicial + 1] = 3;
            } else {
                for (int i = 0; i < tamanho; i++) {
                    matriz[linhaInicial + i][colunaInicial] = tipoNavio;
                }
            }
        } else {
            System.out.println("Orientação inválida.");
            return false;
        }

        return true;
    }



    // Verifica se a posição desejada está disponível para inserir o navio
    private boolean posicaoDisponivel(int linhaInicial, int colunaInicial, int tamanho, String orientacao) {
        if(tamanho==3) {//hidroaviao nao pode estar nas bordas
            if((linhaInicial==0) || (colunaInicial==0)|| (linhaInicial==15) || (colunaInicial==15) ) {
                System.out.println("Nao pode colocar hidroaviao na borda.");
                return false;
            }
            if (colunaInicial + tamanho > 15) {
                return false; // Navio ultrapassaria os limites do tabuleiro
            }
        }
        if (orientacao.equalsIgnoreCase("horizontal")) {
            if (colunaInicial + tamanho > 15) {
                return false; // Navio ultrapassaria os limites do tabuleiro
            }
            for (int i = 0; i < tamanho; i++) {
                if (matriz[linhaInicial][colunaInicial + i] != 0) {
                    return false; // Posição ocupada por outro navio
                }
            }
        } else if (orientacao.equalsIgnoreCase("vertical")) {
            if (linhaInicial + tamanho > 15) {
                return false; // Navio ultrapassaria os limites do tabuleiro
            }
            for (int i = 0; i < tamanho; i++) {
                if (matriz[linhaInicial + i][colunaInicial] != 0) {
                    return false; // Posição ocupada por outro navio
                }
            }
        }
        for (int i = 0; i < tamanho; i++) {//checa se nao tem embarcacao em volta
            for(int j = 0; j < 3; j++) {
                for(int k =0; k<3; k++) {
                    if((linhaInicial-1+j>15) || (colunaInicial-1+k>15) || (linhaInicial-1+j<0) || (colunaInicial-1+k<0)) {
                        System.out.println("Esta fora da matriz");
                    }
                    else {
                        if(matriz[linhaInicial-1+j][colunaInicial-1+k]!=0) {
                            return false;
                        }
                    }
                }
            }
        }

        return true; //tem que botar mais uns ifs aqui, só isso ja dar certo u-u
    }

    public int[][] getMatriz() {
        return matriz;
    }

    public void setMatriz(int[][] matriz) {
        this.matriz = matriz;
    }

    public void salvarMatrizEmArquivo(String caminhoArquivo) {
        SalvadorMatriz.salvarMatriz(matriz, caminhoArquivo);
    }
}

