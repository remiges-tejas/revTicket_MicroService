package com.revticket.notification.controller;

import com.revticket.notification.dto.*;
import com.revticket.notification.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/email/send")
    public ResponseEntity<Map<String, String>> sendEmail(@RequestBody EmailRequest request) {
        emailService.sendEmail(request);
        return ResponseEntity.ok(Map.of("message", "Email sent successfully"));
    }

    @PostMapping("/password-reset")
    public ResponseEntity<Map<String, String>> sendPasswordReset(@RequestBody PasswordResetRequest request) {
        emailService.sendPasswordResetEmail(request);
        return ResponseEntity.ok(Map.of("message", "Password reset email sent"));
    }

    @PostMapping("/booking-confirmation")
    public ResponseEntity<Map<String, String>> sendBookingConfirmation(@RequestBody BookingNotificationRequest request) {
        emailService.sendBookingConfirmation(request);
        return ResponseEntity.ok(Map.of("message", "Booking confirmation sent"));
    }

    @PostMapping("/cancellation-confirmation")
    public ResponseEntity<Map<String, String>> sendCancellationConfirmation(@RequestBody BookingNotificationRequest request) {
        emailService.sendCancellationConfirmation(request);
        return ResponseEntity.ok(Map.of("message", "Cancellation confirmation sent"));
    }

    @PostMapping("/admin/new-user")
    public ResponseEntity<Map<String, String>> notifyAdminNewUser(@RequestBody Map<String, String> request) {
        emailService.sendAdminNewUserNotification(request.get("userName"), request.get("userEmail"));
        return ResponseEntity.ok(Map.of("message", "Admin notified"));
    }

    @PostMapping("/admin/new-booking")
    public ResponseEntity<Map<String, String>> notifyAdminNewBooking(@RequestBody BookingNotificationRequest request) {
        emailService.sendAdminNewBookingNotification(request);
        return ResponseEntity.ok(Map.of("message", "Admin notified"));
    }

    @PostMapping("/admin/cancellation-request")
    public ResponseEntity<Map<String, String>> notifyAdminCancellationRequest(@RequestBody CancellationNotificationRequest request) {
        emailService.sendAdminCancellationRequestNotification(request);
        return ResponseEntity.ok(Map.of("message", "Admin notified"));
    }

    @PostMapping("/review-approved")
    public ResponseEntity<Map<String, String>> notifyReviewApproved(@RequestBody Map<String, String> request) {
        emailService.sendReviewApprovedNotification(request.get("movieTitle"));
        return ResponseEntity.ok(Map.of("message", "Notification sent"));
    }
}
