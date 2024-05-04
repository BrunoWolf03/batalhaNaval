package Model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JogadorTest {

    private Jogador jogador;

    @Before
    public void setUp() {
        jogador = new Jogador();
        jogador.cria_tabuleiro();
    }

    @Test
    public void testInserirNavioValido() {
        assertTrue(jogador.inserirNavio(3, 5, 5, "horizontal"));
        assertTrue(jogador.inserirNavio(1, 5, 10, "horizontal"));
        assertTrue(jogador.inserirNavio(2, 0, 0, "horizontal"));
        assertTrue(jogador.inserirNavio(4, 9, 9, "horizontal"));
        assertTrue(jogador.inserirNavio(5, 11, 3, "horizontal"));


    }

    @Test
    public void testInserirNavioInvalidoForaDoLimite() {
        assertFalse(jogador.inserirNavio(4, 14, 14, "horizontal"));
        // Verifique se inserir um navio com tamanho inválido (4) ultrapassando o limite do tabuleiro retorna falso
    }

    @Test
    public void testInserirNavioInvalidoPosicaoOcupada() {
        // Insira um navio para ocupar parte do tabuleiro
        jogador.inserirNavio(2, 2, 2, "horizontal");
        assertFalse(jogador.inserirNavio(4, 1, 1, "horizontal"));
        // Verifique se inserir um navio em posição ocupada retorna falso
    }

    // Adicione mais métodos de teste conforme necessário para cobrir outros cenários
}
