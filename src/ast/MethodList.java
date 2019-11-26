/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/
package ast;

import java.util.ArrayList;

public class MethodList {
	private ArrayList<Method> methodList = new ArrayList<Method>();

	public void add(Method method){
		this.methodList.add(method);
	}

	public void genJava(PW pw){
		
		for(Method metodo : this.methodList){
			
            metodo.genJava(pw);
            pw.println(" ");
        }
	}

}
