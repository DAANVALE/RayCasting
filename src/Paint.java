import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class Paint extends JPanel {

    private BufferedImage buffer;
    public Graphics graphics;

    BufferedImage[] bufferGrid = new BufferedImage[3];

    int bufferSize = 20;

    public Block[][] blocks = new Block[10][10];

    public Paint(Dimension size) {
        setSize(size);
        buffer = new BufferedImage(size.width, size.height, BufferedImage.TRANSLUCENT);
        setDefaultBlocks();
        graphics = (Graphics) buffer.getGraphics();
        graphics.setColor(Color.black);
    }

    public void setDefaultBlocks(){
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                blocks[i][j] = new Block(i, j, Block.States.FREE);
            }
        }
    }

    @Override
    public void setSize(Dimension size) {
        buffer = new BufferedImage(size.width, size.height, BufferedImage.TRANSLUCENT);
        super.setSize(size);
    }

    public void putPixel(int x, int y){
        putPixel(x,y,graphics.getColor());
    }

    public void putPixel(int x, int y, Color c) {
        if (x >= 0 && x < buffer.getWidth() && y >= 0 && y < buffer.getHeight()) {
            buffer.setRGB(x, y, c.getRGB());
        }
    }

    public void drawLine(int x0, int y0, int x1, int y1, int thickness){
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);

        // Determine if the line is steep (slope > 1)
        boolean steep = dy > dx;

        // Swap x and y if the line is steep
        if (steep) {
            int temp = x0;
            x0 = y0;
            y0 = temp;

            temp = x1;
            x1 = y1;
            y1 = temp;
        }

        // Ensure that x0 < x1
        if (x0 > x1) {
            int temp = x0;
            x0 = x1;
            x1 = temp;

            temp = y0;
            y0 = y1;
            y1 = temp;
        }

        // Recalculate differentials
        dx = x1 - x0;
        dy = Math.abs(y1 - y0);

        // Initialize error term and increments
        int err = dx / 2;
        int yStep = (y0 < y1) ? 1 : -1;
        int y = y0;

        // Loop through points between x0 and x1
        for (int x = x0; x <= x1; x++) {
            // Draw pixels for each point within the line thickness
            for (int i = -thickness / 2; i < thickness / 2; i++) {
                if (steep) {
                    putPixel(y + i, x);
                } else {
                    putPixel(x, y + i);
                }
            }

            // Update error term and y coordinate
            err -= dy;
            if (err < 0) {
                y += yStep;
                err += dx;
            }
        }
    }

    public void drawLine(int x0, int y0, int x1, int y1) {
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);

        byte sx = (byte)(x0 < x1 ? 1 : -1);
        byte sy = (byte)(y0 < y1 ? 1 : -1);

        int err = dx - dy;
        int err2;

        int x = x0;
        int y = y0;

        while (x != x1 || y != y1) {
            putPixel(x, y);
            err2 = 2 * err;
            if (err2 > -dy) {
                err -= dy;
                x += sx;
            }
            if (err2 < dx) {
                err += dx;
                y += sy;
            }
        }
    }

    public void drawSquare(int x, int y, int sideLength) {
        int x2 = x + sideLength;
        int y2 = y + sideLength;

        // Dibujar los cuatro lados del cuadrado usando drawLine()
        drawLine(x, y, x2, y); // Lado superior
        drawLine(x2, y, x2, y2); // Lado derecho
        drawLine(x2, y2, x, y2); // Lado inferior
        drawLine(x, y2, x, y); // Lado izquierdo
    }

    public void drawRectangle(int x, int y, int width, int height, boolean byFillSize) {

        int x2 = width;
        int y2 = height;

        if(byFillSize){
            x2 = x + width;
            y2 = y + height;
        }

        drawRectangle(x, y, x2, y2);
    }

    public void drawRectangle(int x, int y, int x2, int y2) {
        // Dibujar los cuatro lados del rectángulo usando drawLine()
        drawLine(x, y, x2, y); // Lado superior
        drawLine(x2, y, x2, y2); // Lado derecho
        drawLine(x2, y2, x, y2); // Lado inferior
        drawLine(x, y2, x, y); // Lado izquierdo
    }

    public void drawIrregularFigure(int vertices[]){

        if(vertices.length < 6 || vertices.length % 2 != 0){
            return;
        }

        for(int i = 0; i < vertices.length - 2; i += 2) {
            drawLine(vertices[i], vertices[i+1], vertices[i+2], vertices[i+3], 3);
        }

        drawLine(vertices[0], vertices[1], vertices[vertices.length - 2], vertices[vertices.length - 1], 3);
    }

    public void drawPlayer(double vertices[][]){
        int[] newVertice = new int[vertices.length * 2];

        for(int i = 0; i < (vertices.length * 2) ; i+=2){
            newVertice[i] = (int)vertices[i/2][0];
            newVertice[i+1] = (int)vertices[i/2][1];
        }

        drawIrregularFigure(newVertice);
    }

    public void floodFill(int x, int y, Color fillColor) {
        Color targetColor = new Color(buffer.getRGB(x, y));
        if (targetColor.equals(fillColor)) {
            // No necesitamos rellenar si el color objetivo es el mismo que el color de relleno
            return;
        }

        java.util.Queue<Point> queue = new java.util.LinkedList<>();
        queue.add(new Point(x, y));

        while (!queue.isEmpty()) {
            Point p = queue.poll();
            int px = p.x;
            int py = p.y;

            if (px < 0 || px >= buffer.getWidth() || py < 0 || py >= buffer.getHeight() ||
                    !new Color(buffer.getRGB(px, py)).equals(targetColor)) {
                continue;
            }

            putPixel(px, py, new Color(fillColor.getRGB()));

            queue.add(new Point(px - 1, py)); // Píxel izquierdo
            queue.add(new Point(px + 1, py)); // Píxel derecho
            queue.add(new Point(px, py - 1)); // Píxel superior
            queue.add(new Point(px, py + 1)); // Píxel inferior
        }

        repaint();
    }

    public void drawCircle(int xc, int yc, int radius) {
        int x = 0;
        int y = radius;
        int p = 3 - 2 * radius;

        drawCirclePointsOctant(xc, yc, x, y);

        while (x <= y) {
            x++;
            if (p > 0) {
                y--;
                p = p + 4 * (x - y) + 10;
            } else {
                p = p + 4 * x + 6;
            }
            drawCirclePointsOctant(xc, yc, x, y);
        }
    }

    public void drawElipse(int xc, int yc, int rx, int ry){
        int x = 0;
        int y = ry;
        int rx2 = rx * rx;
        int ry2 = ry * ry;
        int d = 4 * ry2 - 4 * rx2 * ry + rx2;
        int deltaE = 4 * ry2;
        int deltaSE = 4 * ry2 - 2 * rx2 * ry + 2 * rx2;

        drawCirclePointsQuadrant(xc, yc, x, y);

        while (2 * ry2 * x < 2 * rx2 * y) {
            if (d >= 0) {
                y--;
                d += deltaSE;
                deltaSE += 2 * rx2;
            }
            x++;
            d += deltaE;
            deltaE += 2 * ry2;
            drawCirclePointsQuadrant(xc, yc, x, y);
        }

        d = (int)(
                ry2 * (x + 0.5) * (x + 0.5) +
                        rx2 * (y - 1) * (y - 1) -
                        rx2 * ry2
        );

        while (y >= 0) {
            drawCirclePointsQuadrant(xc, yc, x, y);
            if (d <= 0) {
                x++;
                d += 2 * ry2 * x + ry2;
            }
            y--;
            d -= 2 * rx2 * y + rx2;
        }
    }

    private void drawCirclePointsOctant(int xc, int yc, int x, int y) {
        drawCirclePointsQuadrant(xc, yc, x, y);
        drawCirclePointsQuadrant(xc, yc, y, x);
    }

    private void drawCirclePointsQuadrant(int xc, int yc, int x, int y){
        putPixel(xc + x, yc + y); // Cuadrante 1
        putPixel(xc - x, yc + y); // Cuadrante 2
        putPixel(xc + x, yc - y); // Cuadrante 3
        putPixel(xc - x, yc - y); // Cuadrante 4
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        paintBlocks(g);
        g.drawImage(buffer, 0, 0, this);
    }

    public void initGridBuffer(){
        for(int i = 0; i < bufferGrid.length; i++){
            bufferGrid[i] = new BufferedImage(bufferSize, bufferSize, BufferedImage.TRANSLUCENT);
        }
    }

    public void setBufferBackground(BufferedImage image, Color color){
        for(int i = 0; i < image.getWidth(); i++){
            for(int j = 0; j < image.getHeight(); j++){
                image.setRGB(i, j, color.getRGB());
            }
        }
    }

    private void paintBlocks(Graphics g){

        initGridBuffer();

        setBufferBackground(bufferGrid[0], new Color(180, 180, 180)); // Para el número 0 (fondo gris claro)
        setBufferBackground(bufferGrid[1], new Color(120, 120, 120));
        setBufferBackground(bufferGrid[2], new Color(80, 2, 44));

        for(int i = 0; i < blocks.length; i++){
            for(int j = 0; j < blocks[0].length; j++){

                if(blocks[i][j].state == Block.States.BLOCK){
                    g.drawImage(bufferGrid[2], bufferSize * (i+1), bufferSize * (j+1), this);
                    continue;
                }

                if( (i + j) % 2 == 0){
                    g.drawImage(bufferGrid[0], bufferSize * (i+1), bufferSize * (j+1), this);
                }else{
                    g.drawImage(bufferGrid[1], bufferSize * (i+1), bufferSize * (j+1), this);
                }
            }
        }
    }

    public void changeBlocks(int px, int py){
        int x = (px - bufferSize) / bufferSize;
        int y = (py - bufferSize) / bufferSize;

        System.out.println("x: " + x + " y: " + y + "\n");

        if(x < 0 || x >= 10){
            return;
        }

        if(y < 0 || y >= 10){
            return;
        }

        blocks[x][y].changeState();
    }

    public void movePlayerWithLimits() {

    }
}