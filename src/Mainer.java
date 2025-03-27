import java.util.Random;

public class Mainer {
	
	public static final int IMAGE_WIDTH = 200;
	public static final int IMAGE_HEIGHT = 200;
	public static final Random rng = new Random();
	
	public static final int POPULATION_SIZE = 100;
	public static final int GENERATIONS = 10000;
	public static final int ELITE = 10;
	public static final int RANDOMS = 10;
	
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
			fitnessAdjustment(population);
			
			// Sort the population
			sort(population);
			
			// Display the best individual in the current population
			app.paintImage(population[0]);
			System.out.println(population[0].fitness);
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
		int selection1 = rng.nextInt(fitnessSum);
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
