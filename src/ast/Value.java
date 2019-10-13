/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/
package ast;

public class Value extends Expr {
	private Type type;
	private int intValue = null;
	private String stringValue = null;
	private boolean booleanValue = null;

	public Value(int newValue) {
		this.type = Type.intType;
		int this.intValue = newValue;
	}
	public Value(String newValue) {
		this.type = Type.stringType;
		String this.stringValue = newValue;
	}
	public Value(boolean newValue) {
		this.type = Type.booleanType;
		boolean this.booleanValue = newValue;
	}
	
	public Type getType(){
		return this.type;
	}

	public void genC(PW pw){
		if(intValue != null)
			pw.print(this.intValue);
		if(stringValue != null)
			pw.print(this.stringValue);
		if(booleanValue == true)
			pw.print("true");
		if(booleanValue == false)
			pw.print("false");
	}
}
