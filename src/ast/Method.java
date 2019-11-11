/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/
package ast;

public class Method {
	private String name;
	private String type;
	private int parametros = 0;

	public Method(String name){
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return this.type;
	}
	
	public void setParametro() {
		this.parametros = this.parametros + 1;
	}
	
	public int getParametro() {
		return parametros;
	}

	public void genJava(PW pw){

	}

}
