/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/
package ast;

public class WhileStat extends Statement{
    private Expr condition;
    private StatementList whilePart;

    public WhileStat(Expr condition){
        this.condition = condition;
    }

    public void setWhilePart(StatementList whilePart){
        this.whilePart = whilePart;
    }

    public void genJava(PW pw){
        pw.print("while (");
        this.condition.genJava(pw);
        pw.println("){");
        pw.add();
        this.whilePart.genJava(pw);
        pw.sub();
        // pw.tab();
        pw.println("}");
	}
}
