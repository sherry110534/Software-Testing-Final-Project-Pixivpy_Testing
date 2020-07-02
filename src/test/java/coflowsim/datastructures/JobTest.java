package coflowsim.datastructures;

import coflowsim.utils.Constants;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

class JobTest {

    Job j = new Job("JOB-1", 1);
    MapTask mt = new MapTask("MAPPER-1", 1, j, 0, Constants.VALUE_IGNORED, new Machine(2));
    ReduceTask rt = new ReduceTask("REDUCER-1", 1, j, 0, Constants.VALUE_IGNORED, new Machine(2), 10, 10);

    @Test
    void addTask() {
        j.addTask(mt);
        assertEquals(1, j.numMappers);
        assertEquals(1, j.actualNumMappers);
        j.addTask(rt);
        assertEquals(1, j.numReducers);
        assertEquals(1, j.actualNumReducers);
    }

    @Test
    void onTaskSchedule() {
        rt.simulatedStartTime = 10;
        j.simulatedStartTime = 100;
        j.onTaskSchedule(rt);
        assertEquals(1, j.numActiveTasks);
        assertEquals(true, j.jobActive);
    }

    @Test
    void onTaskFinish() {
        rt.simulatedFinishTime = 10;
        j.simulatedFinishTime = 9;
        j.tasksInRacks = (Vector<ReduceTask>[]) new Vector[10];
        j.tasksInRacks[rt.taskID] = new Vector<ReduceTask>();
        j.numActiveTasks = 10;
        j.numReducers = 10;
        j.onTaskFinish(rt);
        assertEquals(10, j.simulatedFinishTime);
        assertEquals(9, j.numActiveTasks);
    }

    @Test
    void getSimulatedDuration() {
        j.simulatedFinishTime = 100;
        j.simulatedStartTime = 1;
        assertEquals(99, j.getSimulatedDuration());
    }

    @Test
    void calcShuffleBytesLeft() {
        j.shuffleBytesPerRack = new double[3];
        Arrays.fill(j.shuffleBytesPerRack, 10);
        assertEquals(30, j.calcShuffleBytesLeft());
    }

    @Test
    void decreaseShuffleBytesPerRack() {
        j.shuffleBytesPerRack = new double[10];
        Arrays.fill(j.shuffleBytesPerRack, 1000);
        j.decreaseShuffleBytesPerRack(0, 10);
        assertEquals(990, j.shuffleBytesPerRack[0]);
    }

    @Test
    void timeTillDeadline() {
        j.simulatedStartTime = 10;
        j.deadlineDuration = 100;
        assertEquals(100, j.timeTillDeadline(10));
    }
}