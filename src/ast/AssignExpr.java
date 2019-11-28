/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/
package ast;

public class AssignExpr extends Statement{
    private Expr left, right = null;

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
    	
    	/*if(this.right != null && this.right == ReadExpr) {
    		
    	}*/
    	
    	pw.print("		");
        this.left.genJava(pw);
        if(this.right != null) {
        	 pw.print(" = ");
             this.right.genJava(pw);
             pw.println(";");
        }
	}
}
