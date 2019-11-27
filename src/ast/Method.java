/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/
package ast;
import java.util.ArrayList;

public class Method {
	private String name;
	private Type type;
	private ArrayList<ParamDec> parametros = new ArrayList<ParamDec>();
	private StatementList statList;
	private String scope;

	public Method(String name, String scope){
		this.name = name;
		this.statList = new StatementList();
		this.scope = scope;
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
		pw.add();
		
		if(this.scope != null) {
			pw.print(this.scope);
		}
		
		if(this.type == null) {
			pw.print(" void ");
		}else {
			pw.print(" " + this.type.toString() + " ");
		}
		pw.print(this.name);
		pw.print(" (");
		
		for(ParamDec p : this.parametros){
					
			p.genJava(pw);
		    pw.println(" ");
		}
		
		pw.println(") {");
		
		pw.add();
		this.statList.genJava(pw);
		pw.sub();
        pw.printlnIdent("}");
	}
}
