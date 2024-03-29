package org.sid.configs;

import org.sid.dao.BankTransaction;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.ArrayList;
import java.util.List;


@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {
    @Autowired
    private ItemReader<BankTransaction> bankTransactionItemReader;
    @Autowired
    private ItemWriter<BankTransaction> bankTransactionItemWriter;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean
    public Job getJob() {
        Step step1 = stepBuilderFactory.get("step-load")
                .<BankTransaction, BankTransaction>chunk(100)
                .reader(bankTransactionItemReader)
                .processor(compositeItemProcessor())
                .writer( bankTransactionItemWriter)
                .build();
        return jobBuilderFactory.get("job-load-data")
                .start(step1)
                .build();

    }

    @Bean
    public ItemProcessor<? super BankTransaction,? extends BankTransaction> compositeItemProcessor() {
        List<ItemProcessor<BankTransaction, BankTransaction>> itemProcessors = new ArrayList<>();
        itemProcessors.add(transactionItemProcessor1());
        itemProcessors.add(transactionItemProcessor2());
        CompositeItemProcessor<BankTransaction, BankTransaction> compositeItemProcessor =
                new CompositeItemProcessor<>();
        compositeItemProcessor.setDelegates(itemProcessors);
        return compositeItemProcessor;
    }

    @Bean
    public FlatFileItemReader<BankTransaction> getFlatFileItemReader(@Value("${inputFile}")
                                                                                 Resource resource) {
        FlatFileItemReader<BankTransaction> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setName("data-csv");
        flatFileItemReader.setLinesToSkip(1);
        flatFileItemReader.setResource(resource);
        flatFileItemReader.setLineMapper(getLineMaper());
        return flatFileItemReader;

    }

    @Bean
    public LineMapper<BankTransaction> getLineMaper() {
        DefaultLineMapper<BankTransaction> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setDelimiter(",");
        delimitedLineTokenizer.setStrict(false);
        delimitedLineTokenizer.setNames("id","accountID", "strTransactionDate","transactionType", "amount" );
        lineMapper.setLineTokenizer(delimitedLineTokenizer);
        BeanWrapperFieldSetMapper<BankTransaction> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(BankTransaction.class);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

    @Bean
    public BankTransactionItemProcessor transactionItemProcessor1(){
        return new BankTransactionItemProcessor();
    }
    @Bean
    public BankTransactionItemProcessorAnalytics transactionItemProcessor2(){
        return new BankTransactionItemProcessorAnalytics();
    }

}
