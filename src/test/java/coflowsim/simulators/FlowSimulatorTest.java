package coflowsim.simulators;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import coflowsim.traceproducers.TraceProducer;
import coflowsim.traceproducers.CoflowBenchmarkTraceProducer;

import static org.junit.jupiter.api.Assertions.*;

class FlowSimulatorTest {

    // Create TraceProducer
    TraceProducer traceProducer = null;

    @BeforeEach
    void setUp() {
        String pathToCoflowBenchmarkTraceFile = "../../resources/FB2010-1Hr-150-0.txt";
        traceProducer = new CoflowBenchmarkTraceProducer(pathToCoflowBenchmarkTraceFile);
        traceProducer.prepareTrace();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void initialize() {
    }

    @Test
    void ignoreThisJob() {
    }

    @Test
    void admitThisJob() {
    }

    @Test
    void simulate() {
    }

    @Test
    void afterJobAdmission() {
    }

    @Test
    void afterJobDeparture() {
    }

    @Test
    void printStats() {
    }

    @Test
    void incNumActiveTasks() {
    }

    @Test
    void decNumActiveTasks() {
    }

    @Test
    void uponJobAdmission() {
    }

    @Test
    void onSchedule() {
    }

    @Test
    void removeDeadJob() {
    }
}