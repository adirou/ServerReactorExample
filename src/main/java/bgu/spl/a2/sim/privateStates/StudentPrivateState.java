package bgu.spl.a2.sim.privateStates;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import bgu.spl.a2.PrivateState;

/**
 * this class describe student private state
 */
public class StudentPrivateState extends PrivateState implements Serializable {

	private HashMap<String, Integer> grades;
	private long signature;

	
	/**
 	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 */
	public StudentPrivateState() {
		grades =new HashMap<>();
	}


	public void setGrades(HashMap<String, Integer> grades) {
		this.grades = grades;
	}

	public void setSignature(long signature) {
		this.signature = signature;
	}

	public HashMap<String, Integer> getGrades() {
		return grades;
	}

	public long getSignature() {
		return signature;
	}
}