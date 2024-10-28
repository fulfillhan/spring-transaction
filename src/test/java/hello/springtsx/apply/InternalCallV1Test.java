package hello.springtsx.apply;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Call;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
/*프록시 내부 호출의 문제! -> 메서드 내부 호출시 해당 메서드는 자기자신 인스턴스의 메서드를 호출한다.*/
@SpringBootTest // 테스트 스프링 컨테이너 생성, 빈 등록 및 의존관계 주입 가능하다
@Slf4j
public class InternalCallV1Test {

    @Autowired // 의존관계주입
    CallService callService;

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

    @TestConfiguration//빈으로 등록 가능
    static class InternalCallV1Config{
        @Bean //빈 등록
        CallService callService(){
            return new CallService();
        }

    }

    @Slf4j
    static class CallService{
        public void external(){
            log.info("external call");
            printTxInfo();
            internal();
        }
        @Transactional
        public void internal(){
            log.info("internal call");
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
