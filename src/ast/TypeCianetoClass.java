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
	//    return super.getName();
	// }
	
	public String getType() {
		return super.getName();
	}

	public void setOpen(Boolean flag) {
		this.open = flag;
	}

	public Boolean getOpen() {
		return this.open;
	}

	public void setSuperClass(TypeCianetoClass superClass){
		this.superclass = superClass;
	}
	
	public TypeCianetoClass getSuperClass() {
		return superclass;
	}

	public void setMemberList(MemberList memberList){
		this.fieldList = memberList.getFieldList();
		this.MethodList = memberList.getMethodList();
	}
	
	public MethodList getMemberList() {
		return this.MethodList;
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
		
		pw.print("class " + super.getName());
		if(this.superclass != null) {
			pw.print(" extends " + this.getSuperClass().getName());
		}
		pw.println(" {");
		
		for( Variable list : this.fieldList ){
            list.genJava(pw);
			pw.println(";");
	}
		
		if(MethodList != null) {
			pw.add();
			MethodList.genJava(pw);
		}
		
		pw.println("}");
		
	}

	private TypeCianetoClass superclass = null;
	private ArrayList<Variable> fieldList;
	private MethodList MethodList;
	private Boolean open;
	// m�todos p�blicos get e set para obter e iniciar as vari�veis acima,
	// entre outros m�todos
}
