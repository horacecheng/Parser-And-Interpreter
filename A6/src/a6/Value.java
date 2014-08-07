package a6;
/**
A class that represents possible values of variables
  in a language.  All functionality is relegated
  to subclasses

*/

public class Value extends MapValue
{
	boolean binded;
	public Value()
	{
		binded = false;
	}
	
	public boolean isbinded()
	{
		return binded;
	}
	
}