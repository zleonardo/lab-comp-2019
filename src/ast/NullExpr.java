/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/

package ast;

public class NullExpr extends Expr {
	public void genC( PW pw ) {
		pw.printIdent("NULL");
	}

	public Type getType() {
		return Type.nullType;
	}
}