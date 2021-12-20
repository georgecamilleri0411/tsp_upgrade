public class GA {

	private static final double mutationRate = 0.025;
	private static final int selectionTmentSize = 5;
	private static final boolean useElitism = true;

	/*
	Evolve the population by crossing over from 2 parents, then
	using mutation (randomised) to swap 2 city positions with each other.
	 */
	public static Population evolvePopulation(Population population) {
		Population myPopulation = new Population(population.populationSize(), false);

		// Keep the fittest journey to preserve the fittest (elite) journeys (chromosomes).
		int eCutOff = 0;
		if (useElitism) {
			myPopulation.saveJourney(0, population.getFittestJourney());
			eCutOff = 1;
		}

		// Crossover the population by mating two parents and hoping that their
		// best traits (genes) are inherited by the offspring
		for (int i = eCutOff; i < myPopulation.populationSize(); i++) {
			// Choose the parents using a tournament selection process (i.e. grouped in 5).
			Journey parent1 = tmentSelection(population);
			Journey parent2 = tmentSelection(population);
			// Crossover parents to create their offspring
			Journey offspring = crossover(parent1, parent2);
			// Add the new offspring to new population
			myPopulation.saveJourney(i, offspring);
		}

		// Mutate the new population slightly
		for (int i = eCutOff; i < myPopulation.populationSize(); i++) {
			mutate(myPopulation.getJourney(i));
		}

		return myPopulation;
	}

	/*
	Apply the crossover from 2 parents to produce an offspring
	 */
	public static Journey crossover(Journey parent1, Journey parent2) {
		// Offspring
		Journey offspring = new Journey();

		// Select the start and end journey offsets for parent1
		int startOffset = (int) (Math.random() * parent1.journeySize());
		int endOffset = (int) (Math.random() * parent1.journeySize());

		// Inherit the genes within the start and end offsets by the offspring
		for (int i = 0; i < offspring.journeySize(); i++) {
			if ((startOffset < endOffset) && (i > startOffset) && (i < endOffset)) {
				offspring.setCity(i, parent1.getCity(i));
			} else if (startOffset > endOffset) {
				if (!((i < startOffset) && (i > endOffset))) {
					offspring.setCity(i, parent1.getCity(i));
				}
			}
		}

		// Inherit the genes from the second parent
		for (int i = 0; i < parent2.journeySize(); i++) {
			if (!offspring.cityExists (parent2.getCity(i))) {
				// Find a null value within the offspring journey
				for (int o = 0; o < offspring.journeySize(); o++) {
					if (offspring.getCity(o) == null) {
						offspring.setCity(o, parent2.getCity(i));
						break;
					}
				}
			}
		}
		return offspring;
	}

	/*
	Mutate a journey by swapping 2 random cities. These cannot be either
	the first of the last cities.
	 */
	private static void mutate (Journey journey) {
		for (int c1 = 0; c1 < journey.journeySize(); c1++) {
			// Use the mutation rate to set the mutation threshold
			if (Math.random() < mutationRate) {
				int c2 = (int)(Math.random() * journey.journeySize());

				// Get the cities at generated positions in journey
				GA_City city1 = journey.getCity(c1);
				GA_City city2 = journey.getCity(c2);

				// Swap them around
				journey.setCity(c1, city2);
				journey.setCity(c2, city1);
			}
		}
	}

	/*
	Tournament selector (groups of 5) to select candidates for crossover
	 */
	private static Journey tmentSelection(Population population) {
		Population tournament = new Population(selectionTmentSize, false);
		// Find candidate journeys for each place in the tournament using randomisation
		for (int i = 0; i < selectionTmentSize; i++) {
			int randomId = (int) (Math.random() * population.populationSize());
			tournament.saveJourney(i, population.getJourney(randomId));
		}
		// Find the fittest journey in this tournament. This will be used as one of
		// the parents for the crossover process.
		Journey fittest = tournament.getFittestJourney();
		return fittest;
	}

}
