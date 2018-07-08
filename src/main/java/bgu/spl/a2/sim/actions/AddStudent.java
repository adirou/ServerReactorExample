package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.Promise;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

import java.util.LinkedList;

/**
 * Created by USER on 12/9/2017.
 */

/**
 * A class that represent an add student action that should submit to the department actor
 */
public class AddStudent extends Action<Boolean> {

    private String studentName;

    public AddStudent(String studentName) {
        this.studentName = studentName;
    }


    @Override
    protected void start() {

        DepartmentPrivateState depState=(DepartmentPrivateState)actorState;
        actorState.addRecord("Add Student");

        if(!(depState.getStudentList().contains(studentName))){
            Action<Boolean> buildStudent = new Action<Boolean>() {
                @Override
                protected void start() {
                    StudentPrivateState studentState=(StudentPrivateState)actorState;
                    actorState.addRecord("~build student");
                    complete(true);
                }
            };

            sendMessage(buildStudent,studentName,new StudentPrivateState());


            LinkedList<Action<?>> actions=new LinkedList<>();
            actions.add(buildStudent);

            then(actions,()->{
                depState.getStudentList().add(studentName);
                complete(true);
            });


        }
        else complete(false);
    }
}
