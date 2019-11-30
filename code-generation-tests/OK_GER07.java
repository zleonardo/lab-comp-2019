import java.io.*;
import java.util.Scanner;

public class OK_GER07 {
	public static void main(String [] args) {
		new Program().run();
	}
}
class A {
 
 void m () {
      System.out.println(0);
   }
}

class Program {
 
 void run () {
		A a;
         System.out.println("0");
		a = new A();
		a.m();
      }
}

