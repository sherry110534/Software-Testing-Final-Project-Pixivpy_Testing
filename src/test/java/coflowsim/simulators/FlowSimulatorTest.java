package coflowsim.simulators;

import coflowsim.datastructures.Flow;
import coflowsim.datastructures.Job;
import coflowsim.datastructures.ReduceTask;
import coflowsim.datastructures.Task;
import coflowsim.utils.Constants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import coflowsim.traceproducers.TraceProducer;
import coflowsim.traceproducers.CoflowBenchmarkTraceProducer;

import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

class FlowSimulatorTest {

    // Create TraceProducer
    TraceProducer traceProducer1 = null;
    TraceProducer traceProducer2 = null;
    Simulator nlpl = null;
    Constants.SHARING_ALGO sharingAlgo = Constants.SHARING_ALGO.FAIR;
    double deadlineMultRandomFactor = 1;
    boolean isOffline = false;
    boolean considerDeadline = false;
    int simulationTimestep = 10 * Constants.SIMULATION_SECOND_MILLIS;
    int CURRENT_TIME = 0;


    @BeforeEach
    void setUp() {
        String pathToCoflowBenchmarkTraceFile1 = "C:\\Users\\David Hwang\\Desktop\\碩一course\\Software Testing\\Software-Testing-Final-Project-CoflowSimTesting\\src\\test\\resources\\FB2010-1Hr-150-0.txt";
        traceProducer1 = new CoflowBenchmarkTraceProducer(pathToCoflowBenchmarkTraceFile1);
        traceProducer1.prepareTrace();

        String pathToCoflowBenchmarkTraceFile2 = "src/test/resources/onScheduleTestCase.txt";
        traceProducer2 = new CoflowBenchmarkTraceProducer(pathToCoflowBenchmarkTraceFile2);
        traceProducer2.prepareTrace();

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

        //nlpl.simulate(simulationTimestep);
        nlpl = new FlowSimulator(sharingAlgo, traceProducer1, isOffline, considerDeadline,
                deadlineMultRandomFactor);
        for(int i=0; i<traceProducer1.jobs.size(); i++){
            Job j = traceProducer1.jobs.elementAt(i);
            nlpl.uponJobAdmission(j);
            for (Task r : j.tasks) {
                if (r.taskType != Task.TaskType.REDUCER) {
                    continue;
                }
                ReduceTask rt = (ReduceTask) r;
                int toRack = rt.taskID;
                int ExpectedNumFlows = rt.flows.size();
                if(rt.flows.size() > Constants.MAX_CONCURRENT_FLOWS){
                    ExpectedNumFlows = Constants.MAX_CONCURRENT_FLOWS;
                }
                assertEquals(ExpectedNumFlows,nlpl.flowsInRacks[toRack].size());
            }
        }
        //nlpl.uponJobAdmission(traceProducer.jobs.elementAt(3));
        //uponJobAdmission();

    }

    @Test
    void onSchedule() {
        nlpl = new FlowSimulator(sharingAlgo, traceProducer2, isOffline, considerDeadline,
                deadlineMultRandomFactor);
        for(int i=0; i<traceProducer2.jobs.size(); i++){
            Job j = traceProducer2.jobs.elementAt(i);
            j.wasAdmitted = true;
            nlpl.uponJobAdmission(j);
        }
        double[] rack_flow_total_bytes = new double[nlpl.NUM_RACKS];
        int[] rack_num_flow = new int[nlpl.NUM_RACKS];
        for(int i = 0 ; i< nlpl.NUM_RACKS; i++){
            rack_num_flow[i] = nlpl.flowsInRacks[i].size();
            for(int j = 0 ; j < nlpl.flowsInRacks[i].size(); j++){
                rack_flow_total_bytes[i] += nlpl.flowsInRacks[i].elementAt(j).getFlowSize();
            }
            System.out.print(nlpl.flowsInRacks[i].size()+" ");
        }
        System.out.print("\n\n");
        int EPOCH_IN_MILLIS = simulationTimestep;
        double bytesPerTask = Constants.RACK_BYTES_PER_SEC * (1.0 * Constants.SIMULATION_QUANTA / Constants.SIMULATION_SECOND_MILLIS);
        for (long i = 0; i < EPOCH_IN_MILLIS; i += Constants.SIMULATION_QUANTA) {
            long curTime = CURRENT_TIME + i;
            nlpl.onSchedule(curTime);
            for(int j =0; j< nlpl.NUM_RACKS; j++){
                if(rack_flow_total_bytes[j] > 0)
                    rack_flow_total_bytes[j] -= bytesPerTask;
                if(rack_flow_total_bytes[j]<=0) {
                    assertEquals(0, nlpl.flowsInRacks[j].size());
                }
                else {
                    assertEquals(rack_num_flow[j], nlpl.flowsInRacks[j].size());
                }
                System.out.print(nlpl.flowsInRacks[j].size()+" ");
            }
            System.out.print("\n");

        }

    }

    @Test
    void removeDeadJob() {

    }
}