/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/
package ast;

public class ReturnStat extends Statement{
    Expr expr;

    public ReturnStat(Expr expr){
        this.expr = expr;
    }

    public void genC(PW pw){
        pw.print("\n");
        // pw.tab();
    	pw.print("return ");
        this.expr.genC(pw);
        pw.println(";");
	}
}
