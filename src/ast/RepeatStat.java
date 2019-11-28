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

    public void genJava(PW pw){
        pw.printlnIdent("do {");
        pw.add();
        this.whilePart.genJava(pw);
        pw.printIdent("}while(");
        this.condition.genJava(pw);
        pw.sub();
        // pw.tab();
        pw.println(");");
	}
}
