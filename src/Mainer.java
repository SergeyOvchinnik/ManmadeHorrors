import java.util.Random;

public class Mainer {
	
	public static final int IMAGE_WIDTH = 800;
	public static final int IMAGE_HEIGHT = 600;
	public static final Random rng = new Random();
	
	public static final int POPULATION_SIZE = 10;
	public static final int GENERATIONS = 100;
	
	public static void main(String[] args) {
		ImageWindow app = new ImageWindow(IMAGE_WIDTH, IMAGE_HEIGHT);
		GeneratedImage image = new GeneratedImage();
		image.generateRandom();
		app.paintImage(image);
		while(true) {
			image.generateRandom();
			app.paintImage(image);
		}
		
		
	}
	
	public GeneratedImage randomImage() {
		GeneratedImage image = new GeneratedImage();
		image.generateRandom();
		return image;
	}
	
	public int fitness(GeneratedImage image) {
		int fitness = IMAGE_WIDTH * IMAGE_HEIGHT * 3 * 255;
		for(int y = 0; y < Mainer.IMAGE_HEIGHT; y++) {
			for(int x = 0; x < Mainer.IMAGE_WIDTH; x++) {
				for(int c = 0; c < 3; c++) {
					fitness -= image.pixels[y][x][c];
				}
			}
		}
		return fitness;
	}
	
	public GeneratedImage crossOver(GeneratedImage image1, GeneratedImage image2) {
		return null;
	}
	
	public GeneratedImage mutation(GeneratedImage image) {
		return null;
	}
	
	
	

}
