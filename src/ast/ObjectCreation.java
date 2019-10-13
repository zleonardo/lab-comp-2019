/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/
package ast;

public class ObjectCreation extends Expr {
	private String id;
	// qual o type?
	private Type type;

	public ObjectCreation(String id) {
		this.id = id;
	}

	public Type getType(){
		return this.type;
	}

	public void genC(PW pw){
		pw.print(this.id);
		pw.print(".new");
	}
}
