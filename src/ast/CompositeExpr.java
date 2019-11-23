/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/

package ast;
import lexer.Token;

public class CompositeExpr extends Expr {
	Expr left, right = null;
	Token oper = null;
	Type type = null;

	public CompositeExpr(Expr left, Token oper, Expr right){
		this.left = left;
		this.oper = oper;
		this.right = right;
	}
 
    @Override
	public Type getType(){
		return this.type;
	}

	public void setType(Type type){
		this.type = type;
	}

    @Override
	public void genJava(PW pw) {
		left.genJava(pw);
		if(oper == null)
			return;
		if(this.oper == Token.OR)
			pw.print(" || ");
		else if(this.oper == Token.AND)
			pw.print(" && ");
		else
			pw.print(" " + this.oper.toString() + " ");
		right.genJava(pw);
	}
}
