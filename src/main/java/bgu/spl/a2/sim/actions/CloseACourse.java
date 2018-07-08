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
public class CloseACourse extends Action<Boolean> {
    private String course;
    private String department;

    public CloseACourse(String course, String department) {
        this.course = course;
        this.department = department;
    }

    @Override
    protected void start() {
        DepartmentPrivateState depState = (DepartmentPrivateState) actorState;
        actorState.addRecord("Close Course");
        Action<Boolean> closeCourse = new Action<Boolean>() {
            @Override
            protected void start() {
                CoursePrivateState corState = (CoursePrivateState) actorState;
                actorState.addRecord("~close this Course");
                LinkedList<Action<?>> actionsCourse = new LinkedList<>();
                if (corState.getRegistered() != 0) {
                    for (String student : corState.getRegStudents()) {
                        Action<Boolean> removeStudentCourse = new Action<Boolean>() {
                            @Override
                            protected void start() {
                                actorState.addRecord("~remove course from student");
                                StudentPrivateState studentState = (StudentPrivateState) actorState;
                                studentState.getGrades().remove(course);
                                complete(true);
                            }
                        };

                        sendMessage(removeStudentCourse, student, new StudentPrivateState());
                        actionsCourse.add(removeStudentCourse);

                    }
                    then(actionsCourse, () -> {
                        corState.setRegistered(0);
                        corState.setAvailableSpots(-1);
                        complete(true);
                    });
                } else {
                    corState.setRegistered(0);
                    corState.setAvailableSpots(-1);
                    complete(true);
                }


            }

        };

        sendMessage(closeCourse, course, new CoursePrivateState());


        LinkedList<Action<?>> actions = new LinkedList<>();
        actions.add(closeCourse);

        then(actions, () -> {
            depState.getCourseList().remove(course);
            complete(true);
        });


    }
}
