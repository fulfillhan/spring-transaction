package hello.springtsx.apply;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
public class txBasicTest {

    @Autowired  // 의존관계 주입 -  프록시가 주입된다.
    private BasicService basicService;

    @Test
    void proxyCheck(){
        log.info("aop class={}",basicService.getClass());
        boolean aopProxy = AopUtils.isAopProxy(basicService);// spring aop로 적용된건지 확인 가능
        assertThat(aopProxy).isTrue();
    }

    @Test
    void txTest(){
        basicService.tx();
        basicService.nonTx();
    }


    @TestConfiguration
    static class TxApplyBasicConfig{
        @Bean
        BasicService basicService(){
            return new BasicService();
        }
    }
    @Slf4j
    static class BasicService{

        @Transactional
        public void tx(){
            log.info("call tx");
            //트랜잭션 활성여부 알 수 있다.
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active = {}",txActive);
        }
        public void nonTx(){
            log.info("call nonTx");
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active = {}",txActive);
        }
    }
}
