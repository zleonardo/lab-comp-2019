/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/

package ast;

import lexer.Token;

public class SignalFactor extends Expr {
	private Token signal;
	private Expr factor;

	public SignalFactor(Token signal, Expr factor){
		this.signal = signal;
		this.factor = factor;
	}

	public Type getType() {
		return factor.getType();
	}

	public void genJava( PW pw ) {
		if(signal == Token.PLUS)
			pw.print("+");
		else if(signal == Token.MINUS)
			pw.print("-");
		factor.genJava(pw);
	}
}