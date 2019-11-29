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
    	if(type == Type.intType)
			pw.print("Integer");
		else if(type == Type.stringType)
			pw.print("String");
		else if(type == Type.booleanType)
			pw.print("Boolean");
		else {
			pw.print(type.getName().toString());
		}
        pw.print(" ");
        pw.print(this.getName());
        pw.println(";");
	}
}
