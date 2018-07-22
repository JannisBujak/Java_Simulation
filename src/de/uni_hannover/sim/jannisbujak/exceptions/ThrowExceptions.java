package de.uni_hannover.sim.jannisbujak.exceptions;

public class ThrowExceptions{
	
	/**throws exception calling "no crossroad found"*/
	public static void noCrossroadFound() throws ArithmeticException{
		throw new ArithmeticException("\nNo Crossroad found!\n");
	}
	/**throws exception if a crossroad is supposed to be the source or destination*/
	public static void noHelpSorDFound() throws ArithmeticException{
		throw new ArithmeticException("\nNo help Source or Destination found! \n");
	}
	/**throws exception if a a copied car is not equal to the original*/
	public static void differentCarData() throws ArithmeticException{
		throw new ArithmeticException("\nThe data of the copied Car are different from the Data of the old one\n");
	}
	/**throws exception if a command is unknown. Its name is also printed and given as input command
	@param command name of the command called before*/
	public static void unknownCommand(String command) throws ArithmeticException{
		throw new ArithmeticException("\n\nThe command '" + command + "' is an unknown!\nType in '-help' to see all commands\n");
	}
	
	/**throws exception if a command is called without input
	@param command name of the command called before*/
	public static void StringNeedsANumber(String command)throws ArithmeticException{
		throw new ArithmeticException("\n\n'" + command + "' needs input beeing a number\n");
	}
	
	/**throws exception if the input for a command is too small
	@param command name of the command called before*/
	public static void StringInputTooSmall(String command) throws ArithmeticException{
		throw new ArithmeticException("\n\nInput for '" + command + "' is too small\n");
	}
	
	/**throws exception if the input for a command is not convertible in this case to an integer
	@param command name of the command called before*/
	public static void stringNotConvertible(String command) throws ArithmeticException {
		throw new ArithmeticException("\n\nInput for " + command + " not convertible! \nA number is needed!\n");
	}
	
}