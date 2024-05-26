package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class InserirNome extends JDialog {
    private JTextField player1NameField;
    private JTextField player2NameField;
    private JButton startButton;
    public String nome1;
    public String nome2;

    public InserirNome(JFrame parent) {
        super(parent, "Nomes dos Jogadores", true);
        setSize(300, 200);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

        panel.add(new JLabel("Nome do Jogador 1:"));
        player1NameField = new JTextField();
        panel.add(player1NameField);

        panel.add(new JLabel("Nome do Jogador 2:"));
        player2NameField = new JTextField();
        panel.add(player2NameField);

        startButton = new JButton("Iniciar Jogo");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String player1Name = player1NameField.getText().trim();
                nome1 = player1NameField.getText().trim();
                String player2Name = player2NameField.getText().trim();
                nome2 = player2NameField.getText().trim();
                if (!player1Name.isEmpty() && !player2Name.isEmpty()) {
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(InserirNome.this, "Por favor, insira os nomes de ambos os jogadores.");
                }
            }
        });

        panel.add(new JLabel());
        panel.add(startButton);

        add(panel);
    }
}