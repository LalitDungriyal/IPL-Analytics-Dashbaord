package com.project.ipldashboard.data;


import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.project.ipldashboard.model.Match;

@Configuration
public class BatchConfig {

    private final String[] FIELD_NAMES = new String[] { "ID","City","Date","Season","MatchNumber","Team1","Team2","Venue","TossWinner","TossDecision","SuperOver","Winner","result","result_margin","method","Player_of_Match","Team1Players","Team2Players","Umpire1","Umpire2" };


    @Bean
    public FlatFileItemReader<MatchInput> reader() {
        System.out.println("reader");
        return new FlatFileItemReaderBuilder<MatchInput>()
                .name("MatchItemReader")
                .resource(new ClassPathResource("match-data.csv"))
                .delimited().names(FIELD_NAMES)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<MatchInput>() {
                    {
                        setTargetType(MatchInput.class);
                    }
                }).build();
    }

    @Bean
    public MatchDataProcessor processor() {
        System.out.println("PROCESSOR");
        return new MatchDataProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Match> writer(DataSource dataSource) {
        System.out.println("writer");
        return new JdbcBatchItemWriterBuilder<Match>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO match (city, date, player_of_match, vanue, team1, team2, toss_winner, toss_decision, match_winner, result, result_margin, umpire1, umpire2) "
                        + " VALUES (:city, :date, :playerOfMatch, :vanue, :team1, :team2, :tossWinner, :tossDecision, :matchWinner, :result, :resultMargin, :Umpire1, :Umpire2)")
                .dataSource(dataSource).build();
    }

    @Bean
    public Job importUserJob(JobRepository jobRepository,
    JobCompletionNotificationListener listener, Step step1) {
    return new JobBuilder("importUserJob", jobRepository)
    .incrementer(new RunIdIncrementer())
    .listener(listener)
    .flow(step1)
    .end()
    .build();
}

    @Bean
    public Step step1(JobRepository jobRepository,
        PlatformTransactionManager transactionManager, JdbcBatchItemWriter<Match> writer) {
      return new StepBuilder("step1", jobRepository)
        .<MatchInput, Match> chunk(10, transactionManager)
        .reader(reader())
        .processor(processor())
        .writer(writer)
        .build();
    }
}