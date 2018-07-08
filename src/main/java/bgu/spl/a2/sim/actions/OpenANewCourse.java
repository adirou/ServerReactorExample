package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.Promise;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by USER on 12/9/2017.
 */
public class OpenANewCourse extends Action<Boolean> {
    private int availableSpots;
    private List<String> prerequisites;
    private String courseName;


    public OpenANewCourse(int availableSpots, String courseName, List<String> prerequisites) {
        this.availableSpots = availableSpots;
        this.prerequisites = prerequisites;
        this.courseName = courseName;

    }

    @Override
    protected void start() {
        //todo if its possible to use protected actorState

        DepartmentPrivateState depState = (DepartmentPrivateState) actorState;
        actorState.addRecord("Open Course");
        if (!(depState.getCourseList().contains(courseName))) {
            //depState.getCourseList().add(courseName);
            Action<Boolean> buildCourse = new Action<Boolean>() {
                @Override
                protected void start() {
                    actorState.addRecord("~build course");
                    CoursePrivateState courseState = (CoursePrivateState) actorState;
                    //todo check if exsits already
                    courseState.setAvailableSpots(availableSpots);
                    courseState.setPrequisites(prerequisites);
                    complete(true);
                }
            };

            sendMessage(buildCourse, courseName, new CoursePrivateState());


            LinkedList<Action<?>> actions = new LinkedList<>();
            actions.add(buildCourse);

            then(actions, () -> {
                depState.getCourseList().add(courseName);
                complete(true);
            });


        } else complete(false);

    }
}
