import java.io.*;
import java.util.Scanner;

class A {
 
 void m () {
		Integer a;
Integer b;
Integer c;
Integer d;
Integer e;
Integer f;
		Scanner s = new Scanner(System.in);
a = s.nextInt();
		b = s.nextInt();
		c = s.nextInt();
		d = s.nextInt();
		e = s.nextInt();
		f = s.nextInt();
      System.out.print(a);
      System.out.print(b);
      System.out.print(c);
      System.out.print(d);
      System.out.print(e);
      System.out.print(f);
   }
}

class Program {
 
 void run () {
		A a;
         System.out.println("");
         System.out.println("Ok-ger05");
         System.out.println("The output should be what you give as input.");
         System.out.println("Type in six numbers");
		a = new A();
		a.m();
      }
}

