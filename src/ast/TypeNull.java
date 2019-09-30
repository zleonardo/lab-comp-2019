/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/

package ast;

public class TypeNull extends Type {

	public TypeNull() {
		super("NullType");
	}

	@Override
	public String getCname() {
		return "NULL";
	}

}
