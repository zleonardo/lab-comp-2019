/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/
package ast;

public class Field {
	private Type type;
	private IdList idList;
	
	public Field(Type type, IdList idList){
		this.type = type;
		this.idList = idList;
	}
	
	public Type getType() {
		return this.type;
	}

	public void genJava(PW pw){
		idList.genJava(pw);
	}
}
