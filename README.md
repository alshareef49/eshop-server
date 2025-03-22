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
            "product": {
                "productId": 1000
            },
            "quantity": 1
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

## APPLICATION API DOCUMENTATION:

## Customer API
   1. Register Customer
   - Endpoint: POST /customer-api/register
   - Request Body: CustomerDTO
   ```json
   {
     "emailId": "user@example.com",
     "name": "John Doe",
     "password": "secret123",
     "newPassword": "secret123",
     "phoneNumber": "1234567890",
     "address": "123 Main St, Anytown, USA"
   }
   ```
  - Response Body: String
```String 
   You are successfully registered as customer with Email Id: user@example.com
```

   2. Login Customer
   - Endpoint: POST /customer-api/login
   - Request Body: CustomerDTO
   ```json
   {
     "emailId": "user@example.com",
     "password": "secret123"
   }
   ```
   - Response Body: String
   ```json
  {
   "token": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoic2hhcmVlZkBnbWFpbC5jb20iLCJpYXQiOjE3NDI2NTk5MTksImV4cCI6MTc0MjY5NTkxOX0.JOunmsLaAXbpUbnuz2K11A6Qjyca0Xnmr4Qgn4eY4aE"
  }
   ```
   - need to pass the token in the Authorization header with the value "Bearer <token>"

  3. Browse All Products
  - Endpoint: GET /product-api/products
  - Response Body:
  ```json 
  [
    {
        "productId": 1021,
        "name": "Corsair DSKTP 2020",
        "description": "20-c103in 19.45-inch All-in-One Desktop (Celeron J3060/4GB/500GB/Windows 10/Integrated Graphics), Black",
        "category": "Electronics - Desktop",
        "brand": "Corsair",
        "price": 35000.0,
        "discount": null,
        "quantity": null,
        "sellerEmailId": null,
        "availableQuantity": 150
    },
    {
        "productId": 1022,
        "name": "Precision DSKTP 23",
        "description": "Pentium Quad Core/4 GB DDR3/1 TB/Free DOS - Black, 19.5 Inch Screen",
        "category": "Electronics - Desktop",
        "brand": "Precision",
        "price": 45000.0,
        "discount": null,
        "quantity": null,
        "sellerEmailId": null,
        "availableQuantity": 15
    }
  ]  
  ```
  4. Browse Product By Id
   - Endpoint: GET /product-api/products/{productId}
   - Response Body:
   ```json
      {
           "productId": 1022,
           "name": "Precision DSKTP 23",
           "description": "Pentium Quad Core/4 GB DDR3/1 TB/Free DOS - Black, 19.5 Inch Screen",
           "category": "Electronics - Desktop",
           "brand": "Precision",
           "price": 45000.0,
           "discount": null,
           "quantity": null,
           "sellerEmailId": null,
           "availableQuantity": 15
        }
   ```
    
  5. Add Product to Cart
  - Endpoint: POST /cart-api/products
    - Request Body:
      ```json
          {
            "customerEmailId": "user@example.com",
            "cartProducts": [
                      {
                      "product": {
                          "productId": 1000
                      },
                      "quantity": 1
                  }
        ]
      }
      ```
    
  6. Place Order
  - Endpoint: POST /order-api/place-order
    - Request Body:
      ```json
      {
        "customerEmailId": "user@example.com",
        "paymentThrough": "(DEBIT_CARD || CREDIT_CARD)"           
      }
      ```
    
  7. Make Payment
     - Endpoint: POST /payment-api/customer/user@example.com/order/{orderId}
       - Response Body:
     ```json
             {
                 "status": "SUCCESS",
                 "message": "Payment session created ",
                 "sessionId": "cs_test_a1MrSdMwQoyTVzIUtzTfYGkjUL5ertJu2GkK2iGIJppV3b0V25q3onYvVg",
                 "sessionUrl": "https://checkout.stripe.com/c/pay/cs_test_a1MrSdMwQoyTVzIUtzTfYGkjUL5ertJu2GkK2iGIJppV3b0V25q3onYvVg#fidkdWxOYHwnPyd1blpxYHZxWjA0VzdoZmxCfVE8TjwzVDE0MEtcREZVMklVd3dwUXNNMmpDS3ZwUWloT1JCMWxfTUB%2FYH9fcUZCQ3xUcUdxbTRyfW5ub2E1NUxoc0FLVDFkSEdJSkBHMDdmNTVNMFd2Z0BjMycpJ2N3amhWYHdzYHcnP3F3cGApJ2lkfGpwcVF8dWAnPyd2bGtiaWBabHFgaCcpJ2BrZGdpYFVpZGZgbWppYWB3dic%2FcXdwYHgl"
            }
     ```
     - make sure StripePaymentService is running for making payment request
     - After that copy sessionUrl and open in browser and provide payment details

  
8. Login as Admin for product management
  - Endpoint: POST /admin/create-admin-user
  - Request Body:
  ```json
  {
  "emailId": "userAdmin@gmail.com",
  "name": "User",
  "password": "Admin@123",
  "newPassword": "Admin@123",
  "phoneNumber": "4752399467",
  "address": "New Delhi"
}
  ```
  
    

## Database Schema
![image](https://github.com/user-attachments/assets/d26d3bd4-8763-411c-9eb3-0a204493fdcc)


