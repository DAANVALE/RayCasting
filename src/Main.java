import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;

public class Main {

    JFrame frame = new JFrame();
    Paint3D paintPanel;
    Point3D reference = new Point3D(150, 450, 50);
    int width = 1, height = 1;

    public Main() {

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                System.out.println("Exiting...");
                frame.dispose();
                System.gc();
                System.exit(0);
            }
        });

        frame.setSize(300,300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        paintPanel = new Paint3D(getPanelSize(frame), new Point3D[]{reference});
        frame.add(paintPanel);

        paintPanel.graphics.setColor(Color.blue);

        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {

                if(getPanelSize(frame).height < 20){
                    return;
                }

                paintPanel.setSize(getPanelSize(frame));
                width  = paintPanel.getWidth();
                height = paintPanel.getHeight();

                paintPanel.references = new Point3D[]{reference};
                paintPanel.drawPlayer(Player.dinamicArrow());
            }
        });

        frame.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {

                int keyCode = e.getKeyCode();
                Player.movePlayer(keyCode);

                paintPanel.setSize(getPanelSize(frame));
                paintPanel.drawPlayer(Player.dinamicArrow());
                paintPanel.repaint();
            }
        });

        paintPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                paintPanel.changeBlocks(e.getX(), e.getY());
                changeBlocks();
            }
        });

        paintPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                paintPanel.changeBlocks(e.getX(), e.getY());
                changeBlocks();
            }
        });

        paintPanel.drawPlayer(Player.dinamicArrow());
        frame.setVisible(true);
    }

    public void changeBlocks(){
        paintPanel.setSize(getPanelSize(frame));
        paintPanel.drawPlayer(Player.dinamicArrow());
        paintPanel.repaint();
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                new Main();
            }
        });
    }

    public static Dimension getPanelSize(JFrame window){
        return new Dimension(window.getWidth() - window.getInsets().left - window.getInsets().right, window.getHeight() - window.getInsets().top - window.getInsets().bottom);
    }
}