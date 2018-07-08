package bgu.spl.a2.sim;

import com.google.gson.annotations.SerializedName;

/**
 * Created by USER on 12/16/2017.
 */
public class ComputerJson {
    public String Type;
    @SerializedName("Sig Success")
    public long SigSuccess;

    @SerializedName("Sig Fail")
    public long SigFail;

    public String getType() {
        return Type;
    }

    public long getSigSuccess() {
        return SigSuccess;
    }

    public long getSigFail() {
        return SigFail;
    }
}
