package Model;

class Main {

    public static void main(String[] args) {
        // Criando os objetos necessários
        Jogador player1 = new Jogador();
        Jogador player2 = new Jogador();

        // Criando os tabuleiros dos jogadores
        player1.cria_tabuleiro();
        player2.cria_tabuleiro();

        // Jogador 1 insere seus navios
        player1.inserirNavio(1, 0, 0, "horizontal"); // Submarino
        player1.inserirNavio(2, 2, 1, "vertical"); // Destroyer
        player1.inserirNavio(3, 2, 2, "horizontal"); // Hidroavião
        player1.inserirNavio(4, 3, 3, "horizontal"); // Cruzador
        player1.inserirNavio(5, 4, 4, "horizontal"); // Couraçado
        player1.inserirNavio(3, 15, 1, "horizontal");


        // Jogador 2 insere seus navios
        player2.inserirNavio(1, 0, 0, "horizontal"); // Submarino
        player2.inserirNavio(2, 1, 1, "horizontal"); // Destroyer
        player2.inserirNavio(3, 2, 2, "horizontal"); // Hidroavião
        player2.inserirNavio(4, 3, 3, "horizontal"); // Cruzador
        player2.inserirNavio(5, 4, 4, "horizontal"); // Couraçado

        // Simulação de tiros dos jogadores
        realizarTiro(player1, 3, 3); // Jogador 1 atira na posição (3, 3)
        realizarTiro(player2, 0, 0); // Jogador 2 atira na posição (0, 0)
        realizarTiro(player1, 6, 6); // Jogador 1 atira na posição (6,6)

        player1.salvarMatrizEmArquivo("matriz1.txt");
        player2.salvarMatrizEmArquivo("matriz2.txt");

        Jogador player3 = new Jogador();
        RecuperaMatriz carregador = new RecuperaMatriz();
        player3.cria_tabuleiro();
        player3.matriz=carregador.carregarMatriz("matriz1.txt");
        player3.salvarMatrizEmArquivo("matriz3.txt");
    }

    // Método para realizar um tiro em uma determinada posição
    private static void realizarTiro(Jogador player, int linha, int coluna) {
        int[][] matrizTiro = player.getMatriz(); // Obtém o tabuleiro do jogador
        Tiro t = new Tiro();
        t.atirar(matrizTiro, linha, coluna); // Realiza o tiro

    }
}