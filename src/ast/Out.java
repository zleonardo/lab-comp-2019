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
		pw.print("System.out.");
		if(name.equals("print:")) {
			pw.print("print(");
		}
		else {
			pw.print("println(");
		}

	}

}
