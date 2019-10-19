/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/
package ast;

public class LocalDecStat extends Statement{
	private IdList idList;
	private Type type;
	private Expr expr = null;

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
		// Type IdList [ "=" Expr ]
		//this.type.genC(pw);
		pw.println(" TYPE ");
		this.idList.genC(pw);
		if(this.expr != null){
			pw.println(" = ");
			this.expr.genC(pw);
		}
	}
}
