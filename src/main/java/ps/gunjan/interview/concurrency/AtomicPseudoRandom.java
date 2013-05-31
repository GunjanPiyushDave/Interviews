package ps.gunjan.interview.concurrency;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AtomicPseudoRandom implements PseudoRandom{
	
	private final Lock lock = new ReentrantLock(false);
	private AtomicInteger seed;
	
	public AtomicPseudoRandom(int seed){
		this.seed = new AtomicInteger(seed);
	}
	
	public int nextInt(int n){
		
		while(true){
			int s = seed.get();
			int ns = calculateNext(s);
			
			if(seed.compareAndSet(s, ns)){
				int remainder = s%n;
				return remainder > 0 ? remainder : remainder+n;
			}
		}
	}
	
	private int calculateNext(int s){
		return s*s+1;
	}
}