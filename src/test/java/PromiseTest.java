package bgu.spl.a2;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
public class PromiseTest {

    public Promise<Integer> pr;

    @Before
    public void setUp() throws Exception {
        try{
            pr = new Promise<Integer>();
        }
        catch (Exception e){
            fail();
        }
    }
    @Test
    public void get() throws Exception {
            try {
                pr.get();
                fail();
            }
            catch (IllegalStateException c) {
                pr.resolve(5);
                assertTrue(pr.get() == 5);
            } catch (Exception e) {
                fail();
            }
    }

    @Test
    public void isResolved() throws Exception {
       assertFalse(pr.isResolved());
        pr.resolve(4);
        assertTrue(pr.isResolved());
    }

    @Test
    public void resolve() throws Exception {

        try {
            assertFalse(pr.isResolved());
            pr.resolve(5);
            assertTrue(pr.isResolved());
            pr.resolve(8);
            fail();
            ;
        }
        catch (IllegalStateException c) {
        }
        catch (Exception e) {
            fail();
        }
    }

    @Test
    public void subscribe() throws Exception {
        int oldSubscribed = pr.numOfSubscribed();
        if (!pr.isResolved()) {
            pr.subscribe(() -> {
                System.out.println("hhhh");
            });
            assertTrue(pr.numOfSubscribed() == oldSubscribed + 1);
        } else {
            assertTrue(oldSubscribed == 0);
            pr.subscribe(() -> {
            });
            assertTrue(pr.numOfSubscribed() == 0);
        }

    }

}
