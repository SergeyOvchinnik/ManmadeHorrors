
public class GeneratedImage {
	public int[][][] pixels;
	public int fitness;
	
	public GeneratedImage() {
		pixels = new int[Mainer.IMAGE_HEIGHT][Mainer.IMAGE_WIDTH][3];
	}
	
	public void generateRandom() {
		for(int y = 0; y < Mainer.IMAGE_HEIGHT; y++) {
			for(int x = 0; x < Mainer.IMAGE_WIDTH; x++) {
				for(int c = 0; c < 3; c++)
					pixels[y][x][c] = Mainer.rng.nextInt(256);
			}
		}
	}
}
