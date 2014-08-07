package a6;

import java.util.LinkedList;

public class Function extends MapValue 
{
	OrderedTree<Token> ftreedefstart;
	public Function(OrderedTree<Token> ftreedefstart)
	{
		this.ftreedefstart = ftreedefstart;
	}
	
	/**
    Builds an ordered tree from given data and children.
    A shallow copy is made of the list of children.
    @param A List of args.  Empty List of args
       are permitted
     @return Expr child from Def node
     @throws IllegalArgumentException if the arguments size is not the same as def's function's argument size
 */
	
	public OrderedTree<Token> getExprFromDef(LinkedList<Value> args)
	{  
		  OrderedTree<Token> ftree = ftreedefstart.getKthChild(1);
		  int rightargsize = ftree.getNumberOfChildren();
		  
		  
		  if(rightargsize!= args.size()) 
			  throw new IllegalArgumentException(
				         "arity mismatch: " +
				         args.size() + " arguments found, " +
				         rightargsize + " expected");

		  
		  return  ftreedefstart.getKthChild(2); //first child is Sig, second child is Expr by the grammar
	}
	 /**
    get the right argument size
    @return an integer
   */  
	
	public int getRightArgumentSize()
	{
		OrderedTree<Token> ftree = ftreedefstart.getKthChild(1);  
		return ftree.getNumberOfChildren();
	}
	
	 /**
    get Sig child from def node
    @return  OrderedTree<Token>
    @throws IllegalArgumentException if the arguments size is not the same as def's function's argument size
   */  
	
	public OrderedTree<Token> getOriginalSig(LinkedList<Value> args)
	{
		 OrderedTree<Token> ftree = ftreedefstart.getKthChild(1);
		  int rightargsize = ftree.getNumberOfChildren();
		  
		  
		  if(rightargsize!= args.size()) 
			  throw new IllegalArgumentException(
				         "arity mismatch: " +
				         args.size() + " arguments found, " +
				         rightargsize + " expected");
		  return  ftreedefstart.getKthChild(1);
	}
	
	
	
}
