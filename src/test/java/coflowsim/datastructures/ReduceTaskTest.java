package coflowsim.datastructures;

import coflowsim.utils.Constants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReduceTaskTest {

    Job j = new Job("JOB-1", 1);
    MapTask mt = new MapTask("MAPPER-1", 1, j, 0, Constants.VALUE_IGNORED, new Machine(2));
    ReduceTask rt = new ReduceTask("REDUCER-1", 1, j, 0, Constants.VALUE_IGNORED, new Machine(2), 10, 10);

    @Test
    void createFlows() {
        j.numMappers = 10;
        j.addTask(mt);
        rt.createFlows(false, 0);
        assertEquals(1, rt.flows.size());
        assertEquals(1048576, rt.flows.elementAt(0).getFlowSize());
    }

    @Test
    void roundToNearestNMB() {
        rt.roundToNearestNMB(1);
        assertEquals(1048576.0, rt.shuffleBytes);
        assertEquals(1048576.0, rt.shuffleBytesLeft);
    }

    @Test
    void resetTaskStates() {
        rt.shuffleBytesLeft = 111;
        rt.resetTaskStates();
        assertEquals(rt.shuffleBytes, rt.shuffleBytesLeft);
    }

    @Test
    void cleanupTask() {
        rt.cleanupTask(10);
        assertEquals(0.0, rt.shuffleBytesLeft);
    }
}