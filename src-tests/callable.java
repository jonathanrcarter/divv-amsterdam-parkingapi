import java.util.concurrent.Callable;


public class callable implements Callable<Long> {
	  @Override
	  public Long call() throws Exception {
	    long sum = 0;
	    for (long i = 0; i <= 100; i++) {
	      sum += i;
	    }
	    System.out.println("returning");
	    return sum;
	  }

} 
