/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/
package ast;

public class Variable extends Statement{
    private String name;
    private Type type;

    public Variable(String name){
        this.name = name;
    }

    public void setType(Type type){
        this.type = type;
    }

	public Type getType(){
		return this.type;
	}

    public String getName(){
        return this.name;
    }

    public void genC(PW pw){
        pw.print(this.type.getCname());
        pw.print(" ");
        pw.print(name);
        pw.println(";");
	}
}
