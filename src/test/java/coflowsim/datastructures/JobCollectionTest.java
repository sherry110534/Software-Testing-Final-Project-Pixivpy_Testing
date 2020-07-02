package coflowsim.datastructures;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JobCollectionTest {

    JobCollection jobs = new JobCollection();
    Job j = new Job("JOB-1", 1);

    @Test
    void getOrAddJob() {
        jobs.getOrAddJob(j.jobName);
        assertEquals(1, jobs.size());
    }

    @Test
    void removeJob() {
        jobs.getOrAddJob(j.jobName);
        assertEquals(1, jobs.size());
        Job tmp_j = jobs.getOrAddJob(j.jobName);
        jobs.removeJob(tmp_j);
        assertEquals(0, jobs.size());
    }

    @Test
    void elementAt() {
        jobs.getOrAddJob(j.jobName);
        assertEquals(j.jobName, jobs.elementAt(0).jobName);
    }
}