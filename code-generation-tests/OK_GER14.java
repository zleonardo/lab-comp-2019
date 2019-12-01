import java.io.*;
import java.util.Scanner;

public class OK_GER14 {
	public static void main(String [] args) {
		new Program().run();
	}
}
class A {
int k;
 
 int get_A () {
 return this.k;
   }
 void init () {
		Scanner s = new Scanner(System.in);
this.k = 1;
   }
}

class B extends A {
int k;
 
 int get_B () {
 return this.k;
      }
      @Override
 void init () {
		super.init		this.k = 2;
      }
}

class C extends B {
int k;
 
 int get_C () {
 return this.k;
         }
         @Override
 void init () {
		super.init		this.k = 3;
         }
}

class D extends C {
int k;
 
 int get_D () {
 return this.k;
            }
            @Override
 void init () {
		super.init		this.k = 4;
            }
}

