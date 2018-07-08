/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.a2.sim;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import bgu.spl.a2.Action;
import bgu.spl.a2.ActorThreadPool;
import bgu.spl.a2.PrivateState;
import bgu.spl.a2.sim.actions.*;

import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import sun.nio.ch.IOUtil;


/**
 * A class describing the simulator for part 2 of the assignment
 */
public class Simulator {
	private static  FromJsonObject data;
	private static Warehouse warehouse;
	private static ActorThreadPool actorThreadPool;

	private static CountDownLatch counter;
	//private static AtomicInteger ai;//

	//private static List<Action> actions;//
	//private static int[] actionsSymbol;//
	//private static int i;//

	public static void main(String [] args){

		setData(fromJson(args[0]));
		attachActorThreadPool(new ActorThreadPool(data.getThreads()));
		start();
	}

	/**
	* Begin the simulation Should not be called before attachActorThreadPool()
	*/
    public static void start(){

		warehouse = new Warehouse();
		for (ComputerJson cj:data.getComputers())
			warehouse.tryAddComputer(cj.getType(),cj.getSigFail(),cj.getSigSuccess());

		actorThreadPool.start();
		phaseHandling(data.getPhase1());
		phaseHandling(data.getPhase2());
		phaseHandling(data.getPhase3());

		end();
    }

	private static void phaseHandling(ActionJson[] actionsToSubmit) {
			counter = new CountDownLatch(actionsToSubmit.length);
			for (ActionJson act : actionsToSubmit)
				submitAction(act);
			try {
				counter.await();
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
	}

	/**
	 * an action which decipher the action and it's parameters, submits, and take care for the counter
	 * @param AJ
	 */
	private static void  submitAction(ActionJson AJ ){
		Action<?>actionToSubmit;
		actionToSubmit=null;
		switch (AJ.getActionName()){
			case "Open Course":
				actionToSubmit= new OpenANewCourse(AJ.getSpace(), AJ.getCourse(), AJ.getPrerequisites());
				actorThreadPool.submit(actionToSubmit,AJ.getDepartment(),new DepartmentPrivateState());
				break;

			case "Add Student":
				actionToSubmit=new AddStudent(AJ.getStudent());
				actorThreadPool.submit(actionToSubmit,AJ.getDepartment(),new DepartmentPrivateState());
				break;

			case "Participate In Course":
				actionToSubmit = new ParticipatingInClass(AJ.getStudent(),
						AJ.getCourse(), AJ.getGrade());
				actorThreadPool.submit(actionToSubmit,AJ.getCourse(),new CoursePrivateState());
				break;

			case "Add Spaces":
				actionToSubmit=new OpeningNewPlacesInACourse(AJ.getNumber(),AJ.getCourse());
				actorThreadPool.submit(actionToSubmit,AJ.getCourse(),new CoursePrivateState());
				break;

			case "Unregister":
				actionToSubmit=new Unregistered(AJ.getStudent(),AJ.getCourse());
				actorThreadPool.submit(actionToSubmit,AJ.getCourse(),new CoursePrivateState());
				break;

			case "Close Course":
				actionToSubmit=new CloseACourse(AJ.getCourse(),AJ.getDepartment());
				actorThreadPool.submit(actionToSubmit,AJ.getDepartment(),new DepartmentPrivateState());
				break;

			case "Register With Preferences":
				actionToSubmit=new RegisterWithPreferences(AJ.getStudent(),AJ.getPreferences(),AJ.getGrades());
				actorThreadPool.submit(actionToSubmit,AJ.getStudent(),new StudentPrivateState());
				break;

			case "Administrative Check":
				actionToSubmit=new CheckAdministrativeObligations(AJ.getDepartment(), AJ.getStudents(),AJ.getConditions(),AJ.getComputer(),warehouse);
				actorThreadPool.submit(actionToSubmit,AJ.getDepartment(),new DepartmentPrivateState());
				break;


		}
		if(actionToSubmit!=null) {
			actionToSubmit.getResult().subscribe(() -> {
				counter.countDown();
			});
		}
		else
			counter.countDown();

	}



	
	/**
	* shut down the simulation
	* returns list of private states
	*/
	public static HashMap<String,PrivateState> end() {
		try {
			actorThreadPool.shutdown();
		} catch (InterruptedException ie) {

		}

		HashMap<String, PrivateState> simulationResult = new HashMap<>(actorThreadPool.getActors());
		try {
			FileOutputStream fOut = new FileOutputStream("result.ser");
			ObjectOutputStream oOut = new ObjectOutputStream(fOut);
			oOut.writeObject(simulationResult);

		} catch (FileNotFoundException fnfe) {

		} catch (IOException ioe) {

		}
		return simulationResult;
	}

	/**
	 *
	 * @param jsonNameFile
	 * @return
	 */
	private static FromJsonObject fromJson(String jsonNameFile ){
		Gson gson = new Gson();
		JsonParser jsonParser= new JsonParser();
		try {
			ClassLoader classLoader =Simulator.class.getClassLoader();
			InputStream is = classLoader.getResourceAsStream(jsonNameFile);

			JsonObject js=(JsonObject) jsonParser.parse(new InputStreamReader(is));
			FromJsonObject object = gson.fromJson(js,FromJsonObject.class);
			return object;
		}
		catch (Exception e){
			throw new IllegalArgumentException();
		}
	}




	private static void setData(FromJsonObject data) {
		Simulator.data = data;
	}


	/**
	 * attach an ActorThreadPool to the Simulator, this ActorThreadPool will be used to run the simulation
	 *
	 * @param myActorThreadPool - the ActorThreadPool which will be used by the simulator
	 */
	public static void attachActorThreadPool(ActorThreadPool myActorThreadPool){
		actorThreadPool=myActorThreadPool;
	}
}
