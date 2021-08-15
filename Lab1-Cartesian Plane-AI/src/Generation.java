import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Generation {
	static ArrayList<Individual> generation;
	static ArrayList<Individual> childGeneration;
	
	
	static int fittest;
	static Individual fittestInd;
	Individual secondFittestInd;
	int worstFitness;
	Individual worstInd;
	int genCounter;
	
	int id;
	int totalGenerationFitness;
	float averageGenerationFitness;
	float standardDeviation;
	
	public Generation(){
		Generation.generation = new ArrayList<>();
		Generation.childGeneration = new ArrayList<>();
		Generation.fittest=0;
		this.genCounter=0;
		this.id=0;
		this.totalGenerationFitness=0;
		this.averageGenerationFitness=0;
		this.standardDeviation=0;
	}
	
	
	void generateRandomGeneration(int polynomialDegree, int size){
		for(int i=0; i<size; i++){
			Individual ind = new Individual(polynomialDegree);
			ind.setGenesRandomly(polynomialDegree);
			ind.setId(i);
			generation.add(ind);
		}
		id=size;
	}
	
	int calculateFitnessGen(){
		fittest=0;
		worstInd=generation.get(0);
		for(Individual ind : generation){
			int fitness = ind.calculateFitness();
			if(fitness>fittest){
				if(fittest!=0){
					secondFittestInd=fittestInd;
				}
				fittest=ind.getFitness();
				fittestInd=ind;		
			}
			if(fitness<worstFitness){
				worstFitness=ind.getFitness();
				worstInd=ind;
			}
		}
		Random rand = new Random();

		genCounter = rand.nextInt(100);
			
		 System.out.println("Generation:"+ genCounter);
			
		System.out.println("Fittest in this generation:");
		int counter = fittestInd.genes.size()-1;

			for(Integer gene : fittestInd.genes){
		    	if(counter>0){
		    		System.out.print("(" +gene + "x^" + counter + ")+");
					counter--;
		    	}
		    	else{
		    		System.out.print("(" + gene + ")\n");
		    	}
			 }	
			counter = fittestInd.genes.size()-1;
			this.selection();
						System.out.println(" Best fitness: " + fittestInd.getFitness());
		System.out.println("Worst in this generation:");
		System.out.println(" Worst fitness: " + worstInd.getFitness());
		System.out.println("Average fitness in this generation: " +averageGenerationFitness );
		System.out.println("Standard deviation: " + standardDeviation);
		System.out.println();
		averageGenerationFitness=0;
		totalGenerationFitness=0;
		standardDeviation=0;
		genCounter++;
		return fittest;
	}

	public void selection() {
		ArrayList<Individual> roulette = new ArrayList<>();  // creating an array named roulette
		roulette.clear();
		for(Individual ind : generation){  //in the generation
			
			for(int i=0; i<ind.getFitness(); i++){
				roulette.add(ind); //adding the fitness values in the array
			    	
			}
			totalGenerationFitness=totalGenerationFitness+ind.getFitness();
			averageGenerationFitness=totalGenerationFitness/(generation.size()+1);
			standardDeviation=fittest-averageGenerationFitness;
		}
		
		int rouletteSize = roulette.size();
		Random rand = new Random(); 		 
		 
		for(int i=0; i<generation.size(); i++){
			int pickedNumber = rand.nextInt(rouletteSize);
			childGeneration.add(roulette.get(pickedNumber));
		}	
		childGeneration.add(fittestInd);
		childGeneration.add(secondFittestInd);
	
		generation.clear();  //clear the element chosen
	}
	



	public void crossover(){
		for(int i=0; i<childGeneration.size(); i++){
			
			Random rand = new Random(); 
			int index = rand.nextInt(childGeneration.size());
			Individual ind = childGeneration.get(index);
			
			Random rand2 = new Random(); 
			int index2 = rand2.nextInt(childGeneration.size());
			Individual ind2 = childGeneration.get(index2);
			
			while(ind==ind2){
				index = rand.nextInt(childGeneration.size());
				ind = childGeneration.get(index);
				
				index2 = rand2.nextInt(childGeneration.size());
				ind2 = childGeneration.get(index2);
			}
			
			
			Individual indNew = new Individual(ind.genotype.size());
			id++;
			indNew.setId(id);
			
		    //Select a random crossover point
			Random rand3 = new Random(); 
		    int crossOverPoint = rand3.nextInt(childGeneration.get(1).chromosomeLength-1)+1;
		    
		    for(int k=0; k<ind.genotype.size(); k++){   
	    		
		    	int[] first = ind.genotype.get(k);
	    		int[] second = ind2.genotype.get(k);
	    		int[] child = new int[childGeneration.get(1).chromosomeLength];//child array

		    	for (int j = 0; j < crossOverPoint; j++) {
			    	child[j] = first[j];
			    }
		    	for (int j = crossOverPoint; j < childGeneration.get(1).chromosomeLength; j++) {
			    	child[j] = second[j];
			    }
		    	
		    	indNew.genotype.add(k, child);
	    	}
		    indNew.calculateFitness();
		    generation.add(indNew);
		}
		childGeneration.clear();
	}

	public void mutation(int mutationRate) {
		for(int i=0; i<mutationRate; i++){
			Random rand = new Random(); 
			int index = rand.nextInt(generation.size());
			Individual ind = generation.get(index);  //ind array

			Random r1 = new Random();
			int r11 = r1.nextInt(ind.genotype.size());
			int[] gene = ind.genotype.get(r11);
			
			Random r2 = new Random();
			int r12 = r2.nextInt(gene.length);
			ind.genotype.get(r11)[r12] ^= 1;
			
		}		
	}
}
