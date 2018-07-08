package bgu.spl.a2.sim;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Condition;

/**
 * Created by USER on 12/15/2017.
 */
public class ActionJson {
    @SerializedName("Action")
    public String ActionName;

    public String Department;
    public String Course;
    public int Space;
    public String[] Prerequisites;
    public String Student;
    public String[] Students;
    public String[] Grade;
    public int Number;
    public String[] Preferences;
    public String Computer;
    public String[] Conditions;

    public String getActionName() {
        return ActionName;
    }

    public String getDepartment() {
        return Department;
    }

    public String getCourse() {
        return Course;
    }

    public int getSpace() {
        return Space;
    }

    public List<String> getPrerequisites() {
        return new ArrayList<String>(Arrays.asList( Prerequisites));
    }

    public String getStudent() {
        return Student;
    }

    public int getGrade() {
       if(Grade[0].equals("-"))
           return -1;
       return  Integer.parseInt(Grade[0]);
    }
    public List<Integer> getGrades() {
        Integer[] grades=new Integer[Grade.length];
        for (int i =0;i<Grade.length;i++)
            grades[i]=Grade[0].equals("-")?-1:Integer.parseInt(Grade[0]);
        return new ArrayList<Integer>(Arrays.asList(grades));
    }

    public int getNumber() {
        return Number;
    }

    public List<String> getPreferences() {

        return new ArrayList<String>(Arrays.asList(Preferences));
    }

    public String getComputer() {
        return Computer;
    }

    public List<String> getConditions(){
        return new ArrayList<String>(Arrays.asList( Conditions));
    }

    public List<String> getStudents(){
        return new ArrayList<String>(Arrays.asList( Students));
    }
}
