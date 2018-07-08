package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

import javax.swing.text.StyledEditorKit;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by USER on 12/17/2017.
 */
public class RegisterWithPreferences extends Action<Boolean> {
    private String student;
    private List<String> courses;
    private List<Integer> grades;

    public RegisterWithPreferences(String student, List<String> courses, List<Integer> grades) {
        this.student = student;
        this.courses = courses;
        this.grades = grades;
    }
    @Override
    protected void start() {
        actorState.addRecord("Register With Preferences");
        LinkedList<Action<?>> actionsStudent = new LinkedList<>();
        if (!courses.isEmpty()) {
            Action<Boolean> tryRegister=generateAction(courses.get(0), grades.get(0));
            sendMessage(tryRegister,courses.get(0),new CoursePrivateState());
            actionsStudent.add(tryRegister);
            then(actionsStudent,()->{
                complete(tryRegister.getResult().get());
            });

        }
        else
            complete(false);
    }
    private Action<Boolean> generateAction(String course, int grade)
    {
        actorState.addRecord("try Register With Preferences to "+course);
        return new registerToClass(student,course,grade,courses,grades);
    }





    class registerToClass extends ParticipatingInClass{
        private List<String> courses;
        private List<Integer> grades;


        public registerToClass(String student, String course, int grade, List<String> courses, List<Integer> grades) {
            super(student, course, grade);
            this.courses = courses;
            this.grades = grades;
        }

        @Override
        public void next(List<Action<?>> actions){
            courses.remove(0);
            grades.remove(0);
            if (!courses.isEmpty()) {
                Action<Boolean> tryReg = generateAction(courses.get(0), grades.get(0));
                actions.clear();
                actions.add(tryReg);
                sendMessage(tryReg, courses.get(0), new CoursePrivateState());
                then(actions, () -> {
                    complete(tryReg.getResult().get());
                });
            }
            else
                complete(false);
        }
    }

}
