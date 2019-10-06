/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/
package ast;

public class IfStat extends Statement{
    private Expr condition;
    private StatementList ifPart, elsePart = null;

    public IfStat(Expr condition){
        this.condition = condition;
    }

    public void setIfPart(StatementList ifPart){
        this.ifPart = ifPart;
    }

    public void setElsePart(StatementList elsePart){
        this.elsePart = elsePart;
    }

    public void genC(PW pw){
        pw.print("if (");
        this.condition.genC(pw);
        pw.println("){");
        pw.add();
        this.ifPart.genC(pw);
        pw.sub();
        // pw.tab();
        pw.println("}");

        if (elsePart != null){
            // pw.tab();
            pw.println("else {");
            pw.add();
            this.elsePart.genC(pw);
            pw.sub();
            // pw.tab();
            pw.println("}");
        }
	}
}
