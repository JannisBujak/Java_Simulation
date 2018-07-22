package de.uni_hannover.sim.jannisbujak.model;
import de.uni_hannover.sim.jannisbujak.simulation.*;
//import de.uni_hannover.sim.jannisbujak.model.*;
//import de.uni_hannover.sim.jannisbujak.exceptions.*;

public class Systemedge extends Crossroad{
		
	private double chance;
	private Systemedge[] destinations;
	
	/**Constructor
	saves the simulation, a name and a double chance in the Systemedge
	@param simulation is a simulation
	@param name is the name of the Systemedge
	@param chance is a double aquivalent to the chance, a car will spawn*/
	public Systemedge(Simulation simulation, String name, double chance){
		super(simulation, name);
		this.chance = chance;
	}

	//public Systemedge getDestinationArray(){ 	return destinations;	}
	public void setDestinations(Systemedge[] destinations){
		this.destinations = destinations;
	}
	public void printStatus(){
		//System.out.println(name + " ");
		
	}
	
	/**
	If the Systemedge has not yet acted:
	if a random double betwen 0 and 1 is smaller, than the chhance of the systemedge makeNewCar() is called with a random Systemedge from the destinations Array, 
	*/
	public void update(){
	
        //return if on the field, a car would spawn, there is a car
		if(super.output[0] != null){  if(super.output[0].isCarAtPosition(0))	return;    }
		if(super.output[1] != null){  if(super.output[1].isCarAtPosition(0))	return;  }
		
		if(super.hasAlreadyActed())	return;
		super.setAlreadyActedTrue();
		
		if((super.simulation).getRandom().nextDouble() < chance){
			//spawn new car
			simulation.makeNewCar(this, destinations[(super.simulation).getRandom().nextInt(1)]);	
		}	
	}
}
