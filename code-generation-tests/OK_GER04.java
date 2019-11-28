class A {
 
 void m () {
		Integer i;
		Boolean b;
      System.out.print(6 + " ");
		i = 1;
		while (i <= 5){
         System.out.print(i + " ");
		i = i + 1;
		}
		b = false;
		while (b != true){
         System.out.print(6 + " ");
		b = ! b;
		}
   }
}

class Program {
 
 void run () {
		A a;
         System.out.println("6 1 2 3 4 5 6");
		a = new A();
		a.m();
      }
}

