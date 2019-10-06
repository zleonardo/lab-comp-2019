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
        return left;
    }

    public Expr getRight(){
        return right;
    }

    public void setRightExpr(Expr right){
        this.right = right;
    }

    public void setLeftExpr(Expr left){
        this.left = left;
    }


    // REAPOVEITAR
    public void genC(PW pw){
        // String assign
        //pw.print("\t");

        // String assign
        if(this.left.getType() == Type.stringType){
            if(this.right != null){
                pw.print("strcpy(");
                this.left.genC(pw);
                pw.print(", ");
                this.right.genC(pw);
                pw.println(");");
            }
            else{
                this.left.genC(pw);
                pw.println(";");
            }
        }
        // int assign
        else {
            this.left.genC(pw);

            if (this.right != null){
                pw.print(" = ");
                this.right.genC(pw);
            }
            pw.println(";");
        }
	}
}
