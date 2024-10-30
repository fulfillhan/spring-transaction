package hello.springtsx.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public void order(Order order) throws NotEnoughMoneyException {
        log.info("order 호출");
        Order originalOrder = orderRepository.save(order);

        log.info("order 프로세스 진입");

        if (originalOrder.getUsername().equals("예외")) {
            log.info("예외 발생");
            throw new RuntimeException("시스템 예외");

        } else if (originalOrder.getUsername().equals("잔고부족")) {
            originalOrder.setPayStatus("대기");
            throw new NotEnoughMoneyException("잔고가 부족합니다");

        } else {
            //정상승인
            log.info("정상 승인");
            originalOrder.setPayStatus("완료");
        }
        log.info("프로세스 종료");
    }
}
