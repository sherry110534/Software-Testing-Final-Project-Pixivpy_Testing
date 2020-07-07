package coflowsim.traceproducers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoflowBenchmarkTraceProducerTest {

    CoflowBenchmarkTraceProducer cbtp = new CoflowBenchmarkTraceProducer("C:\\Users\\DU JIN\\Desktop\\課程資料\\code\\coflowsim\\src\\test\\resources\\traceProducerTestCase.txt");

    @Test
    void prepareTrace() {
        cbtp.prepareTrace();
        assertEquals(3, cbtp.numJobs);
        assertEquals(3, cbtp.jobs.size());
    }

    @Test
    void getNumRacks() {
        cbtp.prepareTrace();
        assertEquals(20, cbtp.getNumRacks());
    }

    @Test
    void getMachinesPerRack() {
        cbtp.prepareTrace();
        assertEquals(1, cbtp.getMachinesPerRack());
    }
}