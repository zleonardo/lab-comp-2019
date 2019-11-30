/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/

package ast;

import lexer.Token;

public class ReadExpr extends Expr {
	private Token readType;
	private Type type;
	//private Boolean flag = true;

	public ReadExpr(Token readType){
		this.readType = readType;
		if(readType == Token.READINT)
			type = Type.intType;
		else if(readType == Token.READSTRING)
			type = Type.stringType;
		//else
			//type = Type.nullType;
	}

	public Type getType() {
		return this.type;
	}

	public void genJava( PW pw ) {
		if(type == Type.intType)
			//pw.print("Integer.parseInt(s.nextLine())");
			pw.print("s.nextInt()");
		else if(type == Type.stringType)
			pw.print("s.nextString()");
	}
}