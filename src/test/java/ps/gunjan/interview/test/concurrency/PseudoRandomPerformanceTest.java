package ps.gunjan.interview.test.concurrency;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import org.apache.log4j.Logger;
import org.junit.Test;

import ps.gunjan.interview.concurrency.AtomicPseudoRandom;
import ps.gunjan.interview.concurrency.ReentrantLockPseudoRandom;

public class PseudoRandomPerformanceTest {
	
	private static Logger logger = Logger.getLogger(PseudoRandomPerformanceTest.class);
	
	@Test
	public void testAtomicIntegerVsReentrantLockBasedPRNGs() throws InterruptedException, BrokenBarrierException {
		
		int numThreadsStartValue = 4;
		int numVariations = 10;
		
		final int upperBound = 10000;
		
		final ReentrantLockPseudoRandom reentrantLockPseudoRandom = new ReentrantLockPseudoRandom(17);
		final AtomicPseudoRandom atomicPseudoRandom = new AtomicPseudoRandom(17);
		
		long[] reentrantLockComputeTimes = new long[numVariations];
		
		for(int i=0; i < numVariations; i++){
			final CyclicBarrier barrier = new CyclicBarrier(1+(i+1)*numThreadsStartValue);
			
			long start = System.nanoTime();
			
			for (int j = 0; j < (i + 1) * numThreadsStartValue; j++) {
				
				new Thread(new Runnable(){
					
					@Override
					public void run(){
						reentrantLockPseudoRandom.nextInt(upperBound);
						try{
							barrier.await();
						} catch(InterruptedException | BrokenBarrierException e){
							e.printStackTrace();
						}
					}
				}).start();
			}
			
			barrier.await();
			long end = System.nanoTime();
			reentrantLockComputeTimes[i]=end-start;
			
		}
		
		System.out.println("ReentrantLock based times: \n " + display(reentrantLockComputeTimes, numThreadsStartValue));
		
	}
	
	private String display(long[] computeTimes, int numThreadsStartValue){
		StringBuilder builder = new StringBuilder();
		
		for(int i=0; i < computeTimes.length; i++){
			builder.append(((i+1)*numThreadsStartValue)+" threads "+ Thread.currentThread().getName()+"  "+ computeTimes[i]+ " ns \n");
		}
		return builder.toString();
		
	}
}