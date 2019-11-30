/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/
package ast;

public class AssignExpr extends Statement{
    private Expr left, right = null;
    private static Boolean flag = true;

    public AssignExpr(){
        this.left = null;
    }

    public AssignExpr(Expr left){
        this.left = left;
    }

    // public AssignExpr(Expr left, Expr right){
    //     this.left = left;
    //     this.right = right;
    // }

    public Expr getLeft(){
        return this.left;
    }

    public Expr getRight(){
        return this.right;
    }

    public void setRightExpr(Expr right){
        this.right = right;
    }

    public void setLeftExpr(Expr left){
        this.left = left;
    }

    public void genJava(PW pw){
    	
    	pw.print("		");
    	
    	if(this.right != null) {
    		if(this.right.getType().name == "int" || this.right.getType().name == "string") {
        		if(flag) {
        			//System.out.println(flag);
        			pw.println("Scanner s = new Scanner(System.in);");
        			//pw.println("BufferedReader s = new BufferedReader(new InputStreamReader(System.in));");
        			flag = false;
        		}
        	}
    	}
    	
        this.left.genJava(pw);
        if(this.right != null) {
        	 pw.print(" = ");
             this.right.genJava(pw);
             pw.println(";");
        }
	}
}
