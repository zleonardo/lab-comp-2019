/*
    Nome: Vitor Pratali Camillo  RA: 620181
    Nome: Leonardo Zaccarias     RA: 620491
*/

package ast;

public class LiteralBoolean extends Expr {

    public LiteralBoolean( boolean value ) {
        this.value = value;
    }

    @Override
	public void genJava( PW pw ) {
    	if(value)
    		pw.print("true");
    	else
    		pw.print("false");
    }


    @Override
	public Type getType() {
        return Type.booleanType;
    }

    public static LiteralBoolean True  = new LiteralBoolean(true);
    public static LiteralBoolean False = new LiteralBoolean(false);

    private boolean value;

}
