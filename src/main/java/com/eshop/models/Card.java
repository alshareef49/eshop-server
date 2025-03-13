package com.eshop.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "CARD")
public class Card {

	@Id
	@Column(name = "CARD_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer cardID;

	@Column(name = "CARD_TYPE")
	private String cardType;

	@Column(name = "CARD_NUMBER")
	private String cardNumber;

	@Column(name = "CVV")
	private String cvv;

	@Column(name = "EXPIRY_DATE")
	private LocalDate expiryDate;

	@Column(name = "NAME_ON_CARD")
	private String nameOnCard;

	@Column(name = "CUSTOMER_EMAIL_ID")
	private String customerEmailId;

	@Override
	public final boolean equals(Object o) {
		if (this == o) return true;
		if (o == null) return false;
		Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
		Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
		if (thisEffectiveClass != oEffectiveClass) return false;
		Card card = (Card) o;
		return getCardID() != null && Objects.equals(getCardID(), card.getCardID());
	}

	@Override
	public final int hashCode() {
		return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
	}
}