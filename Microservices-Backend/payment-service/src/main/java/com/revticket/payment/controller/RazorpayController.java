package com.revticket.payment.controller;

import com.revticket.payment.dto.RazorpayOrderRequest;
import com.revticket.payment.dto.RazorpayOrderResponse;
import com.revticket.payment.dto.RazorpayVerificationRequest;
import com.revticket.payment.service.RazorpayService;
import com.revticket.payment.util.JwtUtil;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/razorpay")
public class RazorpayController {

    private static final Logger logger = LoggerFactory.getLogger(RazorpayController.class);

    @Autowired
    private RazorpayService razorpayService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "payment-service-razorpay");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@Valid @RequestBody RazorpayOrderRequest request) {
        try {
            logger.info("Creating Razorpay order for amount: {}", request.getAmount());
            RazorpayOrderResponse response = razorpayService.createOrder(request);
            logger.info("Razorpay order created successfully: {}", response.getOrderId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to create Razorpay order", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to create Razorpay order: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Verify payment - migrated from monolithic version
     * Extracts userId from JWT Authorization header (same as SecurityUtil did in
     * monolithic)
     */
    @PostMapping("/verify-payment")
    public ResponseEntity<?> verifyPayment(
            @Valid @RequestBody RazorpayVerificationRequest request,
            @RequestHeader("Authorization") String authHeader) {
        try {
            // Extract userId from JWT token (equivalent to
            // securityUtil.getCurrentUserId(authentication))
            String userId = extractUserIdFromToken(authHeader);

            logger.info("Verifying payment for user: {}, order: {}", userId, request.getRazorpayOrderId());

            Map<String, Object> result = razorpayService.verifyPaymentAndCreateBooking(userId, request);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Payment verified successfully");
            response.put("bookingId", result.get("bookingId"));
            response.put("ticketNumber", result.get("ticketNumber"));

            logger.info("Payment verified successfully for booking: {}", result.get("bookingId"));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Payment verification failed: {}", e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("success", "false");
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/payment-failed")
    public ResponseEntity<?> paymentFailed(
            @RequestBody RazorpayVerificationRequest request,
            @RequestHeader("Authorization") String authHeader) {
        try {
            // Extract userId from JWT token
            String userId = extractUserIdFromToken(authHeader);

            logger.info("Recording payment failure for user: {}, order: {}", userId, request.getRazorpayOrderId());
            razorpayService.handlePaymentFailure(userId, request);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Payment failure recorded");
            logger.info("Payment failure recorded successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to record payment failure", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Extract userId from JWT token - equivalent to SecurityUtil.getCurrentUserId()
     * in monolithic
     */
    private String extractUserIdFromToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Authorization token is required");
        }
        String token = authHeader.substring(7);
        return jwtUtil.extractUsername(token);
    }
}
