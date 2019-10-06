/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/
package ast;

public class MemberList {
   	private FieldList fieldList = new FieldList();
	private MethodList publicMethodList = new MethodList();
	private MethodList privateMethodList = new MethodList();

	public void addPublicMethod(Method method){
		this.publicMethodList.add(method);
	}

	public void addPrivateMethod(Method method){
		this.privateMethodList.add(method);
	}

	public void addField(Field field){
		this.fieldList.add(field);
	}

	public FieldList getFieldList(){
		return this.fieldList;
	}
	public MethodList getPublicMethodList(){
		return this.publicMethodList;
	}
	public MethodList getPrivateMethodList(){
		return this.privateMethodList;
	}
}
