/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/
package ast;
import java.util.ArrayList;

public class Method {
	private String name;
	private Type type;
	private ArrayList<ParamDec> parametros;
	private StatementList statList;
	private String scope;

	public Method(String name, String scope){
		this.name = name;
		this.statList = new StatementList();
		this.scope = scope;
		this.parametros = new ArrayList<ParamDec>();
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getScope() {
		return this.scope;
	}
	
	public void addStat(StatementList statlist) {
		this.statList = statlist;
	}
	
	public void setType(Type type) {
		this.type = type;
	}
	
	public Type getType() {
		return this.type;
	}
	
	public void addParametro(ParamDec parametro) {
		this.parametros.add(parametro);
	}
	
	public ArrayList<ParamDec> getParametro() {
		return parametros;
	}

	public void genJava(PW pw){
		
		if(this.scope != null) {
			if(this.scope.contains("override")) {
				pw.printlnIdent("@Override");
				this.scope = this.scope.replace("override", "");
			}else {
				pw.print("	" + this.scope);
			}
		}else {
			//pw.print(" public");
		}
		
		if(this.type == null) {
			pw.print(" void ");
		}else if(this.type == Type.intType){
			pw.print(" int ");
		}else if(this.type == Type.stringType){
			pw.print(" String ");
		}else if(this.type == Type.booleanType){
			pw.print(" boolean ");
		}
		
		if(this.name.contains(":")) {
			this.name = this.name.replaceAll(":", "");
		}
		pw.print(this.name);
		pw.print(" (");
		
		int i = 0;		
		
		for(ParamDec p : this.parametros){
			if(i != 0) {
				pw.print(", ");
			}
			p.genJava(pw);
			i++;
		    //pw.print(" ");
		}
		
		pw.println(") {");
		
		pw.add();
		this.statList.genJava(pw);
		pw.sub();
        pw.printlnIdent("}");
	}
}
