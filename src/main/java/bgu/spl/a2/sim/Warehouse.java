package bgu.spl.a2.sim;

import java.util.Map;
import java.util.TreeMap;

/**
 * represents a warehouse that holds a finite amount of computers
 *  and their suspended mutexes.
 * 
 */
public class Warehouse {

    private Map<String,SuspendingMutex> computerMutex;


    /**
     *
     * @param type
     * @param failSig
     * @param succSig
     * @return
     */

    public boolean tryAddComputer(String type,long failSig,long succSig){
        if(computerMutex!=null&&computerMutex.containsKey(type))
            return false;
        SuspendingMutex mutex=new SuspendingMutex();
        //build the computer
        Computer newComp=new Computer(type);
        newComp.setSuccessSig(succSig);
        newComp.setFailSig(failSig);

        mutex.setComputer(newComp);
        if(computerMutex==null)
            computerMutex=new TreeMap<>();
        computerMutex.put(type,mutex);
        return true;
    }
    public SuspendingMutex getMutex(String Type){
        return computerMutex.get(Type);
    }
}
