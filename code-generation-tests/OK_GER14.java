class A {
 k
 
 Integer get_A () {
 return k;
   }
 void init () {
		k = 1;
   }
}

class B extends A {
 k
 
 Integer get_B () {
 return k;
      }
      @Override
	 void init () {
		init		k = 2;
      }
}

class C extends B {
 k
 
 Integer get_C () {
 return k;
         }
         @Override
	 void init () {
		init		k = 3;
         }
}

class D extends C {
 k
 
 Integer get_D () {
 return k;
            }
            @Override
	 void init () {
		init		k = 4;
            }
}

