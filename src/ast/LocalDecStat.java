/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/
package ast;

public class LocalDecStat extends Statement{
	private IdList idList;
	private Type type;
	private Expr expr;

	public LocalDecStat(Type type){
		this.type = type;
	}

	public void setIdList(IdList idList){
		this.idList = idList;
	}

	public void setExpr(Expr expr){
		this.expr = expr;
	}

	public void genC(PW pw){
			pw.println(" LOCALDECSTAT ");
		// if (this.variable.getType() == Type.stringType){
		// 	pw.print("char ");
		// 	pw.print(this.variable.getName());
		// 	pw.println("[100];");
		// }
		// else{
		// 	pw.print("int ");
		// 	pw.print(this.variable.getName());
		// 	pw.println(";");
		// }
	}
}
