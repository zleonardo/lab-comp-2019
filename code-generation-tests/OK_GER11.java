import java.io.*;
import java.util.Scanner;

public class OK_GER11 {
	public static void main(String [] args) {
		new Program().run();
	}
}
class A {
 
 void m1 () {
      System.out.print(" 2 ");
   }
 void m2 (int n) {
      System.out.print(n + " ");
		this.m1   }
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
		a.m2(1);
		a = new B();
		a.m2(3);
         }
}

