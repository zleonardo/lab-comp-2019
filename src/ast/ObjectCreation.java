/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/
package ast;

public class ObjectCreation extends Expr {
	private String id;
	// qual o type?
	// o type vai ser o msm do type do objeto id
	private Type type = null;

	public ObjectCreation(String id) {
		this.id = id;
	}

	public Type getType(){
		return this.type;
	}

	public void genJava(PW pw){
		pw.print(this.id);
		pw.print(".new");
	}
}
