package a6;

import java.util.LinkedList;

//a class for list that list members may be lists as well as symbols

public class Lyst extends Value
{
	private OrderedTree<Token> parent;
	
	public Lyst(OrderedTree<Token> parent)
	{
		this.parent = parent;
		this.binded = true;
		
		
	}
	
	 /**
    Determines whether the list of symbols is empty
    @return <code>true</code> if the list is empty, and
     <code>false</code> otherwise
*/
    public boolean isNull()                   
    {
      if(parent.getNumberOfChildren() == 0)
      {
    	  return true;
      }
      else
      {
    	  return false;
      }
               
    }
	
    /**
    Adds a symbol to the beginning of the list
    @return <code>true</code> if the list is empty, and
     <code>false</code> otherwise
*/

    public Lyst cons(Value value)                
    {
    	
    	LinkedList<OrderedTree<Token>> children = new LinkedList<OrderedTree<Token>>();
    	
    	for(int i = 1 ; i<= parent.getNumberOfChildren() ; i++)
    	{
    		OrderedTree<Token> child = parent.getKthChild(i);
    		children.add(child);
    	}
    	
    	if(value instanceof Symbol)
    	{
    		Symbol symbol = (Symbol) value;
    		
    		OrderedTree<Token> symbolnode = new OrderedTree<Token>(new Token("SymbolLiteral",symbol.toString()));
    		children.add(0, symbolnode);
    	
    	}
    	else
    	{
    		Lyst list = (Lyst) value;
    		OrderedTree<Token> tmpparent = list.getParent();
    		children.add(0,tmpparent);

    	}
    	
    	OrderedTree<Token> list2 = new OrderedTree<Token>(parent.getRootData(), children);
    

    	return new Lyst(list2);                 
    	
    }
    
    
    
    /**
    Gets the first symbol of the list
    @return the first symbol of the list
*/

    public Value car()                       
    {
    	if (parent.getNumberOfChildren()==0)
    		throw new IllegalArgumentException(
    				"cannot take the car of an empty list");
    	OrderedTree<Token> element = parent.getKthChild(1);
    	Interpreter in = new Interpreter();
    	Value value = in.interpretExpr(element);
    	return value;     
    }  
    
    
    /**
    Returns the list with its first Value(Symbol or list) removed
    @return a copy of the list with its first symbol removed
*/
 
    public Lyst cdr()                        
    {
    	if (parent.getNumberOfChildren()==0)
    		throw new IllegalArgumentException(
    				"cannot take the cdr of an empty list");
    	
    	
    	LinkedList<OrderedTree<Token>> children = new LinkedList<OrderedTree<Token>>();
    	
    	for(int i = 2 ; i<= parent.getNumberOfChildren() ; i++)
    	{
    		OrderedTree<Token> child = parent.getKthChild(i);
    		children.add(child);
    	}
    	
    	OrderedTree<Token> list2 = new OrderedTree<Token>(parent.getRootData(), children);
    	
    
    
    	return new Lyst(list2);              
    }
 
    /**
    get the parent orderedtree
    @return the parent orderedtree
*/
    private OrderedTree<Token> getParent()
    {
    	return parent;
    }
    
    
    //FINISHED!!!!!!!
    /**
    a method print the tree
    @return a string
*/
    
    public String toString()
    {
    	StringBuffer result = new StringBuffer();
    	buildString(parent, result);
 
    	
    	return new String(result);
    }
    
    /**
    private method to append the value for printing the list
    @parameter orderedTree<Token> and Stringbuffer
    @return void
*/
    
    private void buildString(OrderedTree<Token> child, StringBuffer result)
    {
    	
    	if(child.getRootData().getType().equals("List"))
    	{
    		result.append("[");
    		for(int i = 1 ; i<=child.getNumberOfChildren() ; i++)
    		{
    			
    			OrderedTree<Token> subchild = child.getKthChild(i);
    			buildString(subchild, result);
    		}
    		result.append("]");
    	}
    	else if(child.getRootData().getType().equals("SymbolLiteral"))
    	{
    		result.append(" " +child.getRootData().getSpelling() + " ");
    	}
    	else
    	{
    		throw new IllegalArgumentException("unexpected type: "+ child.getRootData().getType());
    	}
    }
    

	
}
