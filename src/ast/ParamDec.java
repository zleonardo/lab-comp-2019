/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/
package ast;

public class ParamDec {
	private String id;
	private Type type;
	
	public ParamDec(String id, Type type){
		this.id = id;
		this.type = type;
	}
	
	public Type getType() {
		return this.type;
	}

	public void genJava(PW pw){
		pw.print(this.type.toString());
		pw.print(this.id);
	}
}
