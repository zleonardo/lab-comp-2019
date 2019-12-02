/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/
package ast;

public class BreakStat extends Statement{

    public void genJava(PW pw){
    	pw.println("break");
	}
}
