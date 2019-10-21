/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/
package ast;

import lexer.Token;

class PrintStat extends Statement{
    ExprList exprList;;
    Token printType;

    public PrintStat(Token printType){
        this.printType = printType;
    }
    
    public void setExprList(ExprList exprList){
        this.exprList = exprList;
    }

    public void genJava(PW pw){
        pw.print("\n");
        // pw.tab();
    	pw.print("return ");
        this.expr.genJava(pw);
        pw.println(";");
	}
}
