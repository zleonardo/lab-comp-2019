/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/
package ast;

import java.util.ArrayList;

public class StatementList{
    private ArrayList<Statement> statList;
    
    public StatementList() {
    	this.statList = new ArrayList<Statement>();
    }

    public void addStat(Statement newStat){
        this.statList.add(newStat);
    }

    public void genJava(PW pw){
        for( Statement stat : this.statList ){
            stat.genJava(pw);
            pw.println(";");
        }
	}
}
