public class Population {

	Journey[] journeys;

	public Population (int _populationSize, boolean createNew) {
		journeys = new Journey[_populationSize];

		if (createNew) {
			for (int p = 0; p < _populationSize; p++) {
				Journey newJourney = new Journey();
				newJourney.generateJourney(true);
				saveJourney (p, newJourney);
			}
		}
	}

	/*
	Save the journey specified at the element index specified.
	 */
	public void saveJourney (int journeyIndex, Journey journey) {
		this.journeys[journeyIndex] = journey;
	}

	/*
	Retrieve the journey at the element index specified.
	 */
	public Journey getJourney (int journeyIndex) {
		return this.journeys[journeyIndex];
	}

	/*
	Retrieve the fittest (i.e. shortest) journey in this population
	 */
	public Journey getFittestJourney() {
		// Set the default fittest journey to be the first one in the journeys array.
		Journey fittest = this.journeys[0];
		// Loop through the journeys array to find the fittest one
		for (int f = 1; f < populationSize(); f++) {
			if (fittest.getJourneyFitness() <= getJourney(f).getJourneyFitness()) {
				fittest = getJourney(f);
			}
		}
		return fittest;
	}

	/*
	Retrieve the population size (i.e. number of journeys)
	 */
	public int populationSize() {
		return journeys.length;
	}

}
