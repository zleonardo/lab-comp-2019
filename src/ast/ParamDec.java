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
		if(this.type == Type.intType) {
			pw.print("int ");
		}
		else if(this.type == Type.stringType) {
			pw.print("String ");
		} 
		else if(this.type == Type.booleanType) {
			pw.print("boolean ");
		} 
		pw.print(this.id);
	}
}
