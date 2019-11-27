/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/
package ast;

import java.util.ArrayList;

public class MemberList {
   	private ArrayList<Variable> fieldList = new ArrayList<Variable>();
	private MethodList MethodList = new MethodList();

	public void addMethod(Method method){
		this.MethodList.add(method);
	}

	public void addField(ArrayList<Variable> fieldList){
		this.fieldList.addAll(fieldList);
	}

	public ArrayList<Variable> getFieldList(){
		return this.fieldList;
	}
	public MethodList getMethodList(){
		return this.MethodList;
	}
}
