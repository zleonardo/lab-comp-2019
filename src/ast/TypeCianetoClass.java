/*
	Nome: Vitor Pratali Camillo    	RA: 620181
	Nome: Leonardo Zaccarias    	RA: 620491
*/

package ast;

import java.util.ArrayList;

public class TypeCianetoClass extends Type {

	public TypeCianetoClass( String name ) {
		super(name);
	}

	// @Override
	// public String getCname() {
	//    return getName();
	// }

	public void setOpen(Boolean flag) {
		this.open = flag;
	}

	public Boolean getOpen() {
		return this.open;
	}

	public void setSuperClass(TypeCianetoClass superClass){
		this.superclass = superClass;
	}

	public void setMemberList(MemberList memberList){
		this.fieldList = memberList.getFieldList();
		this.publicMethodList = memberList.getPublicMethodList();
		this.privateMethodList = memberList.getPrivateMethodList();
	}

	public Variable returnField(String name){
		Variable v = null;
		
		for(int i = 0; i < fieldList.size(); i++){
			if(fieldList.get(i).getName() == name)
				v = fieldList.get(i);
		}
		return v;
	}

	public void genJava(PW pw){
		pw.print("\nCLASS\n");
	}

	private String name;
	private TypeCianetoClass superclass;
	private ArrayList<Variable> fieldList;
	private MethodList publicMethodList, privateMethodList;
	private Boolean open;
	// m�todos p�blicos get e set para obter e iniciar as vari�veis acima,
	// entre outros m�todos
}
