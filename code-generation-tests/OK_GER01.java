class A {
 
	public void m () {
      System.out.print("%d %s ", 7, " ");
      if ((1 > 0)) {
         System.out.print("%d %s ", 0, " ");
      }
      if ((1 >= 0)) {
         System.out.print("%d %s ", 1, " ");
      }
      if ((1 != 0)) {
         System.out.print("%d %s ", 2, " ");
      }
      if ((0 < 1)) {
         System.out.print("%d %s ", 3, " ");
      }
      if ((0 <= 1)) {
         System.out.print("%d %s ", 4, " ");
      }
      if ((0 == 0)) {
         System.out.print("%d %s ", 5, " ");
      }
      if ((0 >= 0)) {
         System.out.print("%d %s ", 6, " ");
      }
      if ((0 <= 0)) {
         System.out.print("%d %s ", 7, " ");
      }
      if ((1 == 0)) {
         System.out.print("%d %s ", 18, " ");
      }
      if ((0 > 1)) {
         System.out.print("%d %s ", 10, " ");
      }
      if ((0 >= 1)) {
         System.out.print("%d %s ", 11, " ");
      }
      if ((0 != 0)) {
         System.out.print("%d %s ", 12, " ");
      }
      if ((1 < 0)) {
         System.out.print("%d %s ", 13, " ");
      }
      if ((1 <= 0)) {
         System.out.print("%d %s ", 14, " ");
      }
   }
}

class Program {
 
	public void run () {
		A a;
         System.out.println("%s ", "7 0 1 2 3 4 5 6 7");
		a = A.new;
		a.m;
      }
}

