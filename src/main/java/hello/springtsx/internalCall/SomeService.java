package hello.springtsx.internalCall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@Slf4j
public class SomeService {
    
    //트랜잭션 초기화 작업
    @Transactional
    public void transactionInitalization(){
        boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();
        log.info("트랜잭션 활성 여부 = {}",isActive);
    }
}
