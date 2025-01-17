package com.example.transaction.transaction;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement(mode = AdviceMode.PROXY)
@Slf4j
public class TransactionApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(TransactionApplication.class, args);
    }


    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private BarService barService;

    @Override
    public void run(String... args) throws Exception {
//        fooService.insertRecord();
//        log.info("AAA {}",
//                jdbcTemplate
//                        .queryForObject("SELECT COUNT(*) FROM FOO WHERE BAR='AAA'", Long.class));
//        try {
//            fooService.insertThenRollback();
//        } catch (Exception e) {
//            log.info("BBB {}",
//                    jdbcTemplate
//                            .queryForObject("SELECT COUNT(*) FROM FOO WHERE BAR='BBB'", Long.class));
//        }
//
//        try {
//            fooService.invokeInsertThenRollback();
//        } catch (Exception e) {
//            log.info("BBB {}",
//                    jdbcTemplate
//                            .queryForObject("SELECT COUNT(*) FROM FOO WHERE BAR='BBB'", Long.class));
//        }

        String noException = Global.NESTED_NO_EXCEPTION;
        String hasException = Global.NESTED_HAS_EXCEPTION;



        try {
            barService.noTransactional();
        }catch (Exception e){
            if (Global.MANDATORY_HAS_EXCEPTION.equals(hasException)){
                log.error(e.toString());
            }
            log.info("第一种情况 {}",
                    jdbcTemplate
                            .queryForObject("SELECT COUNT(*) FROM FOO WHERE BAR='"+hasException+"'", Long.class));
        }

        try {
            barService.hasTransactional();
        }catch (Exception e){
            log.info("第二种情况 {}",
                    jdbcTemplate
                            .queryForObject("SELECT COUNT(*) FROM FOO WHERE BAR='"+noException+"'", Long.class));
        }

        try {
            barService.hasTransactionalNoException();
        }catch (Exception e){
            log.info("第三种情况 {}",
                    jdbcTemplate
                            .queryForObject("SELECT COUNT(*) FROM FOO WHERE BAR='"+Global.NESTED_HAS_EXCEPTION_TWO+"'", Long.class));
        }
    }
}
