package org.sid.web;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class BankJobRestController {

    private Job job;
    private JobLauncher jobLauncher;

    public BankJobRestController (Job job, JobLauncher jobLauncher) {
        this.job = job;
        this.jobLauncher = jobLauncher;
    }

    @GetMapping("/startJob")
    public BatchStatus startJob() throws Exception {
        HashMap<String, JobParameter> jobParameters =new HashMap<>();
        jobParameters.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters parameters =new JobParameters(jobParameters);

        JobExecution  jobExecution = jobLauncher.run(job, parameters);
        while (jobExecution.isRunning()) {
            System.out.println("job running ...");
        }
        return jobExecution.getStatus();

    }
}
