package a6;

import java.util.ArrayList;
import java.util.HashMap;

//Mapvalue is a frame
public class MapValue 
{
	
	// a map of variable names to their values in the environment
    HashMap<String, Value> tableV = new HashMap<>();

    // a map of function names to their definitions in the environment
    HashMap<String, Function> tableD = new HashMap<>();
  
    
    /**
    get all variable key from the current environment
    @return arraylist of key,
      or null if none exists
   */  
    
    
    public  ArrayList<String> getAllVarKey()
    {
    	return new ArrayList<String>(tableV.keySet());
    }
    
    /**
    get all variable value from the current environment
    @return an arraylist of value
   */  
    public  ArrayList<Value> getAllVarValue()
    {
    	return new ArrayList<Value>(tableV.values());
    }
    
    
    /**
    get all Function keys from the current environment
    @return arraylist of function
   */  
    public  ArrayList<String> getAllFunctionKey()
    {
    	return new ArrayList<String>(tableD.keySet());
    }
    /**
    get all Functions  from the current environment
    @return arraylist of functions
   */  
    
    public  ArrayList<Function> getAllFunctions()
    {
    	return new ArrayList<Function>(tableD.values());
    }
    
    
    /**
    Adds a variable binding to the current environment
    @param s the variable name
    @param i the value to which variable is to be bound
    @return the old binding for the variable name,
      or null if none exists
   */  
    public Value putVarBinding(String s, Value i)            
    {
    	return tableV.put(s,i);                             
    }
    
    /**
    Gets the value bound to a given variable 
      in the current environment
    @param s the variable whose value is to be found
    @return the value bound to the variable, or null
      if no value is bound to it
     */
    public Value getVarBinding(String s)                       
    {
    	return tableV.get(s);                               
    }
    
    /**
    Adds a function binding to the current environment
    @param s the function name
    @param d the definition to which variable is to be bound
    @return the old binding for the variable name,
      or null if none exists
 */  

    public Function putFunction(String s, Function f)      
    {
    	return tableD.put(s,f);                             
    }
  
  
/**
    Gets the definition bound to a given function name 
      in the current environment
    @param s the name of the function whose definition
      is to be found
    @return the definition bound to the variable, or null
      if no value is bound to it
*/
    public Function getFunction(String s)                    
    {
    	return tableD.get(s);                               
    }
	
	
}









  
  

          

