class A {
 
 void m1 () {
      System.out.print(" 2 ");
   }
 void m2 (Integer n) {
      System.out.print(n + " ");
		m1   }
}

class B extends A {
 
      @Override
	 void m1 () {
         System.out.println(" 4 ");
      }
}

class Program {
 
 void run () {
		A a;
		B b;
            System.out.println("4 1 2 3 4");
            System.out.print("4 ");
		a = new A();
		a.m2();
		a = new B();
		a.m2();
         }
}

