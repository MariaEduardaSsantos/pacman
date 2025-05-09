import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Random;

public class Fantasma {
    private int x, y;
    private int dx, dy;
    private int velocidade;
    private Image imagem;
    private int tamanhoBloco;
    private short[] screenData;
    private int nBlocos;

    public Fantasma(int xInicial, int yInicial, Image imagemInicial, int velocidadeInicial, int tamanhoBloco, short[] screenData, int nBlocos) {
        this.x = xInicial;
        this.y = yInicial;
        this.imagem = imagemInicial;
        this.velocidade = velocidadeInicial;
        this.tamanhoBloco = tamanhoBloco;
        this.screenData = screenData;
        this.nBlocos = nBlocos;
        this.dx = 0;
        this.dy = 0;
        // Inicializa a direção do fantasma aleatoriamente
        Random random = new Random();
        int direcao = random.nextInt(4); // 0: cima, 1: baixo, 2: esquerda, 3: direita
        switch (direcao) {
            case 0: dy = -1; break;
            case 1: dy = 1; break;
            case 2: dx = -1; break;
            case 3: dx = 1; break;
        }
    }

    public void mover() {
        if (x % tamanhoBloco == 0 && y % tamanhoBloco == 0) {
            int pos = x / tamanhoBloco + nBlocos * (y / tamanhoBloco);
            int count = 0;
            int[] possibleDx = new int[4];
            int[] possibleDy = new int[4];

            // Verifica se pode ir para cima e não está indo para baixo
            if ((screenData[pos] & 2) == 0 && dy != 1) { possibleDx[count] = 0; possibleDy[count++] = -1; }
            // Verifica se pode ir para baixo e não está indo para cima
            if ((screenData[pos] & 8) == 0 && dy != -1) { possibleDx[count] = 0; possibleDy[count++] = 1; }
            // Verifica se pode ir para a esquerda e não está indo para a direita
            if ((screenData[pos] & 1) == 0 && dx != 1) { possibleDx[count] = -1; possibleDy[count++] = 0; }
            // Verifica se pode ir para a direita e não está indo para a esquerda
            if ((screenData[pos] & 4) == 0 && dx != -1) { possibleDx[count] = 1; possibleDy[count++] = 0; }

            if (count > 0) {
                Random random = new Random();
                int escolha = random.nextInt(count);
                dx = possibleDx[escolha];
                dy = possibleDy[escolha];
            } else {
                // Se não há outras direções, inverte a atual
                dx = -dx;
                dy = -dy;
            }
        }
        x += dx * velocidade;
        y += dy * velocidade;
    }

    public void desenhar(Graphics2D g2d, Modelo panel) {
        g2d.drawImage(imagem, x + 1, y + 1, panel);
    }

    public int getX() { return x; }
    public int getY() { return y; }

    // Getters para facilitar a detecção de colisão no Modelo
    public int getLargura() { return imagem.getWidth(null); }
    public int getAltura() { return imagem.getHeight(null); }

    public void setX(int novoX) { this.x = novoX; }
    public void setY(int novoY) { this.y = novoY; }
    public void setDx(int novoDx) { this.dx = novoDx; }
    public void setDy(int novoDy) { this.dy = novoDy; }
    public void setVelocidade(int novaVelocidade) { this.velocidade = novaVelocidade; }

}