package de.uni_hannover.sim.jannisbujak.model;

import de.uni_hannover.sim.jannisbujak.profiling.*;
import de.uni_hannover.sim.jannisbujak.simulation.*;
//import de.uni_hannover.sim.jannisbujak.model.*;
//import de.uni_hannover.sim.jannisbujak.exceptions.*;

public class Car extends Actor implements DataSource {
	//private int spawnTime;
	private Crossroad source;
	private Crossroad destination;
	private Road[] way;
	private int waycount;
	private int position;
	private boolean alreadyMoved;
	private Crossroad[] crossroadWay;
	private int lifetime;
	private boolean toDelete;
	
	private double [] logArray;
	
	//Constructor
	/**@param simulation simulation, the road exists in
	@param name name of the road
	@param source crossroad, the car starts
	@param destination crossroad, the car is heading to*/
	public Car(Simulation simulation, String name, Crossroad source, Crossroad destination){
		super(simulation, name);
		//spawnTime = simulation.getRuns();
		this.source = source;
		this.destination = destination;
		crossroadWay = new Crossroad[3];
		crossroadWay[0] = source;
		crossroadWay[2] = destination;
		way = null;
		waycount = 0;
		position = 0;
		alreadyMoved = false;
	}
	//
	public Crossroad getSource(){	return source;	}
	public Crossroad getDestination(){	return destination;	}
	
	//public int getSpawnTime(){	return spawnTime;	}
	public Road[] getWay(){ return way; }
	public int getLifetime(){	return lifetime;	}
	public boolean shouldDelete(){	return toDelete;	}
	public boolean isMoved(){	return alreadyMoved;	}
	public int getWaycount(){	return waycount;	}
	public int getPosition(){	return position;	}
	
	public void setAlreadyActedTrue(){	alreadyMoved = true;	}
	public void setAlreadyActedFalse(){	alreadyMoved = false;	}
	
	/**saves a 1 or 0 (depending on "alreadyMoved" at the certain position of the log-Array
	optionally declares a new logArray*/
	public void logStatus(){
		
		if(logArray == null){
			logArray = new double[simulation.getDuration()];
			for(int i = 0; i < logArray.length; i++)	logArray[i] = -1;
		}	
		
		if(alreadyMoved){	logArray[super.simulation.getRuns()] = 1;	}
		else{				logArray[super.simulation.getRuns()] = 0;	}
	}
	
	public double[] getLog(){
		return logArray;
	}
	
	
	/**finds the way for a car. Tests all possible combinations with two roads and returns for success*/
	public void findWay(){
		
		Road[] allRoads = simulation.getAllRoads();
		way = new Road[2];
		for(int x = 0; x < allRoads.length; x++){
			for(int y = 0; y < allRoads.length; y++){
				
				if(source == allRoads[x].getSource() && allRoads[x].getDestination() == allRoads[y].getSource() && allRoads[y].getDestination() == destination){
					//System.out.println("Erfolch");
					way[0] = allRoads[x];
					crossroadWay[1] = allRoads[x].getDestination();
					way[1] = allRoads[y];
					return;
				}
					
			}			
		}	
		way = null;
		return;	
	}
	
	/**prints the status of a car*/
	public void printStatus(){
		System.out.print(name);
		if(alreadyMoved && position == 0){
			System.out.println(" moved to the beginning of " + way[waycount].getName() + "	(position 0)");
			return;
		}
		if(alreadyMoved) System.out.print(" moved to ");
		else System.out.print(" stayed at ");
		System.out.println("Position " + position + " on the Road " + way[waycount].getName());
	}
	
	/**Resets alreadyMoved and counts up lifetime.*/
	public void reset(){
		lifetime++;
		setAlreadyActedFalse();
	}
	
	
	/**moves a car to the next road.
	Already moved is set true since the car was ,moved*/
	public void moveToNextRoad(){
		position = 0;
		waycount++;
		alreadyMoved = true;
	}
	
	
	
	
	public Car copy(Simulation simulation, Crossroad source, Crossroad destination){
		
		Car equalCar = new Car(simulation, this.name, source, destination);
		equalCar.waycount = waycount;
		equalCar.position = position;
		equalCar.alreadyMoved = false;
		equalCar.lifetime = lifetime;
		equalCar.logArray = null;
		return equalCar;
		
	}
	
	/**first tests if the car is in the middle or on the edge of a road
	if it is in the middle it is tested, if on the next field there is a car
	else - if the Traficlight - function returns true and if on the first field of the next Road there is no car, the car is moved
	also recognizes, if a car is directly in front of the road*/
	public void update(){
		if(alreadyMoved) return;
		if(position >= (way[waycount].getRoadLength()) - 1){
			
			//Das Car ist am ende der Strecke und wird gelöscht
			if(way[waycount].getDestination() == destination){
				this.leaving();
				if(logArray != null)	simulation.pushIntoTrackedActors(name, logArray, lifetime);
				this.toDelete = true;
				
			}else if(!way[waycount + 1].isCarAtPosition(0) && ((Traficlight)way[waycount].getDestination()).greenLightForCarsFromHere(way[waycount])){
				position = 0; 
				waycount++; 
				setAlreadyActedTrue();	
			}				//Wenn sich auf dem Feld vor dem Auto kein Auto befindet, wird es bewegt
		}else if(!way[waycount].isCarAtPosition(position + 1)){
			position++;
			setAlreadyActedTrue();	
		}
	}
	
	/**prints some important facts about a leaving car*/
	public void leaving(){
		if(super.simulation.inPrintedObjects(this))		System.out.println(name + " comming from " + source.name + " arrived at " + destination.name + " after " + (lifetime + 1) + " move\n");
	}
}
