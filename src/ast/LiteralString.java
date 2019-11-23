/*
    Nome: Vitor Pratali Camillo  RA: 620181
    Nome: Leonardo Zaccarias     RA: 620491
*/

package ast;

public class LiteralString extends Expr {
    
    public LiteralString( String literalString ) { 
        this.literalString = literalString;
    }
    
    @Override
    public void genJava( PW pw ) {
        pw.print(literalString);
    }

    @Override
    public Type getType() {
        return Type.stringType;
    }
    
    private String literalString;
}
