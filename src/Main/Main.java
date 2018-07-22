package Main ;
import de.uni_hannover.sim.jannisbujak.simulation.Simulation;
import de.uni_hannover.sim.jannisbujak.exceptions.*;
//import de.uni_hannover.sim.jannisbujak.model.*;

import java.util.*;

public class Main{
	
	private Random randomNum = new Random();
	private String[] args;
	private int duration = 30;
	private String[] printedObjects;
	private boolean profileCars;
	private int startOfTracking;
	private int repeat;
	private String[] commands = new String[]{ "-debug", "-help", "-seed", "-duration", "-inspect", "-profile-cars", "-setting-time", "-repeat" };
	private boolean debug;
	
	/**@param args is the optional user imput including commands*/
	public Main(String[] args){
		repeat = 1;
		this.args = args;
		randomNum = new Random();
		printedObjects = null;
	}
	
	/**@param command is the command taken out of args
	is is tested, if the given input is included in the commands-array*/
	private boolean isCommand(String command){
		for(int i = 0; i < commands.length; i++){
			if(commands[i].equals(command))	return true;
		} 
		return false;
	}
	
	/**checks, if the run() - function should continue and reacts to the commands -help, -seed, -duration, inspect, profile-cars if it is typed in*/
	private boolean illegalInput(){
		//if(args.length == 0) return false;
		
		if(args.length != 0)	if(args[0].equals("-debug"))	debug = true;
		for(int i = 0; i < args.length; i++){
			if(args[i].equals("-help")){
				System.out.println("\n\n'-help' <number>\n	gives information about the useable commands");
				System.out.println("\n\n'-seed' <number>\n	changes seed");
				System.out.println("\n\n'-duration' <number>	[>0]\n	change duration from standard to <number>");
				System.out.println("\n\n'-inspect' <actor>,<actor>,...,<actor>	or '-inspect All'\n	Gives the status of the inserted Actors or of all Actors, if 'All' is inserted");
				System.out.println("\n\n'-profile-cars'\n	gives absolute number of moves of all Cars");
				System.out.println("\n\n'-setting-time' <number>	[>= 0]\n	changes the number of the Round tracking of the Actors is started");
				System.out.println("\n\n'-repeat' <number>	[>0]\n	changes the number of repeats of the simulation");
				System.out.println("\n\n'-debug'\n	gives a feetback on your input");
				
				return true;
			}
			if(args[i].equals("-seed")){
				
				i++;
				if(args.length <= i){	ThrowExceptions.StringNeedsANumber("-seed");	}
				if(!args[i].matches("[0-9]+")){	ThrowExceptions.stringNotConvertible("-seed");	}
				
				randomNum.setSeed(Integer.parseInt(args[i]));
				i++;
				continue;
			}			
			if(args[i].equals("-duration")){
				
				i++;
				if(args.length <= i){	ThrowExceptions.StringNeedsANumber("-duration");	}
				if(!args[i].matches("[0-9]+")){	ThrowExceptions.stringNotConvertible("-duration");	}
				
				
				duration = Integer.parseInt(args[i]);
				if(duration < 1){
					ThrowExceptions.StringInputTooSmall("-duration");
				}				
				if(debug)	System.out.println("Doing " + duration + " runs");
				
				continue;
			}
			if(args[i].equals("-inspect")){
				i++;
				if(args.length == i) printedObjects = new String[0];
				else if(args.length >= i){
					if(args[i].equals("All")) printedObjects = new String[]{"All"};
					printedObjects = args[i].split(",");
				}
				continue;
			}
			if(args[i].equals("-profile-cars")){
				profileCars = !profileCars;
				continue;
			}
			if(args[i].equals("-setting-time")){
				i++;
				
				if(args.length <= i){	ThrowExceptions.StringNeedsANumber("-setting-time");	}
				if(!args[i].matches("[0-9]+")){	ThrowExceptions.stringNotConvertible("-setting-time");	}
				
				startOfTracking = Integer.parseInt(args[i]);		
				
				if(startOfTracking < 0){	ThrowExceptions.StringInputTooSmall("-setting-time");	}
				
				if(debug)	System.out.println("Start tracking at " + Integer.parseInt(args[i + 1]) + " runs");
				
				i++;
				continue;
			}
			if(args[i].equals("-repeat")){
				
				i++;
				if(args.length <= i){	ThrowExceptions.StringNeedsANumber("-repeat");	}
				if(!args[i].matches("[0-9]+")){	ThrowExceptions.stringNotConvertible("-repeat");	}
				
				repeat = Integer.parseInt(args[i]);
				
				if(repeat < 1)	ThrowExceptions.StringInputTooSmall("-repeat");
				
				continue;
			}
			if(!isCommand(args[i])){
				ThrowExceptions.unknownCommand(args[i]);
			}
		}
		return false;
	}
	
	
	
	public static void main(String[] args){
		if(args.length == 0)	System.out.println("\nMaybe you need some information? Type in '-help' to see all commands!");
		
		Main mainFun = new Main(args);
		if(mainFun.illegalInput()) return;
		
		Simulation simulation = new Simulation(mainFun.randomNum);
		simulation.run(mainFun.startOfTracking, false, null, false);
			
		for(int i = 0; i < mainFun.repeat - 1; i++){
			Simulation simulationClone = simulation.clone();
			simulationClone.run(mainFun.duration, mainFun.profileCars, mainFun.printedObjects, true);
		}
		simulation.run(mainFun.duration, mainFun.profileCars, mainFun.printedObjects, true);
		
		
	}
}