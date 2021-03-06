import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class testcalls {
	  private static final int NTHREDS = 10;

	  public static void main(String[] args) {

	    ExecutorService executor = Executors.newFixedThreadPool(NTHREDS);
	    List<Future<Long>> list = new ArrayList<Future<Long>>();
	    for (int i = 0; i < 20000; i++) {
	    	Callable<Long> worker = (Callable<Long>) new callable();
	      Future<Long> submit = executor.submit(worker);
	      list.add(submit);
	    }
	    long sum = 0;
	    System.out.println(list.size());
	    // now retrieve the result
	    for (Future<Long> future : list) {
	      try {
	    	  System.out.println("getting return");
	        sum += future.get();
	      } catch (InterruptedException e) {
	        e.printStackTrace();
	      } catch (ExecutionException e) {
	        e.printStackTrace();
	      }
	    }
	    System.out.println(sum);
	    executor.shutdown();
	  }

}
