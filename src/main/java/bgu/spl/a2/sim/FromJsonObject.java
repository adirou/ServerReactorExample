package bgu.spl.a2.sim;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Created by USER on 12/15/2017.
 */

/**
 *
 */
public class FromJsonObject {
    public int threads;

    public ComputerJson[] Computers;

    @SerializedName("Phase 1")
    public ActionJson[] Phase_1;
    @SerializedName("Phase 2")
    public ActionJson[] Phase_2;
    @SerializedName("Phase 3")
    public ActionJson[] Phase_3;

    public int getThreads() {
        return threads;
    }

    public ComputerJson[] getComputers() {
        return Computers;
    }

    public ActionJson[] getPhase1() {
        return Phase_1;
    }

    public ActionJson[] getPhase2() {
        return Phase_2;
    }

    public ActionJson[] getPhase3() {
        return Phase_3;
    }
}

