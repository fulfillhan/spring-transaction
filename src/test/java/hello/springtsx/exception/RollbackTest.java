package hello.springtsx.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor
public class RollbackTest {

    private final RollbackService rollbackService;

    @Test
    void runtimeException(){
        assertThatThrownBy(() -> rollbackService.runtimeException()).isInstanceOf(RuntimeException.class);
    }

    @Test
    void checkedExcption(){
        assertThatThrownBy(() -> rollbackService.checkedExcption()).isInstanceOf(MyException.class);
    }

    @Test
    void rollbackFor(){
        assertThatThrownBy(() -> rollbackService.rollbackFor()).isInstanceOf(MyException.class);
    }
    @TestConfiguration
    static class RollbackForConfig{
        @Bean
        RollbackService rollbackService(){
         return new RollbackService();
        }
    }

    @Slf4j
    static class RollbackService{

        //런타임 예외 발생 :  롤백
        @Transactional
        public void runtimeException(){
            log.info("call runtimeException");
            throw new RuntimeException();
        }

        //체크 예외 발생 :  커밋
        @Transactional
        public void checkedExcption() throws MyException {
            log.info("call checkedExcption");
            throw new MyException();
        }

        //체크 예외 rollbackfor 사용 : 특정 어떤 예외가 발생할 때 롤백할지 지정할 수 있다.(강제롤백)
        @Transactional(rollbackFor = MyException.class)
        public void rollbackFor() throws MyException {
            log.info("call checkedExcption");
            throw new MyException();
        }
    }

    static class MyException extends Exception{

    }

}

