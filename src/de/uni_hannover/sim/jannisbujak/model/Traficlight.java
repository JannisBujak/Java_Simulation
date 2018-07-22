package de.uni_hannover.sim.jannisbujak.model;
import de.uni_hannover.sim.jannisbujak.profiling.*;
import de.uni_hannover.sim.jannisbujak.simulation.Simulation;
//import de.uni_hannover.sim.jannisbujak.exceptions.*;

public class Traficlight extends Crossroad implements DataSource{
	
	
	int absoluteCountdown;
	int remainingTime;
	public Road[] greenLightOn;
	public Road[] redLightOn;
	double[] logArray;
	
	//Constructor
	
	
	
	public Traficlight(Simulation simulation, String name, int remainingTime){
		super(simulation, name);
		
		absoluteCountdown = remainingTime;
		this.remainingTime = remainingTime + 1;
	}
	//
	
	public int getAbsoluteCountdown(){	return absoluteCountdown;	}
	public int getRemainingTime(){	return remainingTime;	}
	public Road[] getGreenLightOn(){	return greenLightOn;	}
	
	
	
	private void toggle(){
		Road[] help = redLightOn;
		redLightOn = greenLightOn;
		greenLightOn = help;
	}
	
	
	public void setStatus(Actor actor, int absoluteCountdown, int remainingTime){
		this.absoluteCountdown =absoluteCountdown;
		this.remainingTime = remainingTime;
		
		for(int i = 0; i < greenLightOn.length; i++){
			if(greenLightOn[i] == null) continue;
			
			if(greenLightOn[i].getName() != ((Traficlight)actor).getGreenLightOn()[i].getName()){
				toggle();
				break;
			}else{
				break;
			}	
		}
	}
	
	/**
	gibt einem Auto zurück, ob die Straße, auf dem es sich befindet, aktuell eine Grünphase hat
	@param streetCarIsComingFrom is the road, a car is coming from
	@return if the road, given is input, is in the greenLightOn - Array
	*/
	public boolean greenLightForCarsFromHere(Road streetCarIsComingFrom){
		for(int i = 0; i < greenLightOn.length; i++){
			if(greenLightOn[i] == streetCarIsComingFrom){
				return true;
			}
		}
		return false;
	}
	
	
	public void logStatus(){
		if(logArray == null)	logArray = new double[simulation.getDuration()];
		int count = 0;
		for(int i = 0; i < redLightOn.length; i++){
			if(redLightOn[i] == null) continue;
			count += redLightOn[i].countStandingCars(simulation.inPrintedObjects(this));
		}
		//System.out.println(count);
		logArray[super.simulation.getRuns()] = count;
	}
	
	
	public double[] getLog(){
		return logArray;
	}
	
	
	private void printArray(Road[] roads){
		
		for(int i = 0; i < roads.length; i++){
			if(i != 0) System.out.print(", ");
			System.out.print(roads[i].getName());			
		}
	}
	
	public void printStatus(){
		System.out.print(name + " gives red signal for ");
		printArray(redLightOn);
		System.out.print(" and green signal for ");
		printArray(greenLightOn);
		System.out.print(" for " +  remainingTime + "/" + absoluteCountdown + " seconds yet.\n");
	}
	
	/**
	Updates trafficlight:
	if it has not yet acted, remainingTime is decreased. If the remainingTime is zero, the roads for green and red are swiched
	The update is printed
	*/
	public void update(){
		if(super.alreadyActed)	return;
		super.alreadyActed = true;
		remainingTime--;
		if(remainingTime == 0){
			Road[] help = redLightOn;
			redLightOn = greenLightOn;
			greenLightOn = help;
			
			if(simulation.stringInPrintedObjects(name)){
				System.out.println("Traffic light swiched");
				for(int i = 0; i <  greenLightOn.length; i++){		if(redLightOn[i] != null) System.out.print("	//Green light on: " + greenLightOn[i].getName() + "\n");		}
				for(int i = 0; i <  redLightOn.length; i++){		if(redLightOn[i] != null) System.out.print("	//Red light on: " + redLightOn[i].getName() + "	");			}
				System.out.print("\n---------------------------\n\n");
			}
			
			
			remainingTime = absoluteCountdown;			
		}
		
	}	
}

