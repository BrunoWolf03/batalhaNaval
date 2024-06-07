package View;

import Controller.Controller;

import javax.swing. *;
import java.awt. *;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InserirNome extends JDialog {
    public String nome1;
    public String nome2;
    private static InserirNome instance;

    public InserirNome(JFrame frame) {
        super(frame, "Inserir Nomes dos Jogadores", true);

        setLayout(new GridLayout(3, 2));

        JLabel label1 = new JLabel("Nome do Jogador 1:");
        JTextField textField1 = new JTextField();
        JLabel label2 = new JLabel("Nome do Jogador 2:");
        JTextField textField2 = new JTextField();

        add(label1);
        add(textField1);
        add(label2);
        add(textField2);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nome1 = textField1.getText();
                nome2 = textField2.getText();
                setVisible(false);
            }
        });
        add(okButton);

        setSize(300, 150);
        setLocationRelativeTo(frame);
    }

    public static InserirNome getInstance(JFrame frame) {
        if (instance == null) {
            instance = new InserirNome(frame);
        }
        return instance;
    }
}