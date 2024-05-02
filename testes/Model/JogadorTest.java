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
        // Verifique se o navio foi inserido corretamente na posição (5,5) com tipo 3 (hidroavião) horizontalmente
        // Faça mais verificações se necessário
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
