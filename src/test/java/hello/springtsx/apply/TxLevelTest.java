package hello.springtsx.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@SpringBootTest
public class TxLevelTest {

    //LevelService 빈 등록하기
    @Autowired
    private LevelService service;

    @TestConfiguration
    static class TxApplyLevelConfig{
        @Bean
        LevelService levelService(){
            return new LevelService();
        }
    }

    @Test
    void orderTest(){
        service.write();
        service.read();
    }

    @Slf4j
    @Transactional(readOnly = true) // 각 메서드에 적용
    static class LevelService{
        //@Transactional(readOnly = false) : 기본값
        @Transactional
        public void write(){
            log.info("call write");
            printTxInfo();
        }

        public void read(){
            log.info("call read");
            printTxInfo();
        }
        private void printTxInfo(){
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active={}",txActive);
            boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
            log.info("tx readOnly={}",readOnly);
        }
    }
}
