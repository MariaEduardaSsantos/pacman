import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingConstants; // Import para alinhar o texto

public class JanelaJogo extends JFrame {

    private Modelo jogo;
    private JButton iniciarBotao;
    private JPanel painelPrincipal;

    public JanelaJogo() {
        setTitle("GREEDY TRASH");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(Modelo.tamanhoTela, Modelo.tamanhoTela + 50); // Ajustando a altura para o placar

        painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BorderLayout());

        // Carrega a imagem
        ImageIcon imagemIcon = new ImageIcon("C:/Users/ANA CLAUDIA/AppData/Local/Temp/Rar$DIa20228.41417/Tela INicial.gif");

        // Cria o botão com a imagem
        iniciarBotao = new JButton("", imagemIcon);
        iniciarBotao.setHorizontalTextPosition(SwingConstants.RIGHT); // Coloca o texto à direita da imagem

        iniciarBotao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jogo == null) {
                    jogo = new Modelo();
                    painelPrincipal.add(jogo, BorderLayout.CENTER);
                    jogo.requestFocusInWindow(); // Para que o teclado funcione no jogo
                } else {
                    // Se o jogo já estiver rodando, você pode adicionar alguma lógica aqui,
                    // como reiniciar o jogo ao clicar novamente no botão.
                    jogo.iniciarJogo(); // Reinicia o jogo
                    jogo.requestFocusInWindow();
                }
                // Remover o botão após o início (opcional)
                painelPrincipal.remove(iniciarBotao);
                painelPrincipal.revalidate(); // Refaz o layout do painel
                painelPrincipal.repaint();    // Redesenha o painel
            }
        });

        painelPrincipal.add(iniciarBotao, BorderLayout.CENTER); // Coloca o botão no centro
        add(painelPrincipal);

        setLocationRelativeTo(null); // Centraliza a janela
        setVisible(true);
    }

    public static void main(String[] args) {
        new JanelaJogo();
    }
}