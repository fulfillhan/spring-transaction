package hello.springtsx.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
/*프록시 내부 호출의 문제! -> 메서드 내부 호출시 해당 메서드는 자기자신 인스턴스의 메서드를 호출한다.*/
@SpringBootTest // 테스트 스프링 컨테이너 생성, 빈 등록 및 의존관계 주입 가능하다
@Slf4j
public class InternalCallProblemTest {

    @Autowired CallService callService;

    @TestConfiguration//빈으로 등록 가능
    static class InternalCallProblemConfig{
        @Bean //빈 등록
        CallService callService(){
            return new CallService();
        }

    }

    @Test
    void printProxy(){
        log.info("callService class={}", callService.getClass());
    }

    @Test
    void internalCall(){
        callService.internal();
    }

    @Test
    void externalCall(){
        callService.external();
    }

    @Slf4j
    @Service
    static class CallService{

        public void external(){
            log.info("external 호출");
            printTxInfo();
            internal(); // 내부 메서드 호출
        }
        @Transactional
        public void internal(){
            log.info("internal 호출");
            printTxInfo();
        }
        private void printTxInfo(){
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active={}",txActive);
        }
    }
}
