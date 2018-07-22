package de.uni_hannover.sim.jannisbujak.model;

import de.uni_hannover.sim.jannisbujak.profiling.*;
import de.uni_hannover.sim.jannisbujak.simulation.*;
//import de.uni_hannover.sim.jannisbujak.model.*;
//import de.uni_hannover.sim.jannisbujak.exceptions.*;

public class Road implements DataSource{
	private String name;
	private int length;
	private Crossroad from;
	private Crossroad to;
	private Actor[] carHere;
	private final Simulation simulation;
	
	double [] logArray;
	
	//Constructor
	/**Makes a road with the name name, length length and saves a Crossroad as Destination and one as source
	furthermore saves the simulation in the new road
	@param name name of the road
	@param length the number of fields a road has
	@param source crossroad, the road is coming from
	@param destination crossroad, the road is leading to
	@param simulation is the simulation, the road exists in*/
	public Road(String name, int length, Crossroad source, Crossroad destination, Simulation simulation){
		this.simulation = simulation;
		
		this.name = name;
		this.length = length;
		from = source;
		to = destination;
		carHere = new Actor[length];
		for(int i = 0; i < length; i++){
			carHere[i] = null;
		}
	}
	//
	
	//getter
	public String getName(){	return name;	}
	public int getRoadLength(){	return length;	}
	public Crossroad getSource(){	return from;	}
	public Crossroad getDestination(){	return to;	}
	public boolean isCarAtPosition(int test){
		return carHere[test] != null;
	}
	
	/**
	Goes through the Car-Array starting at the end
	It returns the counter, if on a field there is no car, otherwise, counts
	@param printIfFull delivers the information, if this simulation is supposed to print information from this function( e.g. road is full)
	@return number of standing cars in a row at the end of road*/
	public int countStandingCars(boolean printIfFull){
		update();
		int count = 0;
		for(int i = (length - 1); i >= 0; i--){
			if(carHere[i] == null) return count;
			count++;
		}
		if(printIfFull)	System.out.println("The road " + name + " is completely filled!");
		return count;
	}
	
	
	/**first of updates the road, so that carHere is filled.
	Then optionally makes a new double array and goes through all Roads with red light on.
	On the certain road countStandingCars is used
	The counter is saved in the logArray*/
	
	public void logStatus(){
		update();
		if(logArray == null)	logArray = new double[simulation.getDuration()];
		int count = 0;
		for(int i = 0; i < length; i++){
			if(carHere != null)	count++;
		}
		logArray[simulation.getRuns()] = count;
	}
	
	
	public double[] getLog(){
		return logArray;
	}
	
	
	public void printStatus() {
        System.out.println(name);
        for(int i = 0; i < length; i++){
            if(carHere[i] != null)  System.out.println(carHere[i].getName() + "      " + ((Car)carHere[i]).getWay()[((Car)carHere[i]).getWaycount()].name + " at position" + ((Car)carHere[i]).getPosition());
        }
	}
	
	/**
	Makes a new array with all cars in it.
	Goes through all Cars and tests, if it is on the certain road
		saves the i´th car carHere at position	
	*/
	public void update(){
		
		for(int i = 0; i < length; i++)	carHere[i] = null;
		
		Actor[] allActors = simulation.getAllActors();
		Car[] allCars = new Car[allActors.length - simulation.getCrossroadLength()];
		
		int count = 0;
		for(int i = 0; i < allActors.length; i++){
			if(!(allActors[i] instanceof Car)) continue;
			allCars[count] = (Car)allActors[i];
			count++;
		}
		
		for(int i = 0; i < allCars.length; i++){	
			if(allCars[i] == null)	return;
			if(allCars[i].getWay()[allCars[i].getWaycount()] == this){
				carHere[allCars[i].getPosition()] = allCars[i];
			}            
		}
	}
}
