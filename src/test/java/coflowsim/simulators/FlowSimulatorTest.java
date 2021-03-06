package coflowsim.simulators;

import coflowsim.datastructures.*;
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
        String pathToCoflowBenchmarkTraceFile1 = "src/test/resources/FB2010-1Hr-150-0.txt";
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
        nlpl = new FlowSimulator(sharingAlgo, traceProducer1, isOffline, considerDeadline,
                deadlineMultRandomFactor);
        traceProducer1.jobs.sortByStartTime();
        assertEquals(traceProducer1.jobs, nlpl.jobs);
    }

    @Test
    void ignoreThisJob() {
        nlpl = new FlowSimulator(sharingAlgo, traceProducer1, isOffline, considerDeadline,
                deadlineMultRandomFactor);
        Job test_job = new Job("test_job", 1);
        assertEquals(true, nlpl.ignoreThisJob(test_job));
        test_job.numMappers=1;
        assertEquals(false, nlpl.ignoreThisJob(test_job));
    }

    @Test
    void admitThisJob() {
        nlpl = new FlowSimulator(sharingAlgo, traceProducer1, isOffline, considerDeadline,
                deadlineMultRandomFactor);
        Job test_job = new Job("test_job", 1);
        assertEquals(true, nlpl.admitThisJob(test_job));

    }

    @Test
    void simulate() {
        int simulationTimestep = Constants.SIMULATION_SECOND_MILLIS;
        nlpl = new FlowSimulator(sharingAlgo, traceProducer1, isOffline, considerDeadline,
                deadlineMultRandomFactor);
        nlpl.simulate(simulationTimestep);
        assertEquals(0, nlpl.activeJobs.size());
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
            //System.out.print(nlpl.flowsInRacks[i].size()+" ");
        }
        //System.out.print("\n\n");
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
                //System.out.print(nlpl.flowsInRacks[j].size()+" ");
            }
            //System.out.print("\n");

        }

    }

    @Test
    void removeDeadJob() {
        nlpl = new FlowSimulator(sharingAlgo, traceProducer2, isOffline, considerDeadline,
                deadlineMultRandomFactor);
        Job j = new Job("JOB-1", 1);
        nlpl.activeJobs.put("JOB-1", j);
        assertEquals(1, nlpl.activeJobs.size());
        nlpl.removeDeadJob(j);
        assertEquals(0, nlpl.activeJobs.size());
    }
}