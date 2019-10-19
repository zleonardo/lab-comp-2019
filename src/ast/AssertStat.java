/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/
package ast;

public class AssertStat extends Statement{
    Expr expr;
    LiteralString string;

    public AssertStat(Expr expr){
        this.expr = expr;
    }

    public void setString(LiteralString s){
        this.string = s;
    }

    public void genC(PW pw){
    	pw.print("assert ");
        this.expr.genC(pw);
    	pw.print(" ");
        this.string.genC(pw);
        pw.println(";");
	}
}
