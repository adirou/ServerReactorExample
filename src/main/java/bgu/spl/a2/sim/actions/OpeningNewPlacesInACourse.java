package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.Promise;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

import java.util.LinkedList;

/**
 * Created by USER on 12/9/2017.
 */
public class OpeningNewPlacesInACourse extends Action <Boolean> {
    private int number;
    private String course;

    public OpeningNewPlacesInACourse(int number, String course) {
        this.number = number;
        this.course = course;
    }

    @Override
    protected void start() {
        actorState.addRecord("Add Spaces");
        CoursePrivateState corState=(CoursePrivateState)actorState;
        if(corState.getAvailableSpots()!=-1)
              corState.setAvailableSpots(corState.getAvailableSpots()+number);
        complete(true);
    }
}
