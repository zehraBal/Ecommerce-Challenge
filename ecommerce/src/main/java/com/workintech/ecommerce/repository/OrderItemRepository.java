package com.workintech.ecommerce.repository;

import com.workintech.ecommerce.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {

    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.id = :orderId")
    List<OrderItem> findByOrderId(Long orderId);

    @Query("SELECT oi FROM OrderItem oi WHERE oi.product.id = :productId")
    List<OrderItem> findByProductId( Long productId);


    //order_id değil de order.id yazıyoruz burada hql sorguları yazarken direkt databasede verdiğimiz adı kullanamıyoruz
    //Bu yüzden order.id getter mantığı ile aynı çalışıyor.
    //OrderItemdaki order nesnesi referans değer onun id alanına erişebilmek için de order.id kullandık.
}
