/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/
package ast;

import java.util.ArrayList;

public class FieldList {
	private ArrayList<Field> fieldList = new ArrayList<Field>();
	
	public void add(Field field){
		this.fieldList.add(field);
	}

	public void genJava(PW pw){
		
		for( Field list : this.fieldList ){
            list.genJava(pw);
            pw.println(" ");
        }
	}

}
