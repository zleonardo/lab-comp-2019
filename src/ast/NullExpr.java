/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/

package ast;

public class NullExpr extends Expr {
    
   public void genC( PW pw, boolean putParenthesis ) {
      pw.printIdent("NULL");
   }
   
   public Type getType() {
      //# corrija
      return null;
   }
}