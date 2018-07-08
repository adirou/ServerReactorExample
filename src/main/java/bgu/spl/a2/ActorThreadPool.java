package bgu.spl.a2;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * represents an actor thread pool - to understand what this class does please
 * refer to your assignment.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */
public class ActorThreadPool {

	private Map<String,Queue<Action>> pool;
	private Map<String,PrivateState> states;
	private Map<String,Lock> locks ;

	private Thread[] threads;

	private VersionMonitor vm;
	/**
	 * creates a {@link ActorThreadPool} which has nthreads. Note, threads
	 * should not get started until calling to the {@link #start()} method.
	 *
	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 *
	 * @param nthreads
	 *            the number of threads that should be started by this thread
	 *            pool
	 */
	public ActorThreadPool(int nthreads) {
		vm=new VersionMonitor();

		pool = new ConcurrentHashMap<>();
		states= new ConcurrentHashMap<>();
		locks= new ConcurrentHashMap<>();
		threads=new Thread[nthreads];

		for(int i=0;i<nthreads;i++){
			threads[i]=new Thread(()-> {
				//loop event- until the thread has been interrupted
				while (!Thread.currentThread().isInterrupted()) {
					try {
						int last = vm.getVersion();

						//goes over all the actors' queue and search for action and commit
						pool.forEach((actor_id, queue) -> {

							Lock lock = locks.get(actor_id);
							if (!queue.isEmpty() && lock.tryLock()) {
								if (!queue.isEmpty()) {//another check to verify that the queue wasn't empty while try lock
									queue.remove().handle(this, actor_id, states.get(actor_id));
									lock.unlock();
									vm.inc();
								} else
									lock.unlock();

							}
						});
						//if nothing happened meanwhile, wait to notify
						vm.await(last);
					} catch (InterruptedException ie) {
						Thread.currentThread().interrupt();
					}
				}

			});
		}

	}

	/**
	 * submits an action into an actor to be executed by a thread belongs to
	 * this thread pool
	 *
	 * @param action
	 *            the action to execute
	 * @param actorId
	 *            corresponding actor's id
	 * @param actorState
	 *            actor's private state (actor's information)
	 */
	public void  submit(Action<?> action, String actorId, PrivateState actorState) {
		//create new actor in the system if not exists
		if (!pool.containsKey(actorId)) {
			states.put(actorId, actorState);
			locks.put(actorId, new ReentrantLock());
			pool.put(actorId, new ConcurrentLinkedQueue<>());
		}
		//submit the action
		pool.get(actorId).add(action);

		//notify the pool has changed
		vm.inc();
	}

	/**
	 * closes the thread pool - this method interrupts all the threads and waits
	 * for them to stop - it is returns *only* when there are no live threads in
	 * the queue.
	 *
	 * after calling this method - one should not use the queue anymore.
	 *
	 * @throws InterruptedException
	 *             if the thread that shut down the threads is interrupted
	 */
	public void shutdown() throws InterruptedException {
		//stop the threads by interrupt
		for(Thread t : threads)
			t.interrupt();
		//wait for them to stop
		for(Thread t : threads)
			t.join();

	}

	/**
	 * start the threads belongs to this thread pool
	 */
	public void start() {

		for(Thread t : threads)
			t.start();

	}

	/**
	 * getter for actors
	 * @return actors
	 */
	public Map<String, PrivateState> getActors(){

		return states;
	}

	/**
	 * getter for actor's private state
	 * @param actorId actor's id
	 * @return actor's private state
	 */
	public PrivateState getPrivateState(String actorId){

			return states.getOrDefault(actorId,null);

	}



}
