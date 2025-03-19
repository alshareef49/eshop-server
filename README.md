# E-Shop Application

E-Shop is a fully functional e-commerce platform that allows users to browse products, add them to their cart, place orders, and make secure payments. 
The backend is built using Spring Boot. 

## Key Features
- 🛒 Product Listing and Search
- 🛍️ Add Products to Cart
- 🚚 Place Orders with Payment
- 🔒 Secure Authentication with OAuth2 & JWT
- 📚 API Documentation with Swagger

  ## Tech Stack
### Backend:
- Java 21
- Spring Boot 3.4.3
- Spring Data JPA (Hibernate)
- Spring Security (OAuth2, JWT)
- MySQL 8.x
- Swagger (Springdoc OpenAPI)

  ## Request Flow
1. **Add Product to Cart:**
   - Endpoint: POST /cart-api/add-product
   - Payload:
   ```json
   {
     "customerEmailId": "user@example.com",
     "cartProducts": [
       {
         "productId": 1001,
         "quantity": 2
       }
     ]
   }
2. **Place Order:**
  - Endpoint: POST /order-api/place-order
  - Payload:
   ```json
   {
     "customerEmailId": "user@example.com",
     "paymentThrough": "(DEBIT_CARD || CREDIT_CARD)"
   }
```

3. **Make Payment:**
  - Endpoint: POST /payment-api/customer/user@example.com/order/{orderId}



## Database Schema
![image](https://github.com/user-attachments/assets/57d3c39a-2880-41c7-9b8e-990b6c3969fe)

