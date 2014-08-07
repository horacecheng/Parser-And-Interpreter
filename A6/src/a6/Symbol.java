package a6;

/**
A class for representing symbols in a programming language 
@author Jeff Smith
@version for Assignment 5, CS 152, Spring 2014
*/

public class Symbol extends Value
{

// a string with the same spelling as the token
private String spelling;

/**
 * Constructor for objects of class Symbol
 */
public Symbol(String spelling)
{
    this.spelling = spelling;
    this.binded = true;
}

/**
    Converts the Symbol to a string
    @return a string with the same spelling as the token
 */

public String toString()
{
    return spelling;
}
}
