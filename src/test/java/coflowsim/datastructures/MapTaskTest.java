package coflowsim.datastructures;

import coflowsim.utils.Constants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapTaskTest {

    Job j = new Job("JOB-1", 1);
    MapTask mt = new MapTask("MAPPER-1", 1, j, 0, Constants.VALUE_IGNORED, new Machine(2));

    @Test
    void startTask() {
        mt.startTask(10);
        assertEquals(10, mt.simulatedStartTime);
    }

    @Test
    void cleanupTask() {
        mt.cleanupTask(100);
        assertEquals(100, mt.simulatedFinishTime);
    }

    @Test
    void getPlacement() {
        assertEquals(2, mt.getPlacement());
    }

    @Test
    void hasStarted() {
        mt.startTask(10);
        assertEquals(true, mt.hasStarted());
    }

    @Test
    void isCompleted() {
        mt.cleanupTask(100);
        assertEquals(true, mt.isCompleted());
    }

    @Test
    void resetTaskStates() {
        mt.resetTaskStates();
        assertEquals(Constants.VALUE_UNKNOWN, mt.simulatedStartTime);
        assertEquals(Constants.VALUE_UNKNOWN, mt.simulatedFinishTime);
    }
}