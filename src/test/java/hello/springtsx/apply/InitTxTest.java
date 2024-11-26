package hello.springtsx.apply;

import hello.springtsx.internalCall.InitializationComponent;
import hello.springtsx.internalCall.SomeService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@SpringBootTest
@Slf4j
public class InitTxTest {

    @Autowired
    Hello hello;
    @Autowired
    SomeService service;

    /*@Test
    void go(){
        //초기화 코드는 스프링이 초기화 시점에 호출한다
    }*/

    @Test
    void testInitV3(){
        log.info("트랜잭션이 적용 되었는가?");
        //component.initV3();
        service.transactionInitalization();
    }
    @TestConfiguration
    static class InitTxTestConfig{
        @Bean
        Hello hello(){
            return new Hello();
        }
    }

    @Slf4j
    static class Hello{
        @PostConstruct
        @Transactional
        public void initV1(){
            boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("@PostConstruct 트랜잭션 활성 여부={}",isActive);
        }
        //1. 트랜잭션 적용 안됨
        /*초기화 코드가 먼저 호출된 후, 트랜잭션 AOP가 적용되는데,
        * 다음과 같은 경우 PostConstruct 으로 인해 트랜잭션 관리가 제대로 되지 않는다. */


        //2 가장 확실한 대안책 EventListener(value = ApplicationReadyEvent.class) 사용
        @EventListener(ApplicationReadyEvent.class)
        @Transactional
        public void initV2(){
            boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("Hello init ApplicationReadyEvent tx active={}",isActive);
        }
    }

}
