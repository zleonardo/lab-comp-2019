/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/

package ast;

import lexer.Token;

public class PrimaryExpr extends Expr {
	private Token scope = null;
	private Type type;
	private String firstIdName = null, secondIdName = null;
	private Variable firstIdObj = null, secondIdObj = null;
	private ExprList exprList = new ExprList();

	public void setScope(Token scope){
		this.scope = scope;
	}

	public void setType(Type type){
		this.type = type;
	}

	public Type getType() {
		return Type.nullType;
	}

	public void setFirstIdName(String firstIdName){
		this.firstIdName = firstIdName;
	}

	public String getFirstIdName(){
		return this.firstIdName;
	}

	public void setFirstIdObj(Variable firstIdObj){
		this.firstIdObj = firstIdObj;
	}

	public Variable getFirstIdObj(){
		return this.firstIdObj;
	}

	public void setSecondIdName(String secondIdName){
		this.secondIdName = secondIdName;
	}
	
	public String getSecondIdName(){
		return this.secondIdName;
	}

	public void setSecondIdObj(Variable secondIdObj){
		this.secondIdObj = secondIdObj;
	}

	public Variable getSecondId(){
		return this.secondIdObj;
	}

	public void setExprList(ExprList exprList){
		this.exprList = exprList;
	}

	public void genJava( PW pw ) {
		pw.printIdent("NULL");
	}
}