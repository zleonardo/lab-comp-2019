/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/
package ast;

public class ObjectCreation extends Expr {
	private String id;
	private Type type;

	public ObjectCreation(String id) {
		this.object = object;
		this.type = object.getType();
	}

	public Type getType(){
		return this.type;
	}

	public void genC(PW pw){
		this.object.genC;
		pw.print(".new");
	}
}
