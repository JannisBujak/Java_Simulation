package de.uni_hannover.sim.jannisbujak.simulation;

import de.uni_hannover.sim.jannisbujak.profiling.*;
import de.uni_hannover.sim.jannisbujak.model.*;
import de.uni_hannover.sim.jannisbujak.exceptions.*;

import java.util.*;

public class Simulation implements Cloneable{	
	
	private Random randomNum;
	private boolean track;
	
	private int duration;
	private int runs = 0;
	
	private int carNameCount;
	private int carCount;
	private int absoluteLifetime;
	
	private int absoluteRoadLength;
	final private int CROSSROAD_LENGTH;
	
	private Road[] allRoads;
	private Actor[] allActors;
	private String[] printedObjects;
	private Archive[] trackedActors;
	
	/**
	@param randomNum is a Random Object, able to return e.g. random ints or floats
	*/
	public Simulation(Random randomNum){
		
		this.randomNum = randomNum;
		
		
		//Initialisierung aller Crossroads und Roads
		carNameCount = 1;
		
		Traficlight C = new Traficlight(this, "C", 4);
		
		Systemedge A = new Systemedge(this, "A", 0.2);
		Systemedge B = new Systemedge(this, "B", 0.3);
		Systemedge D = new Systemedge(this, "D", 0.0);
		Systemedge E = new Systemedge(this, "E", 0.0);
		
		Road AC = new Road("AC", 7, A, C, this);
		Road BC = new Road("BC", 4, B, C, this);
		Road CD = new Road("CD", 5, C, D, this);
		Road CE = new Road("CE", 3, C, E, this);
		
		Systemedge[] dAndE = { D, E };
				
		Road[] greenLightOn = { AC };
		Road[] redLightOn = { BC };
		
		A.setDestinations(dAndE);
		B.setDestinations(dAndE);
		D.setDestinations(new Systemedge[0]);
		E.setDestinations(new Systemedge[0]);
		
		C.greenLightOn = greenLightOn;
		C.redLightOn = redLightOn;
		
		
		A.setOutput(AC, 0);
		B.setOutput(BC, 1);
		C.setOutput(CE, 0);
		C.setOutput(CD, 1);
		C.setInput(AC, 2);
		C.setInput(BC, 3);
		D.setInput(CD, 1);
		E.setInput(CE, 0);
				
		////////////////////////////
		this.allRoads = new Road[]{ AC, BC, CD, CE };
		Crossroad[] allCrossroads = { A, B, C, D, E };
		CROSSROAD_LENGTH = allCrossroads.length;
		
		trackedActors = new Archive[duration * CROSSROAD_LENGTH];
		
		absoluteRoadLength = 0;
		for(int i = 0; i < allRoads.length; i++)	absoluteRoadLength += allRoads[i].getRoadLength();
		
		this.allActors = new Actor[absoluteRoadLength + CROSSROAD_LENGTH];
		
		for(int i = 0; i < allCrossroads.length; i++)	allActors[i] = allCrossroads[i];
		
	}
	//Constructor ends

	
	
	
	public Random getRandom(){	return randomNum;	}
	public int getRuns(){	return runs;	}
	public int getDuration(){	return duration;	}
	public Road[] getAllRoads(){	return allRoads;	}
	public boolean supposedToTrack(){	return track;	}
	public Actor[] getAllActors(){	return allActors;	}
	public int getCrossroadLength(){	return CROSSROAD_LENGTH;	}
	
	/**check, if the name of a actor is included in the printedObjects - Array
	if this equals null, true is always returned
	@param actor the actor, whose name is tested
	@return boolean, the actor is in the Array of printed objects*/
	
	public boolean inPrintedObjects(Actor actor){
		if(printedObjects == null)	return false;
		if(printedObjects.length == 1 && printedObjects[0].equals("All")) return true;
		for(int i = 0; i < printedObjects.length; i++){
			if(printedObjects[i] == null)	continue;
			if(printedObjects[i].equals(actor.getName()))	return true;
		}
		return false;
	}
	
	
	/**check, if a String is included in the printedObjects - Array
	if this equals null, true is always returned
	@param carName, the name of an object
	@return boolean, the actor is in the Array of printed objects*/
	public boolean stringInPrintedObjects(String carName){
		if(printedObjects == null)	return false;
		if(printedObjects.length == 1 && printedObjects[0].equals("All")) return true;
		for(int i = 0; i < printedObjects.length; i++){
			if(printedObjects[i] == null)	continue;
			if(printedObjects[i].equals(carName))	return true;
		}
		return false;
	}
	
	
	
