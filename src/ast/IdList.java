/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/
package ast;

import java.util.ArrayList;

public class IdList {
	private ArrayList<String> idList = new ArrayList<String>();

	public void add(String id){
		this.idList.add(id);
	}

	public void genC(PW pw){
        if(this.idList.size() > 0){
            // printa primeiro parametro
            pw.print(this.idList.get(0));

            // printa demais parametros separados por virgula
            for(int i = 1; i < this.idList.size(); i++){
                pw.print(", ");
                pw.print(this.idList.get(i));
            }
        }
	}
}
