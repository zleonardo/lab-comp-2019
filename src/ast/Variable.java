/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/
package ast;

public class Variable extends Type{
    private Type type;

    public Variable(String name){
        super(name);
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

    public void genJava(PW pw){
        pw.print(this.type.getName());
        pw.print(" ");
        pw.print(this.getName());
        pw.println(";");
	}
}