	/**
	delete() sets all cars to null, where the variable toDelete is true
	*/
	public void delete(){
		for(int i = 0; i < allActors.length; i++){
			if(allActors[i] != null && allActors[i] instanceof Car && ((Car)allActors[i]).shouldDelete()){
				
				if(supposedToTrack()){
					carCount++;		//carCount wird nur bei gelöschten Autos hochgezählt, da der Schnitt sonst durch sehr viele nicht angekommene Autos nach unten gezogen wird.
					absoluteLifetime += ((Car)allActors[i]).getLifetime() + 1;
				}
				allActors[i] = null;
			}	
			
		}
	}
	
	
	/**
	Die Crossroad soll wie folgt aussehen:
	///////////////////////////
					|
					0
					|
		-- 1 --	Crossroad -- 3 --
					|
					2
					|		
		////////////////////////////
	*/
	
	/**
	puts a new car into the Actor[], where there is no Space for a car left
	Gives the randomly chosed	source and destination	from the Systemedge
	@param source the origin of a car
	@param destination the Systemedge, the Car is directing
	*/
	public void makeNewCar(Systemedge source, Systemedge destination){
		for(int i = 0; i < allActors.length; i++){
			if(allActors[i] != null) continue;
			String carName = "Car" + carNameCount;
			carNameCount++;
			if(stringInPrintedObjects(carName))	System.out.println(carName + " starts at " + source.getName() + " to " + destination.getName() + " at position 0\n");
			
			allActors[i] = new Car(this, carName, source, destination);
			((Car)allActors[i]).findWay();
			((Car)allActors[i]).getWay()[0].update();
			return;
		}
	}
	
