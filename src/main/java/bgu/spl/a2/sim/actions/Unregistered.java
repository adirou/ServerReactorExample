package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.Promise;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

import java.util.LinkedList;
import java.util.Random;

/**
 * Created by USER on 12/9/2017.
 */
public class Unregistered extends Action<Boolean> {

    private String student;
    private String course;
    private Integer n;

    public Unregistered(String student, String course) {
        this.student = student;
        this.course = course;
    }

    @Override
    protected void start() {

        n= new Random().nextInt(300);
        CoursePrivateState corState=(CoursePrivateState)actorState;
        actorState.addRecord("Unregister"/*+" "+course+" "+student+"/"*/);
        if(corState.getRegStudents().contains(student)){
            corState.getRegStudents().remove(student);

            Action<Boolean> removeFromCourse=new Action<Boolean>() {
                @Override
                protected void start() {
                    actorState.addRecord("~remove from "+course+"/"+n);
                    StudentPrivateState studentState=(StudentPrivateState)actorState;
                    //todo check if exsits already
                    if(studentState.getGrades().containsKey(course)){
                        studentState.getGrades().remove(course);
                        complete(true);}
                    else
                        complete(false);

                }
            };

            sendMessage(removeFromCourse,student,new StudentPrivateState());


            LinkedList<Action<?>> actions=new LinkedList<>();
            actions.add(removeFromCourse);

            then(actions,()->{

                 corState.getRegStudents().remove(student);
                if(removeFromCourse.getResult().get()) {
                    actorState.addRecord("~remove from course "+course+"/"+n);
                    corState.setAvailableSpots(corState.getAvailableSpots() + 1);
                    corState.setRegistered(corState.getRegistered() - 1);
                }
                complete(true);

            });


        }
        else complete(false);
    }
}
