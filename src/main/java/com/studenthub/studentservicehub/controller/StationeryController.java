package com.studenthub.studentservicehub.controller;

import com.studenthub.studentservicehub.model.Item;
import com.studenthub.studentservicehub.model.Student;
import com.studenthub.studentservicehub.repository.ItemRepository;
import com.studenthub.studentservicehub.service.OrderManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class StationeryController {

    private final OrderManager orderManager;
    private final ItemRepository itemRepository;

    @Autowired
    public StationeryController(OrderManager orderManager, ItemRepository itemRepository) {
        this.orderManager = orderManager;
        this.itemRepository = itemRepository;
    }

    /**
     * Handles the GET request to view the dedicated Stationery Ordering page.
     * Fetches all available items (Name, ID, Price) and adds them to the model.
     */
    @GetMapping("/stationery/view")
    public String viewStationeryModule(Model model) {

        // Fetch all available items from the database for the dynamic dropdown
        List<Item> items = itemRepository.findAll();

        // Add the full list of items to the model for the frontend loop
        model.addAttribute("availableItems", items);

        // Returns the dedicated template file
        return "stationery-view";
    }

    /**
     * Handles the POST request to place an order, enforcing Stock Validation.
     * Redirects success/failure status back to the Unified Dashboard.
     */
    @PostMapping("/stationery/place-order")
    public String placeOrder(
            @RequestParam Long studentId,
            @RequestParam Long itemId,
            @RequestParam int quantity,
            @RequestParam String paymentMethod,
            RedirectAttributes redirectAttributes) { // Uses RedirectAttributes to safely pass status

        try {
            // SIMULATION: Creates a placeholder Student object (needed for Order FK and EmailNotification)
            Student student = new Student();
            student.setUserId(studentId);
            student.setEmail("studenthub.project18@gmail.com");

            // Triggers the backend business logic and atomic transaction
            orderManager.placeOrder(student, itemId, quantity, paymentMethod);

            // SUCCESS REDIRECT: Order placed successfully
            String successMsg = "Order placed successfully! Stock updated. Check console for notification.";

            redirectAttributes.addAttribute("statusMessage", successMsg);
            redirectAttributes.addAttribute("statusType", "success");

            return "redirect:/dashboard";

        } catch (IllegalStateException e) {
            // FAILURE REDIRECT: Catches the Stock Validation Algorithm failure
            String errorMsg = "Order Failed: " + e.getMessage();

            redirectAttributes.addAttribute("statusMessage", errorMsg);
            redirectAttributes.addAttribute("statusType", "error");

            return "redirect:/dashboard";
        }
    }
}