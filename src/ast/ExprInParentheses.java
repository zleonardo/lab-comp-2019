/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/
package ast;

public class ExprInParentheses extends Expr {
	private Expr expr;
	private Type type;

	public ExprInParentheses(Expr expr) {
		this.expr = expr;
		this.type = expr.getType();
	}

	public Type getType(){
		return this.type;
	}
	
	public void genC(PW pw){
		pw.print("(");
		this.expr.genC;
		pw.print(")");
	}
}