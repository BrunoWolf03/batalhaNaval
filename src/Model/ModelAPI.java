package Model;

import java.util.Observable;

public class ModelAPI extends Observable {
    private static ModelAPI instance;
    private Jogador jogador1;
    private Jogador jogador2;

    public ModelAPI() {
        jogador1 = new Jogador();
        jogador2 = new Jogador();
    }

    public static ModelAPI getInstance() {
        if (instance == null) {
            instance = new ModelAPI();
        }
        return instance;
    }

    public boolean inserirNavio(int jogador, int tipoNavio, int linhaInicial, int colunaInicial, String orientacao) {
        boolean result;
        if (jogador == 1) {
            result = jogador1.inserirNavio(tipoNavio, linhaInicial, colunaInicial, orientacao);
        } else {
            result = jogador2.inserirNavio(tipoNavio, linhaInicial, colunaInicial, orientacao);
        }
        setChanged();
        notifyObservers();
        return result;
    }

    public void resetTabuleiro(int jogador) {
        if (jogador == 1) {
            jogador1.resetTabuleiro();
        } else {
            jogador2.resetTabuleiro();
        }
        setChanged();
        notifyObservers();
    }

    public int registrarTiro(int linha, int coluna, int jogador) {
        int result;
        if (jogador == 1) {
            result = jogador2.registrarTiro(linha, coluna);
        } else {
            result = jogador1.registrarTiro(linha, coluna);
        }
        setChanged();
        notifyObservers();
        return result;
    }

    public void setNome(int jogador, String nome) {
        if (jogador == 1) {
            jogador1.setNome(nome);
        } else {
            jogador2.setNome(nome);
        }
        setChanged();
        notifyObservers();
    }

    public boolean[][] getTiros(int jogador) {
        if (jogador == 1) {
            return jogador1.getTiros();
        } else {
            return jogador2.getTiros();
        }
    }

    public int[][] getTabuleiro(int jogador) {
        if (jogador == 1) {
            return jogador1.getMatriz();
        } else {
            return jogador2.getMatriz();
        }
    }

    public void salvarMatriz(int jogador, String fileName) {
        if (jogador == 1) {
            jogador1.salvarMatrizEmArquivo(fileName);
        } else {
            jogador2.salvarMatrizEmArquivo(fileName);
        }
    }

    public Jogador getJogador(int jogador) {
        if (jogador == 1) {
            return jogador1;
        } else {
            return jogador2;
        }
    }
}
