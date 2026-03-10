package com.studenthub.studentservicehub.controller;

import com.studenthub.studentservicehub.model.Item;
import com.studenthub.studentservicehub.model.Order;
import com.studenthub.studentservicehub.repository.ItemRepository;
import com.studenthub.studentservicehub.repository.OrderRepository;
import com.studenthub.studentservicehub.service.EmailService; // <-- NEW IMPORT
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/admin/stationery")
public class StationeryAdminController {

    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final EmailService emailService; // <-- NEW FIELD

    @Autowired
    public StationeryAdminController(ItemRepository itemRepository, OrderRepository orderRepository, EmailService emailService) { // <-- FINAL CONSTRUCTOR UPDATE
        this.itemRepository = itemRepository;
        this.orderRepository = orderRepository;
        this.emailService = emailService; // <-- INITIALIZE EMAIL SERVICE
    }

    @GetMapping("/dashboard")
    public String showStationeryAdminDashboard(Model model) {
        model.addAttribute("allItems", itemRepository.findAll());
        model.addAttribute("pendingOrders", orderRepository.findByPaymentStatus("PAID"));

        // Ensure status messages passed via redirect are added to the model
        // NOTE: This logic is usually handled by the DispatcherServlet, but defining it here is safer if needed.
        if (model.getAttribute("statusMessage") == null) {
            model.addAttribute("statusMessage", null);
        }

        return "admin-stationery-dashboard";
    }

    // --- Handle Stock Update ---
    @PostMapping("/update-stock")
    public String updateStock(@RequestParam Long itemId, @RequestParam int newStock, RedirectAttributes redirectAttributes) {
        try {
            Item item = itemRepository.findById(itemId)
                    .orElseThrow(() -> new IllegalArgumentException("Item not found."));

            item.setStockQty(newStock);
            itemRepository.save(item);

            redirectAttributes.addAttribute("statusMessage", "Stock for " + item.getName() + " updated successfully to " + newStock + ".");
            redirectAttributes.addAttribute("statusType", "success");
        } catch (Exception e) {
            redirectAttributes.addAttribute("statusMessage", "Stock update failed: " + e.getMessage());
            redirectAttributes.addAttribute("statusType", "error");
        }
        return "redirect:/admin/stationery/dashboard";
    }

    // --- FINAL: Handle Order Acceptance with Email Confirmation ---
    @PostMapping("/accept-order/{orderId}")
    public String acceptOrder(@PathVariable Long orderId, RedirectAttributes redirectAttributes) {
        try {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new IllegalArgumentException("Order not found."));

            // 1. Update status to ACCEPTED
            order.setPaymentStatus("ACCEPTED");
            orderRepository.save(order);

            // 2. Send Confirmation Email with the Database ID
            String recipientEmail = order.getStudent().getEmail();
            String subject = "Order Confirmed: Ready for Pickup (ID: " + order.getOrderId() + ")";
            String body = String.format(
                    "Your stationery order has been accepted and is ready for pickup.\n\n" +
                            "Order ID (Required for Pickup): %s\n" +
                            "Item: %s, Quantity: %d\n" +
                            "Please show this Order ID at the counter to collect your items. Thank you!",
                    order.getOrderId(), order.getItem().getName(), order.getQuantity());

            emailService.sendSimpleEmail(recipientEmail, subject, body); // Real email send

            System.out.println("ADMIN ACTION: Order ID " + orderId + " accepted and confirmation email sent to " + recipientEmail);

            redirectAttributes.addAttribute("statusMessage", "Order ID " + orderId + " accepted successfully! Confirmation email sent.");
            redirectAttributes.addAttribute("statusType", "success");
        } catch (Exception e) {
            redirectAttributes.addAttribute("statusMessage", "Order acceptance failed: " + e.getMessage());
            redirectAttributes.addAttribute("statusType", "error");
        }
        return "redirect:/admin/stationery/dashboard";
    }
}