package org.sid.web;

import org.sid.configs.BankTransactionItemProcessorAnalytics;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class BankJobRestController {

    private Job job;
    private JobLauncher jobLauncher;
    @Autowired
    private BankTransactionItemProcessorAnalytics itemProcessorAnalytics;

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

    @GetMapping("/totals")
    public Map<String, Double> getTotals() {
        Map<String,Double> totals = new HashMap<>();
        totals.put("Total Credit", itemProcessorAnalytics.getTotalCredit());
        totals.put("Total Debit", itemProcessorAnalytics.getTotalDebit());
        return totals;
    }
}

