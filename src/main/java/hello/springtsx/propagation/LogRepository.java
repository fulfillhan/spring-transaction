package hello.springtsx.propagation;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class LogRepository {

    private final EntityManager em;

    //@Transactional
    public void save(Log logMessage){
        log.info("log 저장");
        em.persist(logMessage);

        //예외 발생
        if(logMessage.getMessage().contains("로그 예외")){
            log.info("예외 발생");
            throw new RuntimeException("예외 발생");
        }
    }

    public Optional<Log> find(String logMessage) {
        return em.createQuery("select l from Log l where l.message = : message", Log.class)
                .setParameter("message", logMessage)
                .getResultList().stream().findAny();
    }

}
