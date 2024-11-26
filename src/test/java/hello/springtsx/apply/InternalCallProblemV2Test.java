package hello.springtsx.apply;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
/*프록시 내부 호출 문제2 -> 별도의 클래스로 분리해서 생성한다. 내부호출을 외부호출로 변경한다.*/
@SpringBootTest
@Slf4j
public class InternalCallProblemV2Test {

    @Autowired CallService callService;

    @TestConfiguration
    static class InternalCallV1Config{
        @Bean
        CallService callService(){
            return new CallService(internalService());
        }
        @Bean
        InternalService internalService(){
            return new InternalService();
        }

    }
    @Test
    void externalCallV2(){
        callService.external();

    }

    @Slf4j
    @RequiredArgsConstructor
    static class CallService{

        private final InternalService internalService;

        public void external(){
            log.info("external call");
            printTxInfo();
            internalService.internal();
        }

        private void printTxInfo(){
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active={}",txActive);
        }
    }

    @Slf4j
    static class InternalService{
        @Transactional
        public void internal(){
            log.info("internal call");
            printTxInfo();
        }
        private void printTxInfo(){
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active={}",txActive);
        }
    }

}
