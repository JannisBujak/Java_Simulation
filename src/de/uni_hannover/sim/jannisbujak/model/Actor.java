package de.uni_hannover.sim.jannisbujak.model;
import de.uni_hannover.sim.jannisbujak.simulation.*;
//import de.uni_hannover.sim.jannisbujak.model.*;

public abstract class Actor {
  protected final Simulation simulation;
  protected final String name;

  public Actor(Simulation simulation, String name) {
    this.simulation = simulation;
    this.name = name;
  }
  
  public String getName(){
	  return name;
  }
	public abstract void printStatus();
	public abstract void update();
	public abstract void reset();
}
