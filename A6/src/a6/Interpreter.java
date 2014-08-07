package a6;

import java.util.ArrayList;
import java.util.LinkedList;


/**
     This class provides an interpreter
     for part of the language whose grammar has productions
       Expr -> Literal | Var | FCall | LetExpr | IfExpr
       Literal -> ListLiteral | SymbolLiteral
       ListLiteral -> [ {Literal} ]
       FCall -> FName ( {Expr} )
       FName -> UserFName | PrimFName
       LetExpr -> let {Def} Expr
       Def -> define Sig Expr
       Sig -> UserFName ( {Var} )
       IfExpr -> if Expr Expr Expr
       
    The start symbol is Expr.
    
    In these productions, the parentheses and brackets are terminal symbols; 
      the braces and vertical bars are metasymbols. 
    Also, the following are preterminals:
      Var:  a string that begins with a capital letter
      SymbolLiteral:  a string that begins with the backquote character `
      PrimFName:  one of the strings car, cdr, or cons
      UserFunctionName: a string that begins with a lower-case letter, 
        is not a PrimFName, and does not appear on the right-hand side of any rule. 
     
    The portion of the language not covered is the portion that
      involves user-defined functions (with arguments) and variables.  
      Since no user-defined functions are allowed, no scoping issues are raised,
        except that the scope of names introduced by let expressions extends
        to the end of the entire program 
        
     THE NEW LIST IS WORK NOW

                   
    @author Horace Cheng
 */

public class Interpreter
{
    // a parser -- the interpreter works from the syntax tree
    //   that this parser produces
    private Parser parser = new Parser();

    
    // the current environment (in which to evaluate)
    private Environment env;
    

    /**
     * Constructor for objects of class Interpreter
     */
    public Interpreter()
    {
       
    }

     /**
        Interprets a string in the given grammar 
        @param the input string
        @return the value of the program represented by the string
        @throws IllegalArgumentException if a syntactic or semantic
          error is found
     */
    
  public Value interpret(String input)                          
  {
      OrderedTree<Token> syntaxTree = parser.parse(input);
      env = new Environment();
      MapValue mapvalue = new MapValue();
      env.addMapValue(mapvalue);
      
      return interpretExpr(syntaxTree);                      
  }

          
     /**
        Interprets as an Expr the substring of
          the input string that starts with its
          first unconsumed symbol 
        @return the value of the substring
          of category Expr that starts at the
          first unconsumed symbol
        @throws IllegalArgumentException if a syntactic 
          or semantic error is found
     */
    
    public Value interpretExpr(OrderedTree<Token> syntaxTree)      {
      Token root = syntaxTree.getRootData();  

      if (root.getType().equals("SymbolLiteral"))         
        return new Symbol(root.getSpelling());
      if (root.getType().equals("List"))                         {
      
    	  return new Lyst(syntaxTree);                                 }  
      else if (root.getType().equals("UserFName"))
        return interpretUserFCall(syntaxTree);                          
      else if (root.getType().equals("PrimFName"))       
        return interpretFCall(syntaxTree);                            
      else if (root.getType().equals("LetExpr"))      
        return interpretLetExpr(syntaxTree);                                      
      else if (root.getType().equals("IfExpr"))      
        return interpretIfExpr(syntaxTree);    
        
      // the Var case
      else
      {
    	  Value value = env.getVarBinding(root.getSpelling());
    	  if( value == null|| !value.binded)
    	  {
    		  throw new IllegalArgumentException(
    			        "illegal expression type: " + root.getType() + " " +root.getSpelling());
    	  }
    	  else
    	  {
    		  return value;
    	  }
      }

    	  
}


        /**
        Interprets as a primitive function call 
         the substring of the input string that starts with its
          first unconsumed symbol 
        @return the value of the substring
          of category FCall that starts at the
          first unconsumed symbol (assuming that the
          function being called is primitive).  If
          an undefined function is called (that is, 
          if the caller hasn't checked for a legal
          primitive function, null is returned.          
        @throws IllegalArgumentException if a syntactic 
          or semantic error is found
     */

