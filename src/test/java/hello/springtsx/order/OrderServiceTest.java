package hello.springtsx.order;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@Slf4j
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    //정상 처리
    @Test
    void complete() throws NotEnoughMoneyException {
        //given
        Order order = new Order();
        order.setUsername("정상");
        //when
        orderService.order(order);
        //then
        Order findOrder = orderRepository.findById(order.getId()).get();
        assertThat(findOrder.getPayStatus()).isEqualTo("완료");
    }

    //런타임 예외
    @Test
    void runtimeException()  {
        //given
        Order order = new Order();
        order.setUsername("예외");
        //whenA  예외 조건일 때
        Assertions.assertThatThrownBy(() -> orderService.order(order)).isInstanceOf(RuntimeException.class);
        //then
        Optional<Order> optionalOrder = orderRepository.findById(order.getId());
        assertThat(optionalOrder.isEmpty()).isTrue();
    }

    @Test
    void bizException(){
        //given
        Order order = new Order();
        order.setUsername("잔고부족");

        //when
        try {
            orderService.order(order);
        } catch (NotEnoughMoneyException e) {  // 체크예외로 던지기
            log.info("고객에게 잔고 부족을 알리고 별도의 계좌로 입금하도록 안내해라");
        }

        //then
        Order findOrder = orderRepository.findById(order.getId()).get();
        /*1. 롤백 적용되면 커밋이 되지 않아서 db에 데이터가 저장되지 않음 = No value present*/
        assertThat(findOrder.getPayStatus()).isEqualTo("대기");
    }

}