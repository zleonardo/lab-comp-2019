/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/
package ast;

public class ObjectCreation extends Expr {
	private String id;
	private Type type = null;

	public ObjectCreation(String id) {
		this.id = id;
	}

	public Type getType(){
		return this.type;
	}

	public void genJava(PW pw){
		pw.print("new ");
		pw.print(this.id + "()");
		
	}
}
