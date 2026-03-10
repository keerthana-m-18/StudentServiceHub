package com.studenthub.studentservicehub.service;

import com.studenthub.studentservicehub.model.Item;
import com.studenthub.studentservicehub.model.Order;
import com.studenthub.studentservicehub.model.Payment;
import com.studenthub.studentservicehub.model.Student;
import com.studenthub.studentservicehub.repository.ItemRepository;
import com.studenthub.studentservicehub.repository.OrderRepository;
import com.studenthub.studentservicehub.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.Optional;

@Service // Marks this class as a Spring Service Component
public class OrderManager {

    // Injecting Repositories (Data Access Layer)
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    @Autowired // Spring automatically injects the repository implementations
    public OrderManager(ItemRepository itemRepository, OrderRepository orderRepository, PaymentRepository paymentRepository) {
        this.itemRepository = itemRepository;
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
    }

    /**
     * Business Logic Method: Handles placing an order and updating stock atomically.
     * [cite_start]Implements Stock Validation Algorithm[cite: 351].
     * @param student The student placing the order.
     * @param itemId The ID of the item being purchased.
     * @param quantity The number of items to purchase.
     * @param paymentMethod The method of payment (e.g., "UPI", "Razorpay").
     * @return The newly created Order object.
     * @throws IllegalStateException if stock is insufficient.
     */
    @Transactional // Ensures the entire method runs as one atomic database transaction
    public Order placeOrder(Student student, Long itemId, int quantity, String paymentMethod) {
        // 1. Fetch Item and Perform Stock Validation (Report: Stock Validation Algorithm [cite: 351])
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalStateException("Item not found."));

        // Check item availability before confirming an order [cite: 351]
        if (item.getStockQty() < quantity) {
            // Implements the Stock Validation logic
            throw new IllegalStateException("Insufficient stock: " + item.getName());
        }

        // 2. Calculate and Update Stock (Report: Atomic update to prevent race conditions [cite: 351])
        Double totalAmount = item.getPrice() * quantity;
        item.setStockQty(item.getStockQty() - quantity); // Deduct stock
        itemRepository.save(item); // Save the updated stock

        // 3. Create Order
        Order order = new Order(student, item, quantity, totalAmount, "PENDING");
        order.setQrCode(generateUniqueQrCode());// Generate QR code [cite: 347]

        // 4. Save Order
        Order savedOrder = orderRepository.save(order);

        // 5. Simulate Payment Processing (Report: Razorpay/UPI for digital payments [cite: 32])
        Payment payment = new Payment(savedOrder, paymentMethod, "TXN-" + UUID.randomUUID().toString().substring(0, 8), totalAmount, "SUCCESS");
        paymentRepository.save(payment);

        // Final update of order status after successful payment
        savedOrder.setPaymentStatus("PAID");
        orderRepository.save(savedOrder); // Update order status in the database

        return savedOrder;
    }

    /**
     * Helper method to simulate QR Code generation
     * [cite_start]Report: Uses a Java library (like ZXing) to generate QR codes[cite: 347].
     */
    private String generateUniqueQrCode() {
        // Simple UUID generation simulates the unique code
        return UUID.randomUUID().toString();
    }

    // Additional methods (getOrdersByStudent, getInventory, etc.) would be added here.
}