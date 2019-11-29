class Program {
 
	public void run () {
      System.out.println("100");
		Integer i;
Integer n;
		i = 0;
		n = 10;
		Boolean b;
		b = false;
      do {
		n = n + 1;
         }while(true);
      assert( n == 11): "'repeat-until' statement with 'true' as expression'";
		Integer j;
		j = 0;
		Integer sum;
		sum = 0;
		n = 10;
      do {
		i = 0;
         do {
		i = i + 1;
		sum = sum + 1;
            }while(i >= n);
		j = j + 1;
         }while(j >= n);
      System.out.println(sum);
      assert( sum == 100): "Nested 'repeat-until' statement with two indexes";
   }
}

