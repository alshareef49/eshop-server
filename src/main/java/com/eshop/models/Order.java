package com.eshop.models;

import com.eshop.dto.OrderStatus;
import com.eshop.dto.PaymentThrough;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "`ORDER`")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer orderId;

	private String customerEmailId;

	private LocalDateTime dateOfOrder;

	private Double discount;

	private Double totalPrice;

	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	@Enumerated(EnumType.STRING)
	private PaymentThrough paymentThrough;

	private LocalDateTime dateOfDelivery;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "orderId")
	@ToString.Exclude
	private List<OrderedProduct> orderedProducts;

	private String deliveryAddress;

	@Override
	public final boolean equals(Object o) {
		if (this == o) return true;
		if (o == null) return false;
		Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
		Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
		if (thisEffectiveClass != oEffectiveClass) return false;
		Order order = (Order) o;
		return getOrderId() != null && Objects.equals(getOrderId(), order.getOrderId());
	}

	@Override
	public final int hashCode() {
		return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
	}
}