    private Value interpretFCall(OrderedTree<Token> syntaxTree)    {
      Token root = syntaxTree.getRootData();  
      if (root.getSpelling().equals("car"))                      {
        if (syntaxTree.getNumberOfChildren() != 1)
          throw new IllegalArgumentException(
            "the function car requires exactly 1 argument");
        Value arg =  interpretExpr(syntaxTree.getKthChild(1));
        if (!(arg instanceof Lyst))
          throw new IllegalArgumentException("the function car"
           + " takes a Lyst as its argument");
        return ((Lyst) arg).car();                               }
      if (root.getSpelling().equals("cdr"))                      {
        if (syntaxTree.getNumberOfChildren() != 1)
          throw new IllegalArgumentException(
            "the function cdr requires exactly 1 argument");
        Value arg =  interpretExpr(syntaxTree.getKthChild(1));
        if (!(arg instanceof Lyst))
          throw new IllegalArgumentException("the function cdr"
           + " takes a Lyst as its argument");
        return ((Lyst) arg).cdr();                               }
      if (root.getSpelling().equals("cons"))                     {
        if (syntaxTree.getNumberOfChildren() != 2)
          throw new IllegalArgumentException(
            "the function cons requires exactly 2 arguments");
        Value arg1 = interpretExpr(syntaxTree.getKthChild(1));

      
        Value arg2 = interpretExpr(syntaxTree.getKthChild(2)); 

        if (!(arg1 instanceof Symbol) || !(arg2 instanceof Lyst))
          throw new IllegalArgumentException("the function cons "
           + "takes a Symbol and a Lyst as its arguments");
        Lyst output = (Lyst) arg2;
        return output.cons((Symbol) arg1);                       }
      return null;  /* shouldn't get here */                        }

      
     /**
        Interprets as a user-defined function call 
         the substring of the input string that starts with its
          first unconsumed symbol, provided that this function
          has no arguments (and is therefore equivalent to a 
          local variable)
        @return the value of the substring
          of category FCall that starts at the
          first unconsumed symbol, provided that this function
          has no arguments (and is therefore equivalent to a 
          local variable) 
        @throws IllegalArgumentException if a syntactic 
          or semantic error is found
     */

    private Value interpretUserFCall(OrderedTree<Token> fcalltree) 
    {
    	Function f = env.getFunction(fcalltree.getRootData().getSpelling());
    	int size = fcalltree.getNumberOfChildren();	//at this point we still dont know that
    	LinkedList<Value> args = new LinkedList<Value>();//we have legal number of arguments
		//we will check when we apply the function
    	if(f== null &size==0)
    	{
    		Value value = env.getVarBinding(fcalltree.getRootData().getSpelling());
    		if(value == null)
    		{
    			throw new IllegalArgumentException(
        				"variable not found: " 
        						+ fcalltree.getRootData().getSpelling()); 
    		}
    		if(fcalltree.getNumberOfChildren()!=0)
    		{
    			throw new IllegalArgumentException(
    					fcalltree.getRootData().getSpelling()
    					+"does not support arguments"); 
    		}
    		
    		
    		return value;
    	}
    	if(f == null) //here size must be greater than 0
    	{
    		throw new IllegalArgumentException(
        				"function not found: " 
        						+ fcalltree.getRootData().getSpelling()); 
    		
    	}
    	for(int i = 1 ; i<= size ; i++)				//interpret {Expr}
    	{
    		args.add(interpretExpr(fcalltree.getKthChild(i)));
    	
    	}
    	
    	OrderedTree<Token> expr = f.getExprFromDef(args);
    	OrderedTree<Token> sig = f.getOriginalSig(args);
    	int rightargsize = f.getRightArgumentSize();
  	  	// bind the parameters to the argument values
  	  	for(int i = 1 ; i<= rightargsize ; i++)	//({Var})
  	  	{
  	  		//def's first child is function name(sig). function name's children are vars.
  	  		String var = sig.getKthChild(i).getRootData().getSpelling();
  	  	
  	  		 env.putVarBinding(var, args.get(i-1));

  	  	}
  	  	//after binding, we can run the expr
  	  	
  	  	
  	  	Value value = interpretExpr(expr);
  	  	return value;
       
    }    
      
      
     /**
        Interprets as an IfExpr the substring of
          the input string that starts with its
          first unconsumed symbol.
        @return the value of the substring
          of category IfExpr that starts at the
          first unconsumed symbol.  More precisely,
          this method returns the value of the expression's
          Expr component if the first Expr component 
          evaluates to an empty list, and the value of the
          third one otherwise.
        @throws IllegalArgumentException if a syntactic 
          or semantic error is found
     */
    
