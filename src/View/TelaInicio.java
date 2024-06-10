package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaInicio extends JFrame {

    public TelaInicio() {
        // Configurações da janela
        setTitle("Batalha Naval ABG");
        setUndecorated(true); // Remove a barra de título
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximiza a janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null); // Centraliza a janela na tela

        // Carrega a imagem
        ImageIcon imageIcon = new ImageIcon("TelaInicio.jpeg");
        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setLayout(new GridBagLayout()); // Define o layout para centralizar os botões

        // Cria os botões
        JButton startButton = new JButton("Iniciar jogo");
        JButton loadButton = new JButton("Carregar jogo");

        // Define as configurações do layout para centralizar os botões
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Adiciona os botões à imagem
        imageLabel.add(startButton, gbc);
        gbc.gridy++;
        imageLabel.add(loadButton, gbc);

        // Adiciona a imagem (com os botões) à janela
        add(imageLabel);

        // Adiciona funcionalidades aos botões (opcional)
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Código para iniciar o jogo
                System.out.println("Iniciar jogo clicado!");
            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Código para carregar o jogo
                System.out.println("Carregar jogo clicado!");
            }
        });
    }

    public static void main(String[] args) {
        // Cria e exibe a janela
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TelaInicio().setVisible(true);
            }
        });
    }
}

