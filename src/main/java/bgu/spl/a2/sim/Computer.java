package bgu.spl.a2.sim;

import java.util.List;
import java.util.Map;

public class Computer {

	private String computerType;
	private long failSig;
	private long successSig;


	public Computer(String computerType) {this.computerType = computerType;}

	public void setFailSig(long failSig) {
		this.failSig = failSig;
	}

	public void setSuccessSig(long successSig) {
		this.successSig = successSig;
	}

	/**
	 * this method checks if the courses' grades fulfill the conditions
	 * @param courses
	 * 							courses that should be pass
	 * @param coursesGrades
	 * 							courses' grade
	 * @return a signature if couersesGrades grades meet the conditions
	 */
	public long checkAndSign(List<String> courses, Map<String, Integer> coursesGrades){

	//	if(coursesGrades==null)
	//		throw new IllegalStateException();
		for (String course : courses) {
			if(coursesGrades.containsKey(course)&&coursesGrades.get(course)==null)
				throw new IllegalStateException();
			if (!coursesGrades.containsKey(course)||coursesGrades.get(course)<56)
				return failSig;
		}
		return successSig;
	}

	public String getComputerType() {
		return computerType;
	}

	public long getFailSig() {
		return failSig;
	}

	public long getSuccessSig() {
		return successSig;
	}
}
