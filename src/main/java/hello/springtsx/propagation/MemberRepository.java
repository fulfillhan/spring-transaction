package hello.springtsx.propagation;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    //@Transactional
    public void save(Member member){
        log.info("멤버 저장");
        em.persist(member);
    }

    public Optional<Member> find(String userName) {
        return em.createQuery("select m from Member m where m.userName = : username", Member.class)
                .setParameter("username", userName)
                .getResultList().stream().findAny();
    }
}
