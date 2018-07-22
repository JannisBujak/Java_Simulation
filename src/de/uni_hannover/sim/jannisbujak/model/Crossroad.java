package de.uni_hannover.sim.jannisbujak.model;

import de.uni_hannover.sim.jannisbujak.simulation.*;
//import de.uni_hannover.sim.jannisbujak.model.*;
//import de.uni_hannover.sim.jannisbujak.exceptions.*;

public abstract class Crossroad extends Actor{
	final protected int MAX_IN_AND_OUTPUT = 4;
	protected Road[] input;
	protected Road[] output;
	protected boolean alreadyActed;
	
	public boolean hasAlreadyActed(){	return alreadyActed;	}
	public void setAlreadyActedTrue(){	alreadyActed = true;	}
	public void setAlreadyActedFalse(){	alreadyActed = false;	}
	public String getName(){	return super.name;	}
	//Constructor
	public Crossroad(Simulation simulation, String name){
		
		super(simulation, name);
		
		input = new Road[MAX_IN_AND_OUTPUT];
		output = new Road[MAX_IN_AND_OUTPUT];
	}
	
	public void setInput(Road input, int position){	
		this.input[position] = input;
	}
	public void setOutput(Road output, int position){
		this.output[position] = output;
	}
	
	
	//Empty function, so that no casting is needed
	public void printStatus(){
		//System.out.println(name);
	}
	
	
	
	/**sets alreadyActed back to false*/
	public void reset(){
		setAlreadyActedFalse();
	}
	
	//public void update(){}
	
	
	/*				|
					3
					|
	---> 0 -->	Crossroad <--- 2 <---
					|
					1
					|
	*/
	
}
