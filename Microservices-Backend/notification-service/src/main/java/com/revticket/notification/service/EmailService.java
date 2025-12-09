package com.revticket.notification.service;

import com.revticket.notification.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.admin.email}")
    private String adminEmail;

    public void sendEmail(EmailRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(request.getTo());
        message.setSubject(request.getSubject());
        message.setText(request.getBody());
        mailSender.send(message);
    }

    public void sendPasswordResetEmail(PasswordResetRequest request) {
        String resetUrl = frontendUrl + "/auth/reset-password?token=" + request.getResetToken();
        
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(request.getEmail());
        message.setSubject("RevTicket - Password Reset Request");
        message.setText(buildResetEmailBody(resetUrl));
        
        mailSender.send(message);
    }

    private String buildResetEmailBody(String resetUrl) {
        return "Hello,\n\n" +
               "You have requested to reset your password for your RevTicket account.\n\n" +
               "Please click the link below to reset your password:\n" +
               resetUrl + "\n\n" +
               "This link will expire in 1 hour.\n\n" +
               "If you did not request this password reset, please ignore this email.\n\n" +
               "Best regards,\n" +
               "RevTicket Team";
    }

    public void sendBookingConfirmation(BookingNotificationRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(request.getCustomerEmail());
        message.setSubject("Booking Confirmed - " + request.getMovieTitle());
        message.setText(buildBookingConfirmationBody(request));
        mailSender.send(message);
    }

    public void sendCancellationConfirmation(BookingNotificationRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(request.getCustomerEmail());
        message.setSubject("Booking Cancelled - " + request.getMovieTitle());
        message.setText(buildCancellationBody(request));
        mailSender.send(message);
    }

    private String buildBookingConfirmationBody(BookingNotificationRequest request) {
        String seats = request.getSeats() != null ? String.join(", ", request.getSeats()) : "";

        return "Dear " + request.getCustomerName() + ",\n\n" +
               "Your booking has been confirmed!\n\n" +
               "Booking Details:\n" +
               "Ticket Number: " + request.getTicketNumber() + "\n" +
               "Movie: " + request.getMovieTitle() + "\n" +
               "Theater: " + request.getTheaterName() + "\n" +
               "Screen: " + request.getScreenName() + "\n" +
               "Showtime: " + request.getShowDateTime() + "\n" +
               "Seats: " + seats + "\n" +
               "Total Amount: ₹" + request.getTotalAmount() + "\n\n" +
               "Please arrive 30 minutes before showtime.\n\n" +
               "View your ticket: " + frontendUrl + "/user/my-bookings\n\n" +
               "Enjoy your movie!\n\n" +
               "Best regards,\n" +
               "RevTicket Team";
    }

    private String buildCancellationBody(BookingNotificationRequest request) {
        return "Dear " + request.getCustomerName() + ",\n\n" +
               "Your booking has been cancelled.\n\n" +
               "Booking Details:\n" +
               "Ticket Number: " + request.getTicketNumber() + "\n" +
               "Movie: " + request.getMovieTitle() + "\n" +
               "Refund Amount: ₹" + (request.getRefundAmount() != null ? request.getRefundAmount() : 0) + "\n\n" +
               "The refund will be processed within 5-7 business days.\n\n" +
               "Best regards,\n" +
               "RevTicket Team";
    }

    public void sendAdminNewUserNotification(String userName, String userEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(adminEmail);
        message.setSubject("New User Registration - RevTicket");
        message.setText("New user registered:\n\n" +
                       "Name: " + userName + "\n" +
                       "Email: " + userEmail + "\n\n" +
                       "Login to admin panel to view details.");
        mailSender.send(message);
    }

    public void sendAdminNewBookingNotification(BookingNotificationRequest request) {
        String seats = request.getSeats() != null ? String.join(", ", request.getSeats()) : "";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(adminEmail);
        message.setSubject("New Booking - " + request.getMovieTitle());
        message.setText("New booking received:\n\n" +
                       "Ticket Number: " + request.getTicketNumber() + "\n" +
                       "Customer: " + request.getCustomerName() + " (" + request.getCustomerEmail() + ")\n" +
                       "Movie: " + request.getMovieTitle() + "\n" +
                       "Theater: " + request.getTheaterName() + "\n" +
                       "Screen: " + request.getScreenName() + "\n" +
                       "Showtime: " + request.getShowDateTime() + "\n" +
                       "Seats: " + seats + "\n" +
                       "Amount: ₹" + request.getTotalAmount() + "\n\n" +
                       "Login to admin panel to view details.");
        mailSender.send(message);
    }

    public void sendAdminCancellationRequestNotification(CancellationNotificationRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(adminEmail);
        message.setSubject("Cancellation Request - " + request.getTicketNumber());
        message.setText("Cancellation request received:\n\n" +
                       "Ticket Number: " + request.getTicketNumber() + "\n" +
                       "Customer: " + request.getCustomerName() + " (" + request.getCustomerEmail() + ")\n" +
                       "Movie: " + request.getMovieTitle() + "\n" +
                       "Reason: " + request.getReason() + "\n\n" +
                       "Login to admin panel to approve/reject.");
        mailSender.send(message);
    }

    public void sendReviewApprovedNotification(String movieTitle) {
        // This could be sent to users who submitted reviews
        // For now, just log it
        System.out.println("Review approved for movie: " + movieTitle);
    }
}
