/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/

package ast;

public class TypeInt extends Type {
    
    public TypeInt() {
        super("int");
    }
    
    public String getName() {
        return "int";
    }

    public void genJava(PW pw){
        pw.print("int");
    }

}