	/**
	updates all roads
	*/
	private void refreshRoads(){
		for(int i = 0; i < allRoads.length; i++)	allRoads[i].update();		
	}
	
	
	/**
	Does "duration" - moves for the whole simulation
	@param allActors is the array of acing objects (Cars and Crossroads)
	@param allRoads is an array with all roads included
	*/
	
	
	
	
	/**fors throug the Archive-array and checks, if it is null, if yes:
	if lifetime is -1 (signalises, its a crossroad), it is constructed without lifetime, otherwise the third input of new Archive() is lifetime
	@param name name of the entry in the Archive
	@param log is the log, saved in the entry
	@param lifetime is the number if turns, a car existed, or -1, if the Actor was a Crossroad*/
	public void pushIntoTrackedActors(String name, double[] log, int lifetime){
		for(int i = 0; i < trackedActors.length; i++){
			if(trackedActors[i] == null){
				if(lifetime == -1){
					trackedActors[i] = new Archive(name, log, this);
					return;
				}
				trackedActors[i] = new Archive(name, log, this, lifetime);
				return;
			}	
		}	
	}
	
	
	
	
	public Crossroad findCrossroadFromArray(Crossroad crossroad, Actor[] allActors){
		String crossroadName = crossroad.getName();
		for(int i = 0; i < allActors.length; i++){
			if(allActors[i] instanceof Systemedge || allActors[i] instanceof Traficlight){
				if(crossroadName.equals(allActors[i].getName()))	return (Crossroad)allActors[i];
			}		
			
		}
		ThrowExceptions.noCrossroadFound();
		return null;
	}
	

	
	@Override
	public Simulation clone(){
		Simulation sim = new Simulation(new Random());
		
		for(int i = 0; i < sim.allActors.length; i++){
			
			if(sim.allActors[i] instanceof Traficlight){
				((Traficlight)sim.allActors[i]).setStatus(allActors[i], ((Traficlight)allActors[i]).getAbsoluteCountdown(), ((Traficlight)allActors[i]).getRemainingTime());
				//sim.allActors[i].printStatus();
			}
			
		}
		
		sim.carNameCount = carNameCount;
		
		for(int i = 0; i < this.allActors.length; i++){
			if(allActors[i] != null && allActors[i] instanceof Car && sim.allActors[i] == null){
				
				Crossroad helpSource = findCrossroadFromArray(((Car)allActors[i]).getSource(), sim.allActors);
				Crossroad helpDestination = findCrossroadFromArray(((Car)allActors[i]).getDestination(), sim.allActors);
				
				if(helpSource == null || helpDestination == null)	ThrowExceptions.noHelpSorDFound();
				
				sim.allActors[i] = ((Car)allActors[i]).copy(sim, helpSource, helpDestination);
				if(!sim.allActors[i].getName().equals(allActors[i].getName()))	ThrowExceptions.differentCarData();
			}
		}
		return sim;
	}
	
	
	/**is the core-function
	
	@param duration	number of runs, the simulation is supposed to do
	@param profileCars is a boolean saying, if the average moves per car is suposed to be print out in the end
	@param printedObjects is a String array with the names of all objects, that are supposed to be printed or "All", if All are supposed to be printed
	@param track is a boolean saying, if the DataSource´s should be tracked*/	
	public void run(int duration, boolean profileCars, String[] printedObjects, boolean track){
		
		if(printedObjects != null)	System.out.println("\n\n\nNew run!");
				
		
		for(int i = 0; i < allActors.length; i++){
			if(allActors[i] != null && allActors[i] instanceof Car)	((Car)allActors[i]).findWay();
		}
		
		carCount = 0;
		absoluteLifetime = 0;
		this.printedObjects = printedObjects;
		this.track = track;
		this.duration = duration;
		
		
		for(runs = 0; runs < this.duration; runs++){
			//run begins
			
			
			
						
			if(printedObjects != null){
				System.out.println("---------------------------\n" + "///Round " + (runs + 1) + "\n");
			}	
			
			//does the following steps for as long, as many fields are in the simulation, so that no car can't miss to move
			for(int circles = 0; circles < absoluteRoadLength; circles++){
				
				
				
				//alternately resets + saves all cars in the road they are	and		updates the actors (crossroads or cars)
				for(int i = 0; i < allActors.length; i++){
					if(allActors[i] != null){
						refreshRoads();
						allActors[i].update();
					}
				}
				delete();
				//
			}
			
			//prints the status of all Actors, sets all needed values back
			for(int i = 0; i < allActors.length; i++){	
				if(allActors[i] != null){
					if(inPrintedObjects(allActors[i]))	allActors[i].printStatus();
					if(allActors[i] instanceof DataSource && supposedToTrack())	((DataSource)allActors[i]).logStatus();
					allActors[i].reset();
				}
				
			}
			if(printedObjects != null)	System.out.println();
			for(int i = 0; i < allRoads.length; i++){
				if(allRoads[i] instanceof DataSource && supposedToTrack())	((DataSource)allRoads[i]).logStatus();
			}
			
			
		}
		
		if(supposedToTrack()){
			/**push the Traficlights into the Archive[]*/
			for(int i = 0; i < allActors.length; i++){
				if(allActors[i] instanceof Traficlight){
					pushIntoTrackedActors(allActors[i].getName(), ((Traficlight)allActors[i]).getLog(), -1);
				}
			}
			for(int i = 0; i < allRoads.length; i++){
				if(allRoads[i] instanceof DataSource){
					pushIntoTrackedActors(allRoads[i].getName(), allRoads[i].getLog(), -1);
				}
			}
		}
		
		
		//print objects, that are supposed to be inspected
		for(int i = 0; i < trackedActors.length; i++){
			if(trackedActors[i] != null && stringInPrintedObjects(trackedActors[i].getName()))	trackedActors[i].printFinalStatus();
		}
		if(profileCars){
			System.out.println("\n///Result:	" + carCount + " Cars that arrived all together needed " + absoluteLifetime + " Rounds" + "\n///	");	
			System.out.println("That is an average of " +  (double)absoluteLifetime/carCount + " Moves		///");
		}	
	}
}