    private Value interpretIfExpr(OrderedTree<Token> syntaxTree)    
    {
    	Value value1 = interpretExpr(syntaxTree.getKthChild(1));
    	if (value1 instanceof Symbol)
    	{
    		return interpretExpr(syntaxTree.getKthChild(2));
    	}
    	if (value1 instanceof Lyst && ((Lyst) value1).isNull())
    	{
    		return interpretExpr(syntaxTree.getKthChild(3)); 
    	}
    	else
    	{
    		return interpretExpr(syntaxTree.getKthChild(2));  
    	}
                 
     }

          
     /**
        Interprets as a LetExpr the substring of
          the input string that starts with its
          first unconsumed symbol.
        @return the value of the substring
          of category LetExpr that starts at the
          first unconsumed symbol.  More precisely,
          this method returns the value of the expression's
          Expr component in the current environment, as
          augmented by the definitions of the Def components.
        @throws IllegalArgumentException if a syntactic 
          or semantic error is found
     */
    
    //here is the parse that we deal with dynamic scoping
    private Value interpretLetExpr(OrderedTree<Token> syntaxTree)   
    {
 
  
    	env.addMapValue(env.copyMapValue());
   
    	for (int k = 1; k<syntaxTree.getNumberOfChildren(); k++)
        
    		interpretDef(syntaxTree.getKthChild(k));
      
    	
    	Value value = interpretExpr(syntaxTree.getKthChild(
    			syntaxTree.getNumberOfChildren()));
    	
    	env.deletePeakMapValue();
    	return value;                          
     }
 
            
      
   /**      
        Interprets a Def expression by binding the Sig component's
          UserFName component to the Def's Expr component.  That is,
          it assumes that the function being defined has no arguments,
          and treats the UserFName component as a local variable
        @throws IllegalArgumentException if the "begin" token
          or a corresponding "end" token is not found is found, or
          if the function being defined has one or more arguments.
   */ 
     
    private void interpretDef(OrderedTree<Token> syntaxTree)        
    {

    	OrderedTree<Token> sig = syntaxTree.getKthChild(1);    
    	String fname = sig.getRootData().getSpelling();
    	
    	if(sig.getNumberOfChildren() ==0)
    	{
    		env.putVarBinding(fname, interpretExpr(syntaxTree.getKthChild(2)));
    		return;
    	}
    	
    	
    	for(int i = 1 ; i<= sig.getNumberOfChildren() ;i++)
    	{
    		String var = sig.getKthChild(i).getRootData().getSpelling();
    		if(env.containVar(var))
    		{
    			throw new IllegalArgumentException(
    			          "repeated parameter " + var);
    		}
    		else
    		{
    			env.putVarBinding(var, new Value());//reserve a space for the var that distingushed by boolean binded
    		}
    	}


    	Function function = new Function(syntaxTree);
    	env.putFunction(fname, function);
     }
    
    //Environment contains bunch of MapValue
    private class Environment                                    
    {
        
      LinkedList<MapValue> stack;
      
      private Environment()
      {
    	  stack = new LinkedList<MapValue>();
      }
      
