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

	public Method(String name){
		this.name = name;
	}
	
	public String getName() {
		return this.name;
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

	}

}
