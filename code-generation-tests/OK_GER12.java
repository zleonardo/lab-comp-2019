class A {
 
 void m1 () {
      System.out.print(1 + " ");
   }
 void m2 (Integer n) {
      System.out.print(n + " ");
   }
}

class B extends A {
 
      @Override
	 void m2 (Integer n) {
         System.out.print(n + " ");
		m2:      }
}

class C extends B {
 
         @Override
	 void m1 () {
		m1            System.out.print(2 + " ");
         }
 void m3 () {
		m1            System.out.print(1 + " ");
            System.out.print(2 + " ");
         }
}

class Program {
 
 void run () {
		A a;
		B b;
		C c;
               System.out.println("1 2 1 2 1 2 1 2");
		b = new B();
		b.m2();
		c = new C();
		c.m1();
		c.m3();
            }
}

