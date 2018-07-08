package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.Promise;
import bgu.spl.a2.sim.Computer;
import bgu.spl.a2.sim.SuspendingMutex;
import bgu.spl.a2.sim.Warehouse;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by USER on 12/9/2017.
 */
public class CheckAdministrativeObligations extends Action<Boolean> {
    private String department;
    private List<String> students;
    private List<String> conditions;
    private String computer;
    private Warehouse warehouse;

    public CheckAdministrativeObligations(String department, List<String> students, List<String> conditions, String computer, Warehouse warehouse) {
        this.department = department;
        this.students = students;
        this.conditions = conditions;
        this.computer = computer;
        this.warehouse=warehouse;
    }

    @Override
    protected void start() {
        actorState.addRecord("Administrative Check");
        DepartmentPrivateState depState = (DepartmentPrivateState) actorState;
        if(!students.isEmpty()){
            SuspendingMutex mutex=warehouse.getMutex(computer);
            Promise<Computer> futureComputer= mutex.down(computer);
            futureComputer.subscribe(()->{
                Computer comp=futureComputer.get();
                LinkedList<Action<?>> actions = new LinkedList<>();

                ArrayList<HashMap<String,Integer>> cloneGrades=new ArrayList<>();

                for(String student: students) {
                    HashMap<String, Integer> copyGrades = new HashMap<>();
                    cloneGrades.add(copyGrades);
                    CloneGrade returnCopyGrades = new CloneGrade(copyGrades);
                    sendMessage(returnCopyGrades, student, new StudentPrivateState());
                    actions.add(returnCopyGrades);
                }

                then(actions,()-> {
                    actions.clear();
                    for (String student : students) {
                        long sign;

                        sign = comp.checkAndSign(conditions, cloneGrades.get(0));
                        cloneGrades.remove(0);

                        UpdateSignature updateStudent = new UpdateSignature(sign);
                        sendMessage(updateStudent, student, new StudentPrivateState());
                        actions.add(updateStudent);
                    }
                    mutex.up(comp);


                    then(actions,()-> complete(true));


                });

            });

        }
        else complete(false);
    }

    class UpdateSignature extends Action<Boolean>{
        private long sign;

        public UpdateSignature(long sign) {
            this.sign = sign;
        }

        @Override
        protected void start() {
            ((StudentPrivateState)actorState).setSignature(sign);
            complete(true);
        }
    }
    class CloneGrade extends Action<Boolean>{
        private HashMap<String,Integer> clone;

        public CloneGrade(HashMap<String, Integer> clone) {
            this.clone = clone;
        }

        @Override
        protected void start() {
            HashMap<String,Integer> old=((StudentPrivateState)actorState).getGrades();
            old.forEach((course,grade)->{
                clone.put(course,grade);
            });
            complete(true);
        }
    }

}
