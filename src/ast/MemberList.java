/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/
package ast;

import java.util.ArrayList;

public class MemberList {
   	private ArrayList<Variable> fieldList = new ArrayList<Variable>();
	private MethodList publicMethodList = new MethodList();
	private MethodList privateMethodList = new MethodList();

	public void addPublicMethod(Method method){
		this.publicMethodList.add(method);
	}

	public void addPrivateMethod(Method method){
		this.privateMethodList.add(method);
	}

	public void addField(ArrayList<Variable> fieldList){
		this.fieldList.addAll(fieldList);
	}

	public ArrayList<Variable> getFieldList(){
		return this.fieldList;
	}
	public MethodList getPublicMethodList(){
		return this.publicMethodList;
	}
	public MethodList getPrivateMethodList(){
		return this.privateMethodList;
	}
}
