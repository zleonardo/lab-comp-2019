/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/

package ast;
import lexer.Token;

public class PrimaryExpr extends Expr {
	private boolean isMethod = false;
	private Token scope = null;
	private Type type = null;
	private String firstIdName = null, secondIdName = null;
	private Type firstIdObj = null, secondIdObj = null;
	private ExprList exprList = new ExprList();
	private ObjectCreation creationObj;
	private boolean flag = false;

	public void setScope(Token scope){
		this.scope = scope;
	}
	
	public void isMethod(){
		this.isMethod = true;
	}

	public Token getScope() {
		return this.scope;
	}

	public void setType(Type type){
		this.type = type;
	}

	public Type getType() {
		if(this.type == null)
			return Type.nullType;

		return this.type;
	}

	public void setFirstIdName(String firstIdName){
		this.firstIdName = firstIdName;
	}

	public String getFirstIdName(){
		return this.firstIdName;
	}
	
	public void setObject(ObjectCreation obj) {
		this.creationObj = obj;
	}

	public void setFirstIdObj(Type firstIdObj){
		this.firstIdObj = firstIdObj;
	}

	public Type getFirstIdObj(){
		return this.firstIdObj;
	}

	public void setSecondIdName(String secondIdName){
		this.secondIdName = secondIdName;
	}
	
	public String getSecondIdName(){
		return this.secondIdName;
	}

	public void setSecondIdObj(Type secondIdObj){
		this.secondIdObj = secondIdObj;
	}

	public Type getSecondIdObj(){
		return this.secondIdObj;
	}

	public void setExprList(ExprList exprList){
		this.exprList = exprList;
	}

	public ExprList getExprList(){
		return this.exprList;
	}

	public void genJava( PW pw ) {
				
		if(this.creationObj != null) {
			this.creationObj.genJava(pw);
		}
		else if(this.firstIdName != null) {
			if(this.scope != null) {
				if(this.scope.toString().equals("self")) {
					pw.print("this.");
				}else {
					pw.print(this.scope + ".");
				}
				if(this.firstIdName.contains(":")) {
					this.firstIdName = this.firstIdName.replaceAll(":", "");
					flag = true;
				}
				if(flag) {
					pw.print(this.firstIdName + "(");
					for(int i = 0; i < this.exprList.getTamanho(); i++) {
						this.exprList.getVetor(i).genJava(pw);
					}
					pw.print(")");
				}else{
					if(this.isMethod) {
						pw.print(this.firstIdName + "()");
					}else {
						pw.print(this.firstIdName);
					}
					
				}
			}else {
				pw.print(this.firstIdName);
			}
		}
		
		if(this.secondIdName != null) {
			pw.print(".");
			if(this.secondIdName.contains(":")) {
				this.secondIdName = this.secondIdName.replace(":", "");
			}
			pw.print(this.secondIdName + "(");
			for(int i = 0; i < this.exprList.getTamanho(); i++) {
				this.exprList.getVetor(i).genJava(pw);
				if((i + 1) < this.exprList.getTamanho()) {
					pw.print(", ");
				}
			}
			pw.print(")");
		}
		
	}
}