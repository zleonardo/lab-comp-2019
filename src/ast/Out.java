/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/
package ast;

public class Out extends Statement{
	
	private String name;
	private ExprList exprList;
	
	public Out(String nome) {
		this.name = nome;
	}
	
	public void setExpr(ExprList list) {
		this.exprList = list;
	}

	public void genJava(PW pw){
		pw.printIdent("System.out.");
		if(name.equals("print:")) {
			pw.print("print(");
		}
		else {
			pw.print("println(");
		}

        for(int i = 0; i < exprList.getTamanho(); i++){
            if(i + 1 == exprList.getTamanho()){
                if(this.exprList.getVetor(i).getType() == Type.intType){
                	this.exprList.getVetor(i).genJava(pw);
                }
                else if(this.exprList.getVetor(i).getType() == Type.stringType){
                	this.exprList.getVetor(i).genJava(pw);
                	
                }
            }
            else{
                if(this.exprList.getVetor(i).getType() == Type.intType){
                	this.exprList.getVetor(i).genJava(pw);
                    pw.print(" + ");
                }
                else if(this.exprList.getVetor(i).getType() == Type.stringType){
                	this.exprList.getVetor(i).genJava(pw);
                    pw.print(" + ");
                }
            }
        }
        pw.print(")");
    }

}
