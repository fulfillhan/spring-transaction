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
@SpringBootTest // 테스트 스프링 컨테이너 생성, 빈 등록 및 의존관계 주입 가능하다
@Slf4j
public class InternalCallV2Test {

    @Autowired // 의존관계주입
    CallService callService;

    @Test
    void printProxy(){
        log.info("callService class={}", callService.getClass());
    }

    @Test
    void externalCallV2(){
        callService.external();
    }
    @TestConfiguration//빈으로 등록 가능
    static class InternalCallV1Config{
        @Bean //빈 등록
        CallService callService(){
            return new CallService(internalService());
        }
        @Bean
        InternalService internalService(){
            return new InternalService();
        }

    }

    @Slf4j
    @RequiredArgsConstructor
    static class CallService{

        private final InternalService internalService;

        public void external(){
            log.info("external call");
            printTxInfo();
            internalService.internal();// 외부의 internalService로 주입받는다.
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
