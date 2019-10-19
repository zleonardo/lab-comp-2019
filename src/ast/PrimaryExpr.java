/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/

package ast;
import lexer.Token;

public class PrimaryExpr extends Expr {
	private Token scope = null;
	private String firstId = "", secondId = "";
	private ExprList exprList = new ExprList();

	public void setScope(Token scope){
		this.scope = scope;
	}

	public void setFirstId(String firstId){
		this.firstId = firstId;
	}

	public void setSecondId(String secondId){
		this.secondId = secondId;
	}

	public void setExprList(ExprList exprList){
		this.exprList = exprList;
	}

	public void genC( PW pw ) {
		pw.printIdent("NULL");
	}

	public Type getType() {
		return Type.nullType;
	}
}