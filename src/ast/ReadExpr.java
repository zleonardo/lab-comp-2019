/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/

package ast;

import lexer.Token;

public class ReadExpr extends Expr {
	private Type type;

	public ReadExpr(Token readType){
		if(readType == Token.READINT)
			type = Type.intType;
		else if(readType == Token.READSTRING)
			type = Type.stringType;
	}

	public Type getType() {
		return this.type;
	}

	public void genJava( PW pw ) {
		if(type == Type.intType)
			pw.print("new Scanner(System.in).nextInt()");
		else if(type == Type.stringType)
			pw.print("new Scanner(System.in).nextString()");
	}
}