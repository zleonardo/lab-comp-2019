/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/
package ast;

public class RepeatStat extends Statement{
    private Expr condition;
    private StatementList whilePart;

    public RepeatStat(StatementList whilePart){
        this.whilePart = whilePart;
    }

    public void setCondition(Expr condition){
        this.condition = condition;
    }

    public void genC(PW pw){
        pw.println("do {");
        pw.add();
        this.whilePart.genC(pw);
        pw.print("}while(");
        this.condition.genC(pw);
        pw.sub();
        // pw.tab();
        pw.println(")");
	}
}
