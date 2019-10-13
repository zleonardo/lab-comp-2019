/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/

package ast;
import Lexer.*;

public class CompositeExpr extends Expr {
	Expr left, right = null;
	Token oper = null;

	public CompositeExpr(Expr left, Token oper, Expr right){
		this.left = left;
		this.oper = oper;
		this.right = right;
	}
 
	public Type getType(){
		return left.getType();
	}

	public void genC(PW pw) {
		left.genC(pw);
		if(oper == null)
			return
		if(this.oper == Token.OR)
			pw.print(" || ");
		else if(this.oper == Token.AND)
			pw.print(" && ");
		else
			pw.print(" " + this.oper.toString() + " ");
		right.genC(pw);
	}
}
