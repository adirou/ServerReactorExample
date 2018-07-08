package bgu.spl.a2;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class VersionMonitorTest {
    public VersionMonitor m;

    @Before
    public void setUp() throws Exception {
        try{
            m= new VersionMonitor();
        }
        catch (Exception e){
            fail();
        }
    }

    @Test
    public void getVersion() throws Exception {
        assertTrue(m.getVersion()>=0);
    }

    @Test
    public void inc() throws Exception {
        assertTrue(m.getVersion()>=0);
        int last = m.getVersion();
        m.inc();
        assertTrue(m.getVersion()== last+1);
    }

    @Test
    public void await() throws Exception {
        m.inc();
        Thread thread = new Thread(new runM(m));
        thread.start();
        Thread.sleep(3000);
        if(!(thread.getState()==Thread.State.WAITING))
            fail();
        m.inc();
        Thread.sleep(3000);
        if(!(thread.getState()==Thread.State.WAITING))
            fail();
        m.inc();
        Thread.sleep(3000);
        if((thread.getState()==Thread.State.WAITING))
            fail();
    }
}

class runM implements Runnable{
     VersionMonitor m;

    public runM(VersionMonitor m) {
        this.m = m;
    }

    @Override
    public void run() {
        try{
        m.await(2);}
        catch (InterruptedException iE){
        }
    }
}