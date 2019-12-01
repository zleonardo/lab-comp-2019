import java.io.*;
import java.util.Scanner;

public class OK_GER16 {
	public static void main(String [] args) {
		new Program().run();
	}
}
class A {
int k;
 
	public int get_A () {
 return this.k;
   }
	public void set (int k) {
		this.k = k;
   }
	public void print () {
      System.out.print(" ");
   }
	public void init () {
		this.set(0);
   }
}

class B extends A {
int k;
 
	public int get_B () {
 return this.k;
      }
      @Override
 void init () {
		super.init		this.k = 2;
      }
      @Override
 void print () {
         System.out.print(" ");
         System.out.print(" ");
		super.print      }
}

class C extends A {
 
         @Override
 int get_A () {
 return 0;
         }
}