      /**
      Add a MapValue to top of stack
      @param MapValue
      @return no return value
       */
      private void addMapValue(MapValue mapvalue)
      {
    	  stack.add(mapvalue);
      }
      
      /**
      delete the stack's peak MapValue
      @return no return value
       */
      private void deletePeakMapValue()
      {
    	  stack.removeLast();
      }
      
      
      /**
      Copy the stack peak's MapValue
       from the current environment

      @return a copy of the stack peak's MapValue
       */
      private MapValue copyMapValue()
      { 
    	  if(stack.isEmpty())
    	  {
    		  return null;
    	  }
    	  
    	  MapValue newmapvalue = new MapValue();
    	  
    	 
		  ArrayList<String>  varkeys = stack.peekLast().getAllVarKey();
		  ArrayList<Value> values = stack.peekLast().getAllVarValue();
		  
		  ArrayList<String> functionkeys = stack.peekLast().getAllFunctionKey();
		  ArrayList<Function> functions = stack.peekLast().getAllFunctions(); 
    	  
		  for(int k = 0; k< varkeys.size() ; k++)
		  {
			  newmapvalue.putVarBinding(varkeys.get(k),values.get(k));
		  }
		  for(int k = 0; k< functionkeys.size() ; k++)
		  {
			  newmapvalue.putFunction(functionkeys.get(k),functions.get(k));
		  }
		  
		  return newmapvalue;
    	  
    	  
    	  
      }
      
      /**
     put the function with a function name and function 
       to the current environment
      @param a function name and function
      @return the function bound to the function name, or null
        if no function is bound to it
       */
      
      private Function putFunction(String functionname, Function function)
      {
    	  
    	  MapValue peak = stack.peekLast();
    	  peak.putFunction(functionname,  function);
    	  
    	  
    	  for(int i = stack.size() -1 ; i>-1 ; i--)
    	  {
    		  MapValue tmp = stack.get(i);
    		  Function tmpfunction = tmp.getFunction(functionname);
    		  if(tmpfunction == null)
    		  {
    			  continue;	//then find next level of stacks
    		  }
    		 tmp.putFunction(functionname, function);	//allow function names to be defined more than once in the same scope, 
    		 											//as long as the newer definition overwrites the older one.
    	  }
    	  return function;
    	  
      }
      /**
      Gets the function by a funtion name
       from the current environment
      @param a function name
      @return the function bound to the function name, or null
        if no function is bound to it
       */
    
      private Function getFunction(String functionname)
      {
    	   
    	 
    		  MapValue peak = stack.peekLast();
    		  Function function = peak.getFunction(functionname);
  
    		  return function;

	  }
	  

      
      
      /**
      Gets the value bound to a given variable 
        in the current environment
      @param s the variable whose value is to be found
      @return the value bound to the variable, or null
        if no value is bound to it
       */
      private Value getVarBinding(String var)
      {
  
    		  MapValue peak = stack.peekLast();
    		  Value value = peak.getVarBinding(var);
    		 
    		  return value;
      }
      
      /**
      put the value bound to a given variable 
        in the current environment
      @param s the variable name and the bounded value 
      @return the value bound to the variable, or null
        if unsuccessfully put variable
       */
      private Value putVarBinding(String var, Value value)
      {
    	  MapValue peak = stack.peekLast();
    	  value = peak.putVarBinding(var, value);

    	  return value;
      }
      
      /**
     check the environment contain the var or not for finding duplicate parameter
      @param s the variable whose value is to be found
      @return a boolean value
       */
      
      private boolean containVar(String var)
      {
    	  for(int i = stack.size() -1 ; i>-1 ; i--)
    	  {
    		  MapValue peak = stack.get(i);
    		  Value value = peak.getVarBinding(var);
    		  if(value == null)
    		  {
    			  continue;	//then find next level of stacks
    		  }
    		  return true;
    	  }
    	  
    	  return false;		// undefined variable 
      }
      

    }
      


}