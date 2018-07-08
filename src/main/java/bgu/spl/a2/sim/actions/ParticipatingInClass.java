package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.Promise;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by USER on 12/9/2017.
 */
public class ParticipatingInClass extends Action <Boolean>{

    private String student;
    private String course;
    private int grade;
    private Integer n;//

    public ParticipatingInClass(String student, String course, int grade) {
        this.student = student;
        this.course = course;
        this.grade = grade;
    }

    @Override
    protected void start() {
        n= new Random().nextInt(300);
        LinkedList<Action<?>> actions=new LinkedList<>();
        CoursePrivateState corState=(CoursePrivateState)actorState;
        actorState.addRecord("Participate In Course");
        if(corState.getAvailableSpots()>0&&!(corState.getRegStudents().contains(student))){

            //register the student , although maybe he isn't match
            corState.getRegStudents().add(student);
            corState.setAvailableSpots(corState.getAvailableSpots()-1);
            corState.setRegistered(corState.getRegistered() + 1);

            Action<Boolean> verifyStudent = new Action<Boolean>() {
                @Override
                protected void start() {
                    actorState.addRecord("~verify student match to "+course+"/"+n );
                    StudentPrivateState studentState=(StudentPrivateState)actorState;
                    boolean flag=true;
                    for (String preCourse:corState.getPrequisites()) {
                        actorState.addRecord("~ pre:"+preCourse +", "+studentState.getGrades().containsKey(preCourse));
                        if (!studentState.getGrades().containsKey(preCourse)) {
                            flag=false;
                            break;
                        }
                    }
                    if(flag)
                        studentState.getGrades().put(course,grade);


                    complete(flag);
                }
            };

            sendMessage(verifyStudent,student,new StudentPrivateState());



            actions.add(verifyStudent);

            then(actions,()->{

                if(verifyStudent.getResult().get()) {
                    actorState.addRecord("~reg success ~"+student+"/"+n);
                    if(!corState.getRegStudents().contains(student))
                         corState.getRegStudents().add(student);
                   // corState.setRegistered(corState.getRegistered() + 1);//
                    complete(true);
                }
                else{
                    actorState.addRecord("~reg fail ~"+student+"/"+n);
                    //remove the student ifn't match
                    corState.setAvailableSpots(corState.getAvailableSpots()+1);
                    corState.setRegistered(corState.getRegistered() - 1);
                    corState.getRegStudents().remove(student);

                    next(actions);
                }
            });


        }
        else next(actions);
    }
    //for Register with preffernces
    protected void next(List<Action<?>> actions){

        complete(false);
    }









  /*   LinkedList<Action<?>> actions = new LinkedList<>();
    CoursePrivateState corState = (CoursePrivateState) actorState;
                actorState.addRecord("Participate in course");
                if (corState.getAvailableSpots() > 0 && !(corState.getRegStudents().contains(student))) {
        corState.setAvailableSpots(corState.getAvailableSpots() - 1);
        Action<Boolean> verifyStudent = new Action<Boolean>() {
            @Override
            protected void start() {
                actorState.addRecord("verify student match");
                StudentPrivateState studentState = (StudentPrivateState) actorState;
                boolean flag = true;
                for (String preCourse : corState.getPrequisites()) {
                    if (!studentState.getGrades().containsKey(preCourse)) {
                        flag = false;
                        break;
                    }
                }
                if (flag)
                    studentState.getGrades().put(course, grade);

                complete(flag);
            }
        };

        sendMessage(verifyStudent, student, new StudentPrivateState());



        actions.add(verifyStudent);

        then(actions, () -> {
            if (verifyStudent.getResult().get()) {
                corState.getRegStudents().add(student);
                corState.setRegistered(corState.getRegistered() + 1);
                complete(true);
            } else {
                corState.setAvailableSpots(corState.getAvailableSpots() + 1);
                next(actions);

            }
        });
    } else next(actions);

}
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

*/
}
