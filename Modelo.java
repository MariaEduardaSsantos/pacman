import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Modelo extends JPanel implements ActionListener {

    public Dimension dimensao;
    private final Font fontePequena = new Font("Arial", Font.BOLD, 14);
    private boolean emJogo = false;
    private boolean morrendo = false;
    private boolean jogoAcabou = false;
    private String mensagemJogoAcabou = "";
    
    private Clip clip;
    //private String caminhoArquivo = ("C:/Users/ANA CLAUDIA/Downloads/pacman2_x.wav");

    
    private static final int tamanhoBloco = 24;
    private static final int numeroDeBlocos = 30;
    public static final int tamanhoTela = numeroDeBlocos * tamanhoBloco;
    
    private final int velocidadeLixo = 6;
    
    private int vidas, pontuacao;
    private java.util.List<Fantasma> fantasmas;
    
    private Image fantasmaFase1;
    private Image fantasmaFase2;
    private Image fantasmaFase3;
    private Image fantasmaFase4;
    
    private Image coracao;
    private Image cima, baixo, esquerda, direita;
    private Image fundo;
    
    private int pacmanX, pacmanY, pacmanDeslocamentoX, pacmanDeslocamentoY;
    private int requisicaoDeslocamentoX, requisicaoDeslocamentoY;
    
    public int getTamanhoTela() {
        return tamanhoTela;
    }

    private final short dadosFase1[] = {
        19, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22,
        17, 24, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        21,  0, 17, 16, 16, 16, 16, 24, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        21,  0, 17, 16, 16, 16, 20,  0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 24, 24, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        21,  0, 17, 16, 16, 16, 16, 18, 16, 16, 16, 16, 16, 16, 16, 16, 28,  0,  0, 25, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        21,  0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,  0,  0,  0,  0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        21,  0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 22,  0,  0, 19, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        21,  0,	17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,  0,  0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        17, 18, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 24, 16, 16, 16, 18, 18, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 28,
        17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,  0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,  0,
        17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 18, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,  0,
        17, 16, 16, 16, 16, 24, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 28,  0,
        17, 16, 16, 16, 20,  0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,  0,  0,
        17, 16, 16, 16, 20,  0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 18, 22,
        17, 16, 16, 16, 20,  0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 24, 24, 24, 24, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        17, 16, 16, 16, 20,  0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,  0,	 0,  0,  0, 17, 16, 16, 24, 24, 16, 16, 16, 20,
        17, 16, 16, 16, 20,  0, 17, 16, 16, 16, 16, 24, 24, 16, 16, 16, 16, 18, 18, 18, 18, 16, 16, 20,  0,  0, 17, 16, 16, 20,
        17, 16, 16, 16, 20,  0, 17, 16, 16, 16, 20,  0,  0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,  0, 19, 16, 16, 16, 20,
        17, 16, 16, 16, 20,  0, 17, 16, 16, 16, 20,  0,  0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 18, 16, 16, 16, 16, 20,
        17, 16, 16, 16, 16, 18, 16, 16, 16, 16, 16, 18, 18, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 24, 24, 24, 24, 16, 16, 20,
        17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 24, 16, 16, 16, 16, 16, 16, 16, 20,  0,  0,  0,  0, 17, 16, 20,
        17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,  0, 17, 16, 16, 16, 16, 16, 16, 16, 18, 18, 18, 18, 16, 16, 20,
        17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 18, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        25, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 28
    };  
    
    private final short dadosFase2[] = {
        19, 26, 26, 26, 26, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 26, 26, 26, 26, 22,
        21,  0,  0,  0,  0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,  0,  0,  0,  0, 21,
        21,  0, 19, 18, 18, 16, 16, 24, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 24, 16, 16, 18, 18, 22,  0, 21,
        21,  0, 17, 16, 16, 16, 20,  0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,  0, 17, 16, 16, 16, 20,  0, 21,
        21,  0, 17, 16, 16, 16, 20,  0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,  0, 17, 16, 16, 16, 20,  0, 21,
        17, 18, 16, 16, 16, 16, 20,  0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,  0, 17, 16, 16, 16, 16, 18, 20,
        17, 16, 16, 16, 16, 16, 20,  0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,  0, 17, 16, 16, 16, 16, 16, 20,
        17, 16,	16, 16, 16, 16, 20,  0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,  0, 17, 16, 16, 16, 16, 16, 20,
        17, 16, 16, 16, 16, 16, 20,  0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,  0, 17, 16, 16, 16, 16, 16, 20,
        17, 16, 16, 16, 16, 16, 16, 18, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 18, 16, 16, 16, 16, 16, 16, 20,
        17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 24, 16, 16, 16, 24, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,  0, 25, 16, 28,  0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        17, 16, 16, 24, 16, 16, 16, 16, 16, 16, 16, 22,  0, 29,  0, 19, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 24, 16, 16, 20,
        17, 16, 28,  0, 25, 16, 16, 16, 16, 16, 16, 16, 30,  0, 27, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 28,  0, 25, 16, 20,
        17, 20,  0,  0,  0, 17, 16, 16, 16, 16, 16, 28,  0, 23,  0, 25, 16, 16, 16, 16, 16, 16, 16, 16, 20,  0,  0,  0, 17, 20,
        17, 16, 22,  0, 19, 16, 16, 16, 16, 16, 20,  0, 19, 16, 22,  0, 17, 16,	16, 16, 16, 16, 16, 16, 16, 22,  0, 19, 16, 20,
        17, 16, 16, 18, 16, 16, 16, 16, 16, 16, 16, 18, 16, 16, 16, 18, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 18, 16, 16, 20,
        17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        17, 16, 16, 16, 16, 16, 16, 24, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 24, 16, 16, 16, 16, 16, 16, 20,
        17, 16, 16, 16, 16, 16, 20,  0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,  0, 17, 16, 16, 16, 16, 16, 20,
        17, 16, 16, 16, 16, 16, 20,  0, 17, 16, 16, 16, 24, 16, 16, 16, 24, 16, 16, 16, 16, 20,  0, 17, 16, 16, 16, 16, 16, 20,
        17, 16, 16, 16, 16, 16, 20,  0, 17, 16, 16, 20,  0, 25, 24, 28,  0, 17, 16, 16, 16, 20,  0, 17, 16, 16, 16, 16, 16, 20,
        17, 24, 16, 16, 16, 16, 20,  0, 17, 16, 16, 16, 22,  0,  0,  0, 19, 16, 16, 16, 16, 20,  0, 17, 16, 16, 16, 16, 24, 20,
        21,  0, 17, 16, 16, 16, 20,  0, 17, 16, 16, 16, 16, 18, 18, 18, 16, 16, 16, 16, 16, 20,  0, 17, 16, 16, 16, 20,  0, 21,
        21,  0, 17, 16, 16, 16, 16, 18, 16, 16, 16, 16, 28,  0,  0,  0, 25, 16, 16, 16, 16, 20,  0, 17, 16, 16, 16, 20,  0, 21,
        21,  0, 25, 24, 24, 16, 16, 16, 16, 16, 16, 20,  0, 19, 18, 22,  0, 17, 16, 16, 16, 16, 18, 16, 16, 24, 24, 28,  0, 21,
        21,  0,  0,  0,  0, 17, 16, 16, 16, 16, 16, 16, 18, 16, 16, 16, 18, 16, 16, 16, 16, 16, 16, 16, 20,  0,  0,  0,  0, 21,
        25, 26, 26, 26, 26, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 26, 26, 26, 26, 28

    };

    private final short dadosFase3[] = {
        19, 26, 18, 18, 18, 26, 18, 18, 18, 26, 18, 18, 18, 26, 18, 18, 18, 26, 18, 18, 18, 26, 18, 18, 18, 26, 18, 18, 18, 22,
        21,  0, 17, 16, 20,  0, 17, 16, 20,  0, 17, 16, 20,  0, 17, 16, 20,  0, 17, 16, 20,  0, 17, 16, 20,  0, 17, 16, 16, 20,
        21,  0, 17, 16, 20,  0, 17, 16, 20,  0, 17, 16, 20,  0, 17, 16, 20,  0, 17, 16, 20,  0, 17, 16, 20,  0, 17, 16, 16, 20,
        21,  0, 17, 16, 20,  0, 17, 16, 20,  0, 17, 16, 20,  0, 17, 16, 20,  0, 17, 16, 20,  0, 17, 16, 20,  0, 17, 16, 16, 20,
        21,  0, 17, 24, 20,  0, 17, 24, 20,  0, 17, 24, 20,  0, 17, 24, 20,  0, 17, 24, 20,  0, 17, 24, 20,  0, 17, 24, 16, 20,
        21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 17, 20,
        21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 17, 20,
        21,  0,	21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 17, 20,
        21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 17, 20,
        21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 17, 20,
        21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 17, 20,
        17, 18, 16, 18, 16, 18, 16, 18, 16, 18, 16, 18, 16, 18, 16, 18, 16, 18, 16, 18, 16, 18, 16, 18, 16, 18, 16, 18, 16, 20,
        17, 16, 16, 16, 24, 16, 16, 16, 16, 16, 16, 16, 16, 16, 24, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 24, 16, 16, 16, 20,
        17, 16, 16, 28,  0, 25, 16, 16, 16, 16, 16, 16, 16, 28,  0, 25, 16, 16, 16, 16, 16, 16, 16, 16, 28,  0, 25, 16, 16, 20,
        17, 16, 20,  0,  0,  0, 16, 16, 16, 16, 16, 16, 20,  0,  0,  0, 17, 16, 16, 16, 16, 16, 16, 20,  0,  0,  0, 17, 16, 20,
        17, 16, 16, 22,  0, 19, 16, 16, 16, 16, 16, 16, 16, 22,  0, 19, 16, 16,	16, 16, 16, 16, 16, 16, 22,  0, 19, 16, 16, 20,
        17, 16, 16, 16, 18, 16, 16, 16, 16, 16, 16, 18, 16, 16, 18, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 18, 16, 16, 16, 20,
        17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        17, 16, 24, 16, 16, 16, 24, 16, 16, 16, 24, 16, 16, 16, 24, 16, 16, 16, 24, 16, 16, 16, 24, 16, 16, 16, 24, 16, 16, 20,
        17, 20,  0, 17, 16, 20,  0, 17, 16, 20,  0, 17, 16, 20,  0, 17, 16, 20,  0, 17, 16, 20,  0, 17, 16, 20,  0, 17, 16, 20,
        17, 20,  0, 17, 16, 20,  0, 17, 16, 20,  0, 17, 16, 20,  0, 17, 16, 20,  0, 17, 16, 20,  0, 17, 16, 20,  0, 17, 16, 20,
        17, 20,  0, 17, 16, 20,  0, 17, 16, 20,  0, 17, 16, 20,  0, 17, 16, 20,  0, 17, 16, 20,  0, 17, 16, 20,  0, 17, 16, 20,
        17, 20,  0, 17, 24, 20,  0, 17, 24, 20,  0, 17, 24, 20,  0, 17, 24, 20,  0, 17, 24, 20,  0, 17, 24, 20,  0, 17, 24, 20,
        17, 20,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,
        17, 20,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,
        17, 20,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,
        17, 20,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,
        17, 20,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,
        17, 20,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,  0, 21,
        25, 24, 26, 24, 26, 24, 26, 24, 26, 24, 26, 24, 26, 24, 26, 24, 26, 24, 26, 24, 26, 24, 26, 24, 26, 24, 26, 24, 26, 28
    };

    private final short dadosFase4[] = {
        19, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22,
	    17, 16, 24, 16, 16, 16, 16, 16, 24, 16, 16, 16, 16, 16, 24, 16, 16, 16, 16, 16, 24, 16, 16, 16, 16, 16, 24, 16, 16, 20,
	    17, 28,  0, 25, 16, 16, 16, 28,  0, 25, 16, 16, 16, 28,  0, 25, 16, 16, 16, 28,  0, 25, 16, 16, 16, 28,  0, 25, 16, 20,
	    21,  0,  0,  0, 17, 16, 20,  0,  0,  0, 17, 16, 20,  0,  0,  0, 17, 16, 20,  0,  0,  0, 17, 16, 20,  0,  0,  0, 17, 20,
	    17, 22,  0, 19, 16, 16, 16, 22,  0, 19, 16, 16, 16, 22,  0, 19, 16, 16, 16, 22,  0, 19, 16, 16, 16, 22,  0, 19, 16, 20,
	    17, 16, 18, 16, 16, 16, 16, 16, 18, 16, 16, 16, 16, 16, 18, 16, 16, 16, 16, 16, 18, 16, 16, 16, 16, 16, 18, 16, 16, 20,
	    17, 16, 24, 16, 16, 16, 16, 16, 24, 16, 16, 16, 16, 16, 24, 16, 16, 16, 16, 16, 24, 16, 16, 16, 16, 16, 24, 16, 16, 20,
	    17, 28,  0, 25, 16, 16, 16, 28,  0, 25, 16, 16, 16, 28,  0, 25, 16, 16, 16, 28,  0, 25, 16, 16, 16, 28,  0, 25, 16, 20,
	    21,  0,  0,  0, 17, 16, 20,  0,  0,  0, 17, 16, 20,  0,  0,  0, 17, 16, 20,  0,  0,  0, 17, 16, 20,  0,  0,  0, 17, 20,
	    17, 22,  0, 19, 16, 16, 16, 22,  0, 19, 16, 16, 16, 22,  0, 19, 16, 16, 16, 22,  0, 19, 16, 16, 16, 22,  0, 19, 16, 20,
	    17, 16, 18, 16, 16, 16, 16, 16, 18, 16, 16, 16, 16, 16, 18, 16, 16, 16, 16, 16, 18, 16, 16, 16, 16, 16, 18, 16, 16, 20,
	    17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 24, 24, 16, 16, 16, 16, 16, 16, 16, 16, 16, 24, 20,
	    17, 16, 16, 16, 16, 24, 24, 24, 24, 16, 16, 16, 16, 16, 16, 16, 20,  0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 20,  0, 21,
	    17, 16, 16, 16, 20,  0,  0,  0,  0, 17, 16, 16, 16, 16, 16, 16, 20,  0, 25, 24, 16, 16, 16, 16, 16, 16, 24, 28,  0, 21,
	    17, 16, 16, 24, 28,  0, 19, 22,  0, 25, 24, 16, 16, 16, 16, 16, 20,  0,  0,  0, 17, 16, 16, 16, 16, 20,  0,  0,  0, 21,
	    17, 16, 20,  0,  0,  0, 17, 20,  0,  0,  0, 17, 16, 16, 16, 16, 16, 18, 22,  0, 25, 24, 16, 16, 24, 28,  0, 19, 18, 20,
	    17, 24, 28,  0, 19, 18, 16, 16, 18, 22,  0, 25, 24, 16, 16, 16, 16, 16, 20,  0,  0,  0, 17, 20,  0,  0,  0, 17, 16, 20,
	    21,  0,  0,  0, 17, 16, 16, 16, 16, 20,  0,  0,  0, 17, 16, 16, 16, 16, 16, 18, 22,  0, 25, 28,  0, 19, 18, 16, 16, 20,
	    21,  0, 19, 18, 16, 16, 16, 16, 16, 16, 18, 22,  0, 17, 16, 16, 16, 16, 16, 16, 20,  0,  0,  0,  0, 17, 16, 16, 16, 20,
        21,  0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 20,  0, 17, 16, 16, 16, 16, 16, 16, 16, 18, 18, 18, 18, 16, 16, 16, 16, 20,
	    17, 18, 24, 16, 16, 16, 16, 16, 24, 16, 16, 16, 18, 16, 24, 16, 16, 16, 16, 16, 24, 16, 16, 16, 16, 16, 24, 16, 16, 20,
	    17, 28,  0, 25, 16, 16, 16, 28,  0, 25, 16, 16, 16, 28,  0, 25, 16, 16, 16, 28,  0, 25, 16, 16, 16, 28,  0, 25, 16, 20,
	    21,  0,  0,  0, 17, 16, 20,  0,  0,  0, 17, 16, 20,  0,  0,  0, 17, 16, 20,  0,  0,  0, 17, 16, 20,  0,  0,  0, 17, 20,
	    17, 22,  0, 19, 16, 16, 16, 22,  0, 19, 16, 16, 16, 22,  0, 19, 16, 16, 16, 22,  0, 19, 16, 16, 16, 22,  0, 19, 16, 20,
	    17, 16, 18, 16, 16, 16, 16, 16, 18, 16, 16, 16, 16, 16, 18, 16, 16, 16, 16, 16, 18, 16, 16, 16, 16, 16, 18, 16, 16, 20,
	    17, 16, 24, 16, 16, 16, 16, 16, 24, 16, 16, 16, 16, 16, 24, 16, 16, 16, 16, 16, 24, 16, 16, 16, 16, 16, 24, 16, 16, 20,
	    17, 28,  0, 25, 16, 16, 16, 28,  0, 25, 16, 16, 16, 28,  0, 25, 16, 16, 16, 28,  0, 25, 16, 16, 16, 28,  0, 25, 16, 20,
	    21,  0,  0,  0, 17, 16, 20,  0,  0,  0, 17, 16, 20,  0,  0,  0, 17, 16, 20,  0,  0,  0, 17, 16, 20,  0,  0,  0, 17, 20,
	    17, 22,  0, 19, 16, 16, 16, 22,  0, 19, 16, 16, 16, 22,  0, 19, 16, 16, 16, 22,  0, 19, 16, 16, 16, 22,  0, 19, 16, 20,
	    25, 24, 26, 24, 24, 24, 24, 24, 26, 24, 24, 24, 24, 24, 26, 24, 24, 24, 24, 24, 26, 24, 24, 24, 24, 24, 26, 24, 24, 28
        
    };

    private final int velocidadesValidas[] = {1, 2, 3, 4, 6, 8};
    private final int velocidadeMaxima = 6;

    private int faseAtual = 1;
    private short[] dadosFaseAtual;
    private Image imagemFantasmaAtual;
    private int numeroFantasmasFaseAtual;

    private int velocidadeAtual = 10;
    private short[] dadosTela;
    private Timer temporizador;
    
    public Modelo() {
        
        carregarImagens();
        inicializarVariaveis();
        addKeyListener(new AdaptadorDeTeclas());
        setFocusable(true);
        iniciarJogo();
        carregarMusica(); // Adicione esta linha
        loopMusica();
    }
    

    private void carregarImagens() {
        baixo = new ImageIcon("C:/Users/ANA CLAUDIA/Downloads/teste/Lixeira FRENTE.gif").getImage();
        cima = new ImageIcon("C:/Users/ANA CLAUDIA/Downloads/teste/Lixeira costas.gif").getImage();
        esquerda = new ImageIcon("C:/Users/ANA CLAUDIA/Downloads/teste/Lixeira lado esquerdo.gif").getImage();
        direita = new ImageIcon("C:/Users/ANA CLAUDIA/Downloads/teste/Lixeira lado direito.gif").getImage();
        fantasmaFase1 = new ImageIcon("C:/Users/ANA CLAUDIA/Downloads/teste/Maçã DIREITA.gif").getImage();
        fantasmaFase2 = new ImageIcon("C:/Users/ANA CLAUDIA/Downloads/teste/Garrafa2.1.gif").getImage();
        fantasmaFase3 = new ImageIcon("C:/Users/ANA CLAUDIA/Downloads/teste/metal.gif").getImage();
        fantasmaFase4 = new ImageIcon("C:/Users/ANA CLAUDIA/Downloads/teste/papel.gif").getImage();
        coracao = new ImageIcon("C:/Users/ANA CLAUDIA/OneDrive/Desktop/Imagens_APS/heart.png").getImage();
        fundo = new ImageIcon("C:/Users/ANA CLAUDIA/OneDrive/Desktop/Imagens_APS/fundo.jpg").getImage();
        
    }
    
    public void carregarMusica() { // Removi o parâmetro
        try {
            File arquivoAudio = new File("C:/Users/ANA CLAUDIA/Downloads/pacman2_x.wav"); // Usei this.caminhoArquivo
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(arquivoAudio);
            clip = AudioSystem.getClip();
            clip.open(audioInput);
        } catch (UnsupportedAudioFileException e) {
            System.err.println("Formato de áudio não suportado: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Erro ao carregar o arquivo de áudio: " + e.getMessage());
        } catch (LineUnavailableException e) {
            System.err.println("Dispositivo de áudio não disponível: " + e.getMessage());
        }
    }

    public void loopMusica() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
    private void inicializarVariaveis() {

        dadosTela = new short[numeroDeBlocos * numeroDeBlocos];
        dimensao = new Dimension(400, 400);
        fantasmas = new java.util.ArrayList<>(); // Inicializa a lista de fantasmas

        temporizador = new Timer(40, this);
        temporizador.start();
    }

    private void definirFase(int fase) {
        faseAtual = fase;
        switch (faseAtual) {
            case 1:
                dadosFaseAtual = dadosFase1;
                imagemFantasmaAtual = fantasmaFase1;
                numeroFantasmasFaseAtual = 1;
                break;
            case 2:
                dadosFaseAtual = dadosFase2;
                imagemFantasmaAtual = fantasmaFase2;
                numeroFantasmasFaseAtual = 1;
                break;
            case 3:
                dadosFaseAtual = dadosFase3;
                imagemFantasmaAtual = fantasmaFase3;
                numeroFantasmasFaseAtual = 2;
                break;
            case 4:
                dadosFaseAtual = dadosFase4;
                imagemFantasmaAtual = fantasmaFase4;
                numeroFantasmasFaseAtual = 3;
                break;
            default:
                dadosFaseAtual = dadosFase1;
                imagemFantasmaAtual = fantasmaFase1;
                numeroFantasmasFaseAtual = 6;
                break;
        }

        dadosTela = new short[numeroDeBlocos * numeroDeBlocos];
        System.arraycopy(dadosFaseAtual, 0, dadosTela, 0, numeroDeBlocos * numeroDeBlocos);
        fantasmas.clear(); // Limpa a lista de fantasmas da fase anterior
        for (int i = 0; i < numeroFantasmasFaseAtual; i++) {
            // Define uma posição inicial para cada fantasma
            int inicioX = 4 * tamanhoBloco;
            int inicioY = 4 * tamanhoBloco;
            int velocidadeFantasma = velocidadesValidas[new Random().nextInt(velocidadesValidas.length)];
            fantasmas.add(new Fantasma(inicioX, inicioY, imagemFantasmaAtual, velocidadeFantasma, tamanhoBloco, dadosTela, numeroDeBlocos));
        }
        
        continuarNivel();
    }

    private void jogar(Graphics2D g2d) {

        if (morrendo) {
            morte();
        } else {
            moverPacman();
            desenharPacman(g2d);
            moverFantasmas(g2d);
            verificarLabirinto();
        }
    }

    private void mostrarTelaIntro(Graphics2D g2d) {

        String inicio = "Aperte espaço para começar";
        g2d.setColor(Color.yellow);
        g2d.drawString(inicio, (tamanhoTela)/4, 150);

    }

    private void desenharPontuacao(Graphics2D g) {
        g.setFont(fontePequena);
        g.setColor(new Color(5, 181, 79));
        String s = "Pontuação: " + pontuacao;
        g.drawString(s, tamanhoTela / 2 + 96, tamanhoTela + 16);

        for (int i = 0; i < vidas; i++) {
            g.drawImage(coracao, i * 28 + 8, tamanhoTela + 1, this);
        }
    }

    private void verificarLabirinto() {

        boolean terminado = true;
        for (int i = 0; i < numeroDeBlocos * numeroDeBlocos; i++) {
            if ((dadosTela[i] & 16) != 0) { // Verifica se ainda existe alguma bolinha (bit 16)
                terminado = false;
                break; // Não precisa continuar verificando se encontramos uma bolinha
            }
        }

        if (terminado) {
            pontuacao += 50;
            faseAtual++;
            definirFase(faseAtual);

            if (velocidadeAtual < velocidadeMaxima) {
                velocidadeAtual++;
            }
        }
    }

    private void morte() {
        vidas--;

        if (vidas == 0) {
            emJogo = false;
            jogoAcabou = true;
            mensagemJogoAcabou = "Fim de Jogo! Pontuação: " + pontuacao;
        }

        continuarNivel();
    }

    private void desenharGameOver(Graphics2D g2d) {
        g2d.setColor(Color.black);
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        FontMetrics metricas = getFontMetrics(g2d.getFont());
        int x = (tamanhoTela - metricas.stringWidth(mensagemJogoAcabou)) / 2;
        int y = tamanhoTela / 2;
        g2d.drawString(mensagemJogoAcabou, x, y);

        g2d.setColor(Color.yellow);
        g2d.setFont(fontePequena);
        String mensagemReiniciar = "Pressione ESPAÇO para reiniciar";
        metricas = getFontMetrics(g2d.getFont());
        int reiniciarX = (tamanhoTela - metricas.stringWidth(mensagemReiniciar)) / 2;
        int reiniciarY = y + 30;
        g2d.drawString(mensagemReiniciar, reiniciarX, reiniciarY);
    }

    private void moverFantasmas(Graphics2D g2d) {
        for (Fantasma fantasma : fantasmas) {
            fantasma.mover();
            fantasma.desenhar(g2d, this);

            // Verifica a colisão com o Pacman usando as bounding boxes
            if (pacmanX < fantasma.getX() + fantasma.getLargura() &&
                pacmanX + 23 > fantasma.getX() && // Assumindo que a largura do Pacman é 24
                pacmanY < fantasma.getY() + fantasma.getAltura() &&
                pacmanY + 23 > fantasma.getY() && // Assumindo que a altura do Pacman é 24
                emJogo) {
                morrendo = true;
            }
        }
    }

    private void moverPacman() {

        int posicao;
        short conteudo;

        if (pacmanX % tamanhoBloco == 0 && pacmanY % tamanhoBloco == 0) {
            posicao = pacmanX / tamanhoBloco + numeroDeBlocos * (int) (pacmanY / tamanhoBloco);
            conteudo = dadosTela[posicao];

            if ((conteudo & 16) != 0) {
                dadosTela[posicao] = (short) (conteudo & 15);
                pontuacao++;
            }

            if (requisicaoDeslocamentoX != 0 || requisicaoDeslocamentoY != 0) {
                if (!((requisicaoDeslocamentoX == -1 && requisicaoDeslocamentoY == 0 && (conteudo & 1) != 0)
                    || (requisicaoDeslocamentoX == 1 && requisicaoDeslocamentoY == 0 && (conteudo & 4) != 0)
                    || (requisicaoDeslocamentoX == 0 && requisicaoDeslocamentoY == -1 && (conteudo & 2) != 0)
                    || (requisicaoDeslocamentoX == 0 && requisicaoDeslocamentoY == 1 && (conteudo & 8) != 0))) {
                    pacmanDeslocamentoX = requisicaoDeslocamentoX;
                    pacmanDeslocamentoY = requisicaoDeslocamentoY;
                }
            }

            // Verifica se está parado
            if ((pacmanDeslocamentoX == -1 && pacmanDeslocamentoY == 0 && (conteudo & 1) != 0)
                || (pacmanDeslocamentoX == 1 && pacmanDeslocamentoY == 0 && (conteudo & 4) != 0)
                || (pacmanDeslocamentoX == 0 && pacmanDeslocamentoY == -1 && (conteudo & 2) != 0)
                || (pacmanDeslocamentoX == 0 && pacmanDeslocamentoY == 1 && (conteudo & 8) != 0)) {
                pacmanDeslocamentoX = 0;
                pacmanDeslocamentoY = 0;
            }
        }
        pacmanX = pacmanX + velocidadeLixo * pacmanDeslocamentoX;
        pacmanY = pacmanY + velocidadeLixo * pacmanDeslocamentoY;
    }

    private void desenharPacman(Graphics2D g2d) {

        if (requisicaoDeslocamentoX == -1) {
            g2d.drawImage(esquerda, pacmanX + 1, pacmanY + 1, this);
        } else if (requisicaoDeslocamentoX == 1) {
            g2d.drawImage(direita, pacmanX + 1, pacmanY + 1, this);
        } else if (requisicaoDeslocamentoY == -1) {
            g2d.drawImage(cima, pacmanX + 1, pacmanY + 1, this);
        } else {
            g2d.drawImage(baixo, pacmanX + 1, pacmanY + 1, this);
        }
    }

    private void desenharLabirinto(Graphics2D g2d) {

        short i = 0;
        int x, y;

        for (y = 0; y < tamanhoTela; y += tamanhoBloco) {
            for (x = 0; x < tamanhoTela; x += tamanhoBloco) {
                
                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(5));

                if ((dadosFaseAtual[i] == 0)) { // Agora usamos dadosFaseAtual
                    g2d.fillRect(x, y, tamanhoBloco, tamanhoBloco);
                }

                if ((dadosTela[i] & 1) != 0) {
                    g2d.drawLine(x, y, x, y + tamanhoBloco - 1);
                }

                if ((dadosTela[i] & 2) != 0) {
                    g2d.drawLine(x, y, x + tamanhoBloco - 1, y);
                }

                if ((dadosTela[i] & 4) != 0) {
                    g2d.drawLine(x + tamanhoBloco - 1, y, x + tamanhoBloco - 1,
                    y + tamanhoBloco - 1);
                }

                if ((dadosTela[i] & 8) != 0) {
                    g2d.drawLine(x, y + tamanhoBloco - 1, x + tamanhoBloco - 1,
                    y + tamanhoBloco - 1);
                }

                if ((dadosTela[i] & 16) != 0) {
                    g2d.setColor(Color.black);
                    g2d.fillOval(x + 10, y + 10, 6, 6);
                }

                i++;
            }
        }
    }       

    public void iniciarJogo() {

        vidas = 3;
        pontuacao = 0;
        faseAtual = 1; // Começa sempre na Fase 1
        definirFase(faseAtual);
        velocidadeAtual = 3;
        jogoAcabou = false;
    } 

    private void continuarNivel() {
        pacmanX = 7 * tamanhoBloco;
        pacmanY = 11 * tamanhoBloco;
        pacmanDeslocamentoX = 0;
        pacmanDeslocamentoY = 0;
        requisicaoDeslocamentoX = 0;
        requisicaoDeslocamentoY = 0;
        morrendo = false;

        // Reinicializa a posição e a direção dos fantasmas para a nova fase
        Random random = new Random();
        for (Fantasma fantasma : fantasmas) {
            fantasma.setX(4 * tamanhoBloco);
            fantasma.setY(4 * tamanhoBloco);
            fantasma.setDx(0);
            fantasma.setDy(0);
            int direcao = random.nextInt(4);
            switch (direcao) {
                case 0: fantasma.setDy(-1); break;
                case 1: fantasma.setDy(1); break;
                case 2: fantasma.setDx(-1); break;
            case 3: fantasma.setDx(1); break;
            }
            fantasma.setVelocidade(velocidadesValidas[random.nextInt(velocidadesValidas.length)]);
        }
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.drawImage(fundo, 0, 0, getWidth(), getHeight(), this);

        desenharLabirinto(g2d);
        desenharPontuacao(g2d);

        if (emJogo) {
            jogar(g2d);
        } else if (jogoAcabou) {
            desenharGameOver(g2d);
        } else {
            mostrarTelaIntro(g2d);
        }

        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }


    // Controles
    class AdaptadorDeTeclas extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (emJogo) {
                if (key == KeyEvent.VK_LEFT) {
                    requisicaoDeslocamentoX = -1;
                    requisicaoDeslocamentoY = 0;
                } else if (key == KeyEvent.VK_RIGHT) {
                    requisicaoDeslocamentoX = 1;
                    requisicaoDeslocamentoY = 0;
                } else if (key == KeyEvent.VK_UP) {
                    requisicaoDeslocamentoX = 0;
                    requisicaoDeslocamentoY = -1;
                } else if (key == KeyEvent.VK_DOWN) {
                    requisicaoDeslocamentoX = 0;
                    requisicaoDeslocamentoY = 1;
                } else if (key == KeyEvent.VK_ESCAPE && temporizador.isRunning()) {
                    emJogo = false;
                }
            } else {
                if (key == KeyEvent.VK_SPACE) {
                    if (jogoAcabou) {
                        jogoAcabou = false; // Reseta o estado de fim de jogo
                        iniciarJogo(); // Reinicia o jogo
                    } else {
                        emJogo = true;
                        iniciarJogo();
                    }
                }   
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
