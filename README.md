# E-commerce Backend System

A robust backend system for an e-commerce platform that handles product inventory, shopping cart management, automated order creation, and secure payment integration with Razorpay.

## Tech Stack

- **Framework:** Spring Boot 4.0.1
- **Language:** Java 17
- **Database:** MongoDB
- **Security & Configuration:** Dotenv-java
- **Payment Gateway:** Razorpay Java SDK
- **Utilities:** Lombok for boilerplate reduction

---

## 1. Project Setup and Configuration

### Environment Variables (.env)

Create a `.env` file in the root directory to store sensitive credentials securely. This file is ignored by Git to protect your data.

```env
MONGO_URI=mongodb://localhost:27017/ecommerce
RAZORPAY_KEY_ID=your_key_id
RAZORPAY_KEY_SECRET=your_key_secret
```

### Loading Configurations

The system uses a custom `DotenvPropertySource` to inject `.env` variables into the Spring Environment during startup.

- **Application Initializer:** Registered via `spring.factories`
- **YAML Mapping:** Properties are mapped in `application.yaml` using the `${}` placeholder syntax

### Razorpay Bean

A dedicated `RazorpayConfig` class initializes the `RazorpayClient` bean using credentials injected from the environment.

---

## 2. Core Entities and Data Models

- **Product:** Manages name, description, price, and stock levels
- **CartItem:** Links users to products and quantities before checkout
- **Order:** Records the total amount, status (CREATED, PAID, FAILED), and timestamp
- **OrderItem:** Snapshot of product details (price, quantity) at the time of purchase
- **Payment:** Tracks internal transaction status and links to the external Razorpay ID
- **User:** Handles user identification and roles (ROLE_BUYER, ROLE_SELLER, ROLE_ADMIN)

---

## 3. API Documentation

### Product Management

#### Create Product
- **Endpoint:** `POST /api/products/`
- **Description:** Accepts a Product JSON object to add to inventory

#### Get All Products
- **Endpoint:** `GET /api/products/`
- **Description:** Retrieves a list of all available items

### Shopping Cart

#### Add to Cart
- **Endpoint:** `POST /api/cart/add`
- **Description:** Adds a product to the user's cart. The system automatically fetches the latest price from the database for data integrity

#### View Cart
- **Endpoint:** `GET /api/cart/`
- **Description:** Retrieves all items for a specific `userId` passed as raw text in the request body

#### Clear Cart
- **Endpoint:** `DELETE /api/cart/clear`
- **Description:** Empties the cart for a specific user

### Order & Checkout

#### Create Order
- **Endpoint:** `POST /api/orders/`
- **Description:** Converts all items in a user's cart into a formal Order
- **Logic:** Calculates total amount, saves the Order and individual OrderItems, and clears the cart
- **Status:** Initialized as `CREATED`

### Payments & Webhooks

#### Initiate Payment
- **Endpoint:** `POST /api/payments/create`
- **Description:** Creates a Razorpay Order and a corresponding "PENDING" payment record in the database

#### Payment Webhook
- **Endpoint:** `POST /api/webhooks/payment`
- **Description:** Endpoint for Razorpay to confirm payment status
- **Callback Logic:** Updates the Payment record to `SUCCESS` and the Order status to `PAID` upon successful capture

---

## 4. Business Logic Workflows

### Cart to Order Conversion

When the checkout endpoint is hit, the `OrderService` performs the following:

1. Fetches all `CartItem` records for the user
2. Iterates through items to calculate the `totalAmount`
3. Persists a new `Order` document
4. Creates and saves `OrderItem` records as a permanent record of the sale
5. Clears the user's cart by deleting the `CartItem` entries

### Secure Payment Flow

The payment integration ensures that financial transactions are validated:

1. **Validation:** Ensures the order exists and is in `CREATED` status before allowing payment
2. **External Sync:** Uses `RazorpayClient` to generate an external order with the amount in paise (Amount × 100)
3. **Webhook Security:** Asynchronously listens for the `payment.captured` event to finalize the order status, preventing manual status manipulation from the client-side

---

## 5. Testing Guide

To verify the complete pipeline in Postman, follow this order:

1. **POST Product:** Create your inventory
2. **POST Cart Add:** Add items using the generated Product IDs
3. **POST Order:** Trigger checkout for the user
4. **POST Payment Create:** Get your Razorpay details
5. **POST Webhook:** Simulate a successful transaction using the `PaymentWebhookRequest` DTO structure

---
# Postman Collection
https://mohammed-rehan-s-team.postman.co/workspace/SpringBoot-101~437c1c44-0cdb-4760-85ba-4277aa6ff95d/collection/47884007-e6984e84-3130-42bb-9772-fe400fc8e370?action=share&creator=47884007
