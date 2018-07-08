package bgu.spl.a2.sim;
import bgu.spl.a2.Promise;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

/**
 * 
 * this class is related to {@link Computer}
 * it indicates if a computer is free or not
 * 
 * Note: this class can be implemented without any synchronization.
 * However, using synchronization will be accepted as long as the implementation is blocking free.
 *
 */
public class SuspendingMutex {

	private Computer computer;
	private Queue<Promise<Computer>>promises;


	/**
	 * Computer acquisition procedure
	 * Note that this procedure is non-blocking and should return immediatly
	 * 
	 * @param computerType
	 * 					computer's type
	 * 
	 * @return a promise for the requested computer
	 */
	public Promise<Computer> down(String computerType){
		Promise<Computer> newPromise ;
		{
			newPromise = new Promise<>();
			synchronized (promises) {
				if (promises.isEmpty()) {
					newPromise.resolve(computer);
					promises.add(newPromise);
				} else
					promises.add(newPromise);
			}

		}
		return newPromise;
	}
	/**
	 * Computer return procedure
	 * releases a computer which becomes available in the warehouse upon completion
	 * 
	 * @param computer
	 */
	public void up(Computer computer){

			synchronized (promises) {
				promises.remove();
				if (!promises.isEmpty())
					promises.peek().resolve(computer);
			}
	}

	/**
	 *
	 * @param computer
	 */
	public void setComputer(Computer computer) {
		this.computer = computer;
		promises=new LinkedList<>();
	}
}
