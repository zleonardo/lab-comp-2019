/*
    Nome: Vitor Pratali Camillo  RA: 620181
    Nome: Leonardo Zaccarias     RA: 620491
*/

package ast;

abstract public class Type {

    public Type( String name ) {
        this.name = name;
    }

    public static Type booleanType = new TypeBoolean();
    public static Type intType = new TypeInt();
    public static Type stringType = new TypeString();
    public static Type nullType = new TypeNull();

    public String getName() {
        return name;
    }

	abstract public void genJava(PW pw);

    protected String name;
}
