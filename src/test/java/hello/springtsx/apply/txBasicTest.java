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

    @Autowired
    private BasicService basicService;

    @Test
    void proxyCheck(){
        log.info("aop class={}",basicService.getClass());
        boolean aopProxy = AopUtils.isAopProxy(basicService);// spring aop로 적용된건지 확인 가능
        assertThat(aopProxy).isTrue();
    }

    @Test
    void txTest(){
        basicService.transaction();
        basicService.nonTransaction();
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
        public void transaction(){
            log.info("call transaction");
            //트랜잭션 활성여부 알 수 있다.
            boolean transactionActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("transaction active = {}",transactionActive);
        }
        public void nonTransaction(){
            log.info("call nonTransaction");
            boolean transactionActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("transaction active = {}",transactionActive);
        }
    }
}
