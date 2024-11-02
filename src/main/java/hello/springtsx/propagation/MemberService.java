package hello.springtsx.propagation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final LogRepository logRepository;

    //트랜잭션 각각 적용
    @Transactional
    public void joinV1(String userName){
        Member member = new Member(userName);
        Log logMessage = new Log(userName);

        log.info("memberRepository 호출 시작");
        memberRepository.save(member);
        log.info("== memberRepository 호출 종료 ==");

        log.info("== logRepository 호출 시작 ==");
        logRepository.save(logMessage);
        log.info("== logRepository 호출 종료 ==");

    }

    // DB로그 저장시 예외가 발생하면 예외를 복구한다.
    //별도의 트랜잭션은 설정하지 않음
    @Transactional
    public void joinV2(String userName){
        Member member = new Member(userName);
        Log logMessage = new Log(userName);

        log.info("memberRepository 호출 시작");
        memberRepository.save(member);
        log.info("== memberRepository 호출 종료 ==");

        log.info("== logRepository 호출 시작 ==");
        try {
            logRepository.save(logMessage);
        }catch (RuntimeException e){
            log.info("로그 저장에 실패했습니다. log.getMessage={}",logMessage.getMessage());
            log.info("정상 흐름 반환");
        }
        log.info("== logRepository 호출 종료 ==");

    }

}
