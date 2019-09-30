package ast;

public class MemberList {
   	private FieldList fieldList;
	private MethodList publicMethodList, privateMethodList;

	public void addPublicMethod(Method method){
		publicMethodList.add(method);
	}

	public void addPrivateMethod(Method method){
		privateMethodList.add(method);
	}

	public void addField(Field field){
		fieldList.add(field);
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
