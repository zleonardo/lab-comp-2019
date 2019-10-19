/*
    Nome: Vitor Pratali Camillo  RA: 620181
    Nome: Leonardo Zaccarias     RA: 620491
*/

package ast;

public class LiteralInt extends Expr {
    
    public LiteralInt( int value ) { 
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    public void genJava( PW pw, boolean putParenthesis ) {
        pw.printIdent("" + value);
    }
    
    public Type getType() {
        return Type.intType;
    }

    @Override
	public void genJava( PW pw ) {
    	pw.print(Integer.toString(this.value));
    }
    
    private int value;
}
