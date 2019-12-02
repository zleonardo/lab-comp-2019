/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/

package ast;

import java.util.ArrayList;

public class ExprList{
    private ArrayList<Expr> exprList = new ArrayList<Expr>();

    public void addExpr(Expr expr){
        this.exprList.add(expr);
    }
    
    public int getTamanho() {
    	return this.exprList.size();
    }
    
    public Expr getVetor(int i) {
    	return this.exprList.get(i);
	}
}
