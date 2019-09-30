/*
   Nome: Vitor Pratali Camillo    RA: 620181
   Nome: Leonardo Zaccarias    RA: 620491
*/

package ast;
/*
 * Krakatoa Class
 */
public class TypeCianetoClass extends Type {

   public TypeCianetoClass( String name ) {
      super(name);
   }

   @Override
   public String getCname() {
      return getName();
   }

   public void setSuperClass(TypeCianetoClass superClass){
      this.superclass = superClass;
   }

   public void setMemberList(MemberList memberList){
      this.fieldList = memberList.getFieldList();
      this.publicMethodList = memberList.getPublicMethodList();
      this.privateMethodList = memberList.getPrivateMethodList();
   }

   private String name;
   private TypeCianetoClass superclass;
   private FieldList fieldList;
   private MethodList publicMethodList, privateMethodList;
   // m�todos p�blicos get e set para obter e iniciar as vari�veis acima,
   // entre outros m�todos
}
