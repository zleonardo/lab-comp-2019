class A {
 
 void m () {
      System.out.print("%d %s ", 6, " ");
      if (true && true) {
         System.out.print("%d %s ", 1, " ");
      }
      if ((false && true)) {
         System.out.print("%d %s ", 1000, " ");
      }
      if (true && false) {
         System.out.print("%d %s ", 1000, " ");
      }
      if ((false && false)) {
         System.out.print("%d %s ", 1000, " ");
      }
      if (true || true) {
         System.out.print("%d %s ", 2, " ");
      }
      if ((true || false)) {
         System.out.print("%d %s ", 3, " ");
      }
      if (false || true) {
         System.out.print("%d %s ", 4, " ");
      }
      if ((false || false)) {
         System.out.print("%d %s ", 1000, " ");
      }
      if ((! false)) {
         System.out.print("%d %s ", 5, " ");
      }
      if (! true) {
         System.out.print("%d %s ", 1000, " ");
      }
      if ((true || (true && false))) {
         System.out.print("%d %s ", 6, " ");
      }
   }
}

class Program {
 
	public void run () {
		 a;
         System.out.println("%s ", "6 1 2 3 4 5 6");
		a = A;
		a      }
}

