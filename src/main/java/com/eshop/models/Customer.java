package com.eshop.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "CUSTOMER")
public class Customer {
    @Id
    private String emailId;
    private String name;
    private String password;
    private String phoneNumber;
    private String address;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "CUSTOMER_ROLES",
            joinColumns = @JoinColumn(name = "CUSTOMER_EMAIL"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID")
    )
    @JsonManagedReference
    private List<Role> roles;
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Customer customer = (Customer) o;
        return getEmailId() != null && Objects.equals(getEmailId(), customer.getEmailId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}