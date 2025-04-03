import java.util.Random;

public class Mainer {
	
	public static final int IMAGE_WIDTH = 20;
	public static final int IMAGE_HEIGHT = 20;
	public static final Random rng = new Random();
	
	public static final int POPULATION_SIZE = 100;
	public static final int GENERATIONS = 10000;
	public static final int ELITE = 10;
	public static final int RANDOMS = 0;
	
	public static final int DISPLAY_FREQUENCY = 20;
	
	public static void main(String[] args) {
		
		// Initialise image window
		ImageWindow app = new ImageWindow(IMAGE_WIDTH, IMAGE_HEIGHT);
		
		// Initialise random population
		GeneratedImage[] population = new GeneratedImage[POPULATION_SIZE];
		for(int i  = 0; i < POPULATION_SIZE; i++)
			population[i] = randomImage();
		
		// Run GA
		for(int g = 0; g < GENERATIONS; g++) {
			
			// Evaluate unevaluated images
			for(int i = 0; i < POPULATION_SIZE; i++) {
				// TODO: optimise fitness calculations by running them at the point of image generation only
				//if(population[i].fitness == -1) // disable this check if using fitness adjustments
					population[i].fitness = fitness(population[i]);
				//System.out.println(population[i].fitness);
			}
			
			// Apply fitness adjustment to improve performance
			//fitnessAdjustment(population);
			
			// Sort the population
			sort(population);
			
			// Display the best individual in the current population
			if(g % DISPLAY_FREQUENCY == 0) {
				app.paintImage(population[0]);
				System.out.println(population[0].fitness);
			}
			//sleep(1000);
			
			// Generate the next population
			GeneratedImage[] nextPopulation = new GeneratedImage[POPULATION_SIZE];
			
			// Copy over the elite
			for(int i = 0; i < ELITE; i++) {
				//System.out.println(i);
				nextPopulation[i] = population[i];
			}
			
			// Produce new images using selection/crossover
			for(int i = ELITE; i < POPULATION_SIZE - RANDOMS; i++) {
				//System.out.println(i);
				GeneratedImage[] parents = selection(population);
				nextPopulation[i] = crossover(parents[0], parents[1]);
				
				// Mutate some of the new individuals
				if(rng.nextDouble() < 0.3)
					mutation(nextPopulation[i]);
			}
			
			// Add random images to the population
			for(int i = POPULATION_SIZE - RANDOMS; i < POPULATION_SIZE; i++) {
				//System.out.println(i);
				nextPopulation[i] = randomImage();
			}
			
			// Replace the population
			population = nextPopulation;
		}
		
	}
	
	public static GeneratedImage randomImage() {
		GeneratedImage image = new GeneratedImage();
		image.generateRandom();
		return image;
	}
	
	public static int fitness(GeneratedImage image) {
		return adjacentDiff(image);
	}
	
	public static int adjacentDiff(GeneratedImage image) {
		int fitness = 0;
		for(int y = 1; y < Mainer.IMAGE_HEIGHT - 1; y++) {
			for(int x = 1; x < Mainer.IMAGE_WIDTH - 1; x++) {
				int topDiff = 0;
				topDiff += Math.abs(image.pixels[y][x][0] - image.pixels[y-1][x][0]);
				topDiff += Math.abs(image.pixels[y][x][1] - image.pixels[y-1][x][1]);
				topDiff += Math.abs(image.pixels[y][x][2] - image.pixels[y-1][x][2]);
				int botDiff = 0;
				botDiff += Math.abs(image.pixels[y][x][0] - image.pixels[y+1][x][0]);
				botDiff += Math.abs(image.pixels[y][x][1] - image.pixels[y+1][x][1]);
				botDiff += Math.abs(image.pixels[y][x][2] - image.pixels[y+1][x][2]);
				int leftDiff = 0;
				leftDiff += Math.abs(image.pixels[y][x][0] - image.pixels[y][x-1][0]);
				leftDiff += Math.abs(image.pixels[y][x][1] - image.pixels[y][x-1][1]);
				leftDiff += Math.abs(image.pixels[y][x][2] - image.pixels[y][x-1][2]);
				int rightDiff = 0;
				rightDiff += Math.abs(image.pixels[y][x][0] - image.pixels[y][x+1][0]);
				rightDiff += Math.abs(image.pixels[y][x][1] - image.pixels[y][x+1][1]);
				rightDiff += Math.abs(image.pixels[y][x][2] - image.pixels[y][x+1][2]);
				int topContrastDiff = Math.abs(image.pixels[y][x][0] + image.pixels[y][x][1] + image.pixels[y][x][2] - image.pixels[y-1][x][0] - image.pixels[y-1][x][1] - image.pixels[y-1][x][2]);
				int botContrastDiff = Math.abs(image.pixels[y][x][0] + image.pixels[y][x][1] + image.pixels[y][x][2] - image.pixels[y+1][x][0] - image.pixels[y+1][x][1] - image.pixels[y+1][x][2]);
				int leftContrastDiff = Math.abs(image.pixels[y][x][0] + image.pixels[y][x][1] + image.pixels[y][x][2] - image.pixels[y][x-1][0] - image.pixels[y][x-1][1] - image.pixels[y][x-1][2]);
				int rightContrastDiff = Math.abs(image.pixels[y][x][0] + image.pixels[y][x][1] + image.pixels[y][x][2] - image.pixels[y][x+1][0] - image.pixels[y][x+1][1] - image.pixels[y][x+1][2]);
				fitness += (topDiff + botDiff + leftDiff + rightDiff + topContrastDiff + botContrastDiff + leftContrastDiff + rightContrastDiff);
			}
		}
		return fitness;
	}
	
