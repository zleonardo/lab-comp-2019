/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/

package ast;
import java.util.ArrayList;

public class IdList {
	private ArrayList<Variable> idList = new ArrayList<Variable>();

	public void add(Variable id){
		this.idList.add(id);
    }
    
	public int size(){
		return this.idList.size();
    }
    
    public Variable getVariable(int i){
        return this.idList.get(i);
    }

	public void genJava(PW pw){
        if(this.idList.size() > 0){
            // printa primeiro parametro
        	/*if(this.idList.get(0).getType() == Type.intType) {
    			pw.print("Integer");
    		}else if(this.idList.get(0).getType() == Type.stringType) {
    			pw.print("String");
    		}else if(this.idList.get(0).getType() == Type.booleanType) {
    			pw.print("Boolean");
    		}else {
    			pw.print(this.idList.get(0).getType().getName().toString());
    		}*/
        	//pw.print(this.idList.get(0).getType());
            this.idList.get(0).genJava(pw);

            // printa demais parametros separados por virgula
            for(int i = 1; i < this.idList.size(); i++){
                //pw.print(", ");
                this.idList.get(i).genJava(pw);
            }
        }
        //pw.println(";");
	}
}
