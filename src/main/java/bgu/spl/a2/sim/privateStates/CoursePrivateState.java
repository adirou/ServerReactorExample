package bgu.spl.a2.sim.privateStates;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import bgu.spl.a2.PrivateState;

/**
 * this class describe course's private state
 */

public class CoursePrivateState extends PrivateState implements Serializable {

	private Integer availableSpots;
	private Integer registered;
	private List<String> regStudents;
	private List<String> prequisites;
	
	/**
 	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 */
	public CoursePrivateState() {
		//TODO: replace method body with real implementation
		//throw new UnsupportedOperationException("Not Implemented Yet.");
		availableSpots=0;
		registered=0;
		regStudents= new LinkedList<>();
		prequisites= new LinkedList<>();
	}

	//mine
	public void setAvailableSpots(Integer availableSpots) {
		this.availableSpots = availableSpots;
	}

	public void setRegistered(Integer registered) {
		this.registered = registered;
	}

	public void setRegStudents(List<String> regStudents) {
		this.regStudents = regStudents;
	}

	public void setPrequisites(List<String> prequisites) {
		this.prequisites = prequisites;
	}
	//

	public Integer getAvailableSpots() {
		return availableSpots;
	}

	public Integer getRegistered() {
		return registered;
	}

	public List<String> getRegStudents() {
		return regStudents;
	}

	public List<String> getPrequisites() {
		return prequisites;
	}
}
