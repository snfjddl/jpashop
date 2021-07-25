package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findAll(OrderSearch orderSearch) {
        /**
         * 방법 1 JPQL 동적 빌드
         */
        String JPQL = "select o from Order o join o.member m";
        boolean isFirstCondition = true;

        // 주문상태검색
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                JPQL += " where";
                isFirstCondition = false;
            } else {
                JPQL += " and";
            }
            JPQL += " o.status = :status";
        }

        // 회원이름검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                JPQL += " where";
            } else {
                JPQL += " and";
            }
            JPQL += " m.name like :name";
        }

        TypedQuery<Order> query = em.createQuery(JPQL, Order.class)
                .setMaxResults(1000);

        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }

        if(StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }

        return query.getResultList();

        /*
        return em.createQuery("select o from Order o join o.member m" +
                        " where o.status= :status" +
                        " and m.name like :name", Order.class)
                .setParameter("status", orderSearch.getOrderStatus())
                .setParameter("name", orderSearch.getMemberName())
//                .setFirstResult(100)   페이징 처리가 필요하다면 사용가능 100번째 결과부터 가져온다.
                .setMaxResults(1000)    // 쿼리 결과를 최대 1000개 까지 가져온다
                .getResultList();
        */
    }
}
