/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/
package ast;

public class NegationFactor extends Expr {
	private Expr NegationFactor;
	private Type type;

	public NegationFactor(Expr expr) {
		this.expr = expr;
		this.type = expr.getType();
	}

	public Type getType(){
		return this.type;
	}

	public void genC(PW pw){
		pw.print("! ");
		this.expr.genC;
	}
}
