package org.example.finalprojecttuwaiq.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.finalprojecttuwaiq.Api.ApiResponse;
import org.example.finalprojecttuwaiq.DTO.PaymentRequestDTO;
import org.example.finalprojecttuwaiq.Model.User;
import org.example.finalprojecttuwaiq.Service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("/status/{paymentId}")
    public ResponseEntity<?> getPaymentStatus(@PathVariable String paymentId){
    return ResponseEntity.ok(paymentService.getPaymentStatus(paymentId));
    }

    @PostMapping("/monthly")
    public ResponseEntity<?> processPaymentMonthly(@AuthenticationPrincipal User user, @RequestBody @Valid PaymentRequestDTO paymentRequestDTO){

        return ResponseEntity.ok(paymentService.processPaymentMonthly(user.getId(), paymentRequestDTO));
    }
    @PostMapping("/yearly")
    public ResponseEntity<?> processPaymentYearly( @AuthenticationPrincipal User user, @RequestBody @Valid PaymentRequestDTO paymentRequestDTO){
        return ResponseEntity.ok(paymentService.processPaymentYearly(user.getId(), paymentRequestDTO));
    }

    @GetMapping("/callback/yearly/{baID}")
    public ResponseEntity<?> subScribeYearly(@PathVariable Integer baID,
                                             @RequestParam(name = "id") String transaction_id,
                                             @RequestParam(name = "status") String status,
                                             @RequestParam(name = "message") String message){

        // sample URL from moyasar:
        /*http://localhost:8080/api/v1/payment/callback/monthly/1
        ?id=631e541a-5916-40f3-a0e6-295130cb2307
        &status=paid
        &message=APPROVED*/

        return ResponseEntity.ok(paymentService.subScribeYearly(baID, transaction_id, status, message));
    }

    @GetMapping("/callback/monthly/{baID}")
    public ResponseEntity<?> subScribeMonthly(@PathVariable Integer baID,
                                              @RequestParam(name = "id") String transaction_id,
                                              @RequestParam(name = "status") String status,
                                              @RequestParam(name = "message") String message){

        return ResponseEntity.ok(paymentService.subScribeMonthly(baID, transaction_id, status, message));
    }

    @GetMapping("/subscription/status")
    public ResponseEntity<?> getSubscriptionStatus(@AuthenticationPrincipal User user){
        return ResponseEntity.ok(paymentService.getSubscriptionStatus(user.getId()));
    }

    @PutMapping("/subscription/cancel")
    public ResponseEntity<?> cancelSubscription(@AuthenticationPrincipal User user){
        paymentService.cancelSubscription(user.getId());
        return ResponseEntity.ok(new ApiResponse("subscription canceled successfully"));
    }
}
