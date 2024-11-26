package hello.springtsx.internalCall;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InitializationComponent {

    private final SomeService service;
    @PostConstruct
    public void initV3(){
        log.info("@PostConstruct 적용한 초기화 메서드");
        service.transactionInitalization(); //트랜잭션 적용 메서드
    }
}
