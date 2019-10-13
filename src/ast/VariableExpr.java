/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/
package ast;

public class VariableExpr extends Expr {
	// private Variable variable;

	// public VariableExpr(Variable v) {
	// 	this.variable = v;
	// }
	
	public Type getType(){
		return null;
	}
	
	// public String getName(){
	// 	// return this.variable.getName();
	// }

	public void genC(PW pw){
		// pw.print(this.variable.getName());
	}
}
