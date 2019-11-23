/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/

package ast;

import lexer.Token;

public class PrimaryExpr extends Expr {
	private Token scope = null;
	private Type type = null;
	private String firstIdName = null, secondIdName = null;
	private Type firstIdObj = null, secondIdObj = null;
	private ExprList exprList = new ExprList();

	public void setScope(Token scope){
		this.scope = scope;
	}

	public Token getScope() {
		return this.scope;
	}

	public void setType(Type type){
		this.type = type;
	}

	public Type getType() {
		//remover if quanto finalizado
		if(this.type == null)
			return Type.nullType;

		return this.type;
	}

	public void setFirstIdName(String firstIdName){
		this.firstIdName = firstIdName;
	}

	public String getFirstIdName(){
		return this.firstIdName;
	}

	public void setFirstIdObj(Type firstIdObj){
		this.firstIdObj = firstIdObj;
	}

	public Type getFirstIdObj(){
		return this.firstIdObj;
	}

	public void setSecondIdName(String secondIdName){
		this.secondIdName = secondIdName;
	}
	
	public String getSecondIdName(){
		return this.secondIdName;
	}

	public void setSecondIdObj(Type secondIdObj){
		this.secondIdObj = secondIdObj;
	}

	public Type getSecondIdObj(){
		return this.secondIdObj;
	}

	public void setExprList(ExprList exprList){
		this.exprList = exprList;
	}

	public ExprList getExprList(){
		return this.exprList;
	}

	public void genJava( PW pw ) {
		pw.printIdent("NULL");
	}
}