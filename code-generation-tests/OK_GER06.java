class A {
 
 void m () {
		Integer i;
Integer j;
Integer k;
      System.out.print(7 + " ");
		i = 1;
		j = i + 1;
		k = j + 1;
      System.out.print(i + " ");
      System.out.print(j + " ");
      System.out.print(k + " ");
		i = ((((3 + 1) * 3) / 2) / 2) + 1;
      System.out.print(i + " ");
		i = ((100 - 95) * 2) - 5;
      System.out.print(i + " ");
		i = (100 - (45 * 2)) - 4;
      System.out.print(i + " ");
      System.out.print(7 + " ");
   }
}

class Program {
 
 void run () {
		A a;
         System.out.println("7 1 2 3 4 5 6 7");
		a = new A();
		a.m();
      }
}

