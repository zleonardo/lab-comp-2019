/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/

package ast;

abstract public class Expr extends Statement {

    // new method: the type of the expression
    abstract public Type getType();
}