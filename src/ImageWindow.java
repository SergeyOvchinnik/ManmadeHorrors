import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageWindow {

    private JFrame frame;
    private DrawingPanel drawingPanel;

    public ImageWindow(int width, int height) {
        frame = new JFrame("Manmade Horrors");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);

        drawingPanel = new DrawingPanel(width, height);
        frame.add(drawingPanel);

        frame.setVisible(true);
    }

    public void drawPixel(int x, int y, Color color) {
        drawingPanel.setPixel(x, y, color);
    }
    
    public void paintImage(GeneratedImage image) {
    	for(int y = 0; y < Mainer.IMAGE_HEIGHT; y++) {
			for(int x = 0; x < Mainer.IMAGE_WIDTH; x++) {
				for(int c = 0; c < 3; c++) {
					drawPixel(x, y, new Color(image.pixels[y][x][0], image.pixels[y][x][1], image.pixels[y][x][2]));
				}
					
			}
		}
    }

    private class DrawingPanel extends JPanel {
        private BufferedImage canvas;

        public DrawingPanel(int width, int height) {
        	this.setSize(width, height);
        	System.out.println(getWidth() + " by " + getHeight());
            canvas = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
            clearCanvas();
        }

        public void setPixel(int x, int y, Color color) {
            if (x >= 0 && x < getWidth() && y >= 0 && y < getHeight()) {
                canvas.setRGB(x, y, color.getRGB());
                repaint();
            }
        }

        public void clearCanvas() {
            Graphics2D g2d = canvas.createGraphics();
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.dispose();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(canvas, 0, 0, this);
        }
    }

    public static void main(String[] args) {
    	ImageWindow app = new ImageWindow(800, 600);

        // Example usage: Draw a diagonal red line
        for (int i = 0; i < 800 && i < 600; i++) {
            app.drawPixel(i, i, Color.RED);
        }
    }
}