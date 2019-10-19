/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/
package ast;

public class NegationFactor extends Expr {
	private Expr expr;
	private Type type;

	public NegationFactor(Expr expr) {
		this.expr = expr;
		this.type = expr.getType();
	}

	public Type getType(){
		return this.type;
	}

	public void genJava(PW pw){
		pw.print("! ");
		this.expr.genJava(pw);
	}
}
