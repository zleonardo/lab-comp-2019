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

    public void genJava(PW pw){
        /*if(this.exprList.size() > 0){
            // printa primeiro parametro
            Expr v = this.exprList.get(0);
            v.genJava(pw);

            // printa demais parametros separados por virgula
            for(int i = 1; i < this.exprList.size(); i++){
                pw.print(", ");
                v = this.exprList.get(i);
                v.genJava(pw);
            }
        }*/
	}
}
