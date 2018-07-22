package de.uni_hannover.sim.jannisbujak.profiling;
import de.uni_hannover.sim.jannisbujak.simulation.*;
//import de.uni_hannover.sim.jannisbujak.profiling.*;
//import de.uni_hannover.sim.jannisbujak.exceptions.*;

public class Archive {
	private String name;
	private double[] log;
	private Simulation simulation;
	
	/**saves the name, the double[] log and the simulation
	@param name name of the object
	@param log double-Array with object.depending data
	@param simulation is the simulation, the Archive exists in
	@param lifetime the number of runs, the car existed. It is needed to make a new double-Array not filled with zeros so that the mean is not pulled down*/
	public Archive(String name, double[] log, Simulation simulation, int lifetime){
		this.setSimulation(simulation);
		this.name = name;
		this.log = new double[lifetime];
		
		int count = 0;
		for(int i = 0; i < log.length; i++){
			if(log[i] < 0) continue;
			this.log[count] = log[i];
			count++;
		}
	}
	
	
	/**@param name name of the object
	@param log double-Array with object.depending data
	@param simulation is the simulation, the Archive exists in*/
	public Archive(String name, double[] log, Simulation simulation){
		this.setSimulation(simulation);
		this.name = name;
		this.log = log;
	}
	
	/**@return name name of the object*/
	public String getName(){	return name;	}
	
	
	/**prints the log from index 0 to the end*/
	@SuppressWarnings("unused")
	private void printlog(){
		for(int i = 0; i < log.length; i++){
			System.out.println(log[i]);
		}
	}
	
	
	public void printFinalStatus(){
		if(log != null) System.out.println("The object " + name + " has a mean of " + Statistics.calculateMean(log) + " and a standard deviation of " + Statistics.calculateStandardDeviation(log));
		//printlog();
	}


	public Simulation getSimulation() {
		return simulation;
	}


	public void setSimulation(Simulation simulation) {
		this.simulation = simulation;
	}
}