	public static int contrast(GeneratedImage image) {
		int fitness = 0;
		for(int y = 0; y < Mainer.IMAGE_HEIGHT; y++) {
			for(int x = 0; x < Mainer.IMAGE_WIDTH; x++) {
				int max = Math.max(Math.max(image.pixels[y][x][0], image.pixels[y][x][1]), image.pixels[y][x][2]);
				int secondMax;
				if(image.pixels[y][x][0] == max)
					secondMax = Math.max(image.pixels[y][x][1], image.pixels[y][x][2]);
				else if(image.pixels[y][x][1] == max)
					secondMax = Math.max(image.pixels[y][x][0], image.pixels[y][x][2]);
				else
					secondMax = Math.max(image.pixels[y][x][0], image.pixels[y][x][1]);
				fitness += max - secondMax;
			}
		}
		return fitness;
	}
	
	public static int brightness(GeneratedImage image) {
		int fitness = 0;
		for(int y = 0; y < Mainer.IMAGE_HEIGHT; y++) {
			for(int x = 0; x < Mainer.IMAGE_WIDTH; x++) {
				for(int c = 0; c < 3; c++) {
					fitness += image.pixels[y][x][c];
				}
			}
		}
		return fitness;
	}
	
	public static int darkness(GeneratedImage image) {
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
	
	public static GeneratedImage[] selection(GeneratedImage[] population) {
		int fitnessSum = 0;
		for(GeneratedImage image : population) {
			fitnessSum += image.fitness;
		}
		int selection1 = rng.nextInt(fitnessSum); // TODO: fitness sum can become negative smh?
		GeneratedImage image1 = population[0];
		for(int i = 0; i < POPULATION_SIZE; i++) {
			selection1 -= population[i].fitness;
			if(selection1 <= 0) {
				image1 = population[i];
				break;
			}
		}
		GeneratedImage image2 = image1;
		while(image1 == image2) {
			int selection2 = rng.nextInt(fitnessSum);
			for(int i = 0; i < POPULATION_SIZE; i++) {
				selection2 -= population[i].fitness;
				if(selection2 <= 0) {
					image2 = population[i];
					break;
				}
			}
		}
		return new GeneratedImage[] {image1, image2};
	}
	
	public static GeneratedImage crossover(GeneratedImage image1, GeneratedImage image2) {
		GeneratedImage result = new GeneratedImage();
		int totalFitness = image1.fitness + image2.fitness;
		double p1 = (0.0 + image1.fitness) / (0.0 + totalFitness);
		for(int y = 0; y < Mainer.IMAGE_HEIGHT; y++) {
			for(int x = 0; x < Mainer.IMAGE_WIDTH; x++) {
				for(int c = 0; c < 3; c++) {
					if(rng.nextDouble() <= p1) 
						result.pixels[y][x][c] = image1.pixels[y][x][c];
					else 
						result.pixels[y][x][c] = image2.pixels[y][x][c];
				}
			}
		}
		return result;
	}
	
	public static void sort(GeneratedImage[] arr) {
		for(int i = 0; i < arr.length - 1; i++)
			for(int j = i+1; j < arr.length; j++)
				if(arr[i].fitness < arr[j].fitness) {
					GeneratedImage temp = arr[i];
					arr[i] = arr[j];
					arr[j] = temp;
				}
	}
	
	public static void mutation(GeneratedImage image) {
		for(int i = 0; i < 100; i++)
			onePixelChange(image);
	}
	
	public static void onePixelChange(GeneratedImage image) {
		int y = rng.nextInt(Mainer.IMAGE_HEIGHT);
		int x = rng.nextInt(Mainer.IMAGE_WIDTH);
		int originalRed = image.pixels[y][x][0];
		int originalGreen = image.pixels[y][x][1];
		int originalBlue = image.pixels[y][x][2];
		int originalFitness = fitness(image);
		int newFitness = originalFitness;
		int count = 0;
		while(newFitness <= originalFitness && count < 3) {
			image.pixels[y][x][0] = rng.nextInt(256);
			image.pixels[y][x][1] = rng.nextInt(256);
			image.pixels[y][x][2] = rng.nextInt(256);
			newFitness = fitness(image);
			count++;
		}
		if(newFitness < originalFitness) {
			image.pixels[y][x][0] = originalRed;
			image.pixels[y][x][1] = originalGreen;
			image.pixels[y][x][2] = originalBlue;
		}
	}
	
	public static void brightnessAdjustment(GeneratedImage image) {
		int adjustment = rng.nextInt(11) - 5;
		for(int y = 0; y < Mainer.IMAGE_HEIGHT; y++) {
			for(int x = 0; x < Mainer.IMAGE_WIDTH; x++) {
				for(int c = 0; c < 3; c++) {
					if(rng.nextDouble() <= 0.5) {
						image.pixels[y][x][c] = Math.min(255, Math.max(0, image.pixels[y][x][c] + adjustment));
					}
				}
			}
		}
	}
	

	public static void fitnessAdjustment(GeneratedImage[] population) {
		int minFitness = Integer.MAX_VALUE;
		for(GeneratedImage image : population)
			if(minFitness > image.fitness)
				minFitness = image.fitness;
		for(GeneratedImage image : population) {
			image.fitness -= minFitness + 1;
		}
			
	}
	
	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	

}
