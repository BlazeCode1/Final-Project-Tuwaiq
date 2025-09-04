package org.example.finalprojecttuwaiq.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.finalprojecttuwaiq.Api.ApiResponse;
import org.example.finalprojecttuwaiq.DTO.PaymentRequestDTO;
import org.example.finalprojecttuwaiq.Service.PaymentService;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/monthly/{baID}")
    public ResponseEntity<?> processPaymentMonthly(@PathVariable Integer baID, @RequestBody @Valid PaymentRequestDTO paymentRequestDTO){

        return ResponseEntity.ok(paymentService.processPaymentMonthly(baID, paymentRequestDTO));
    }
    @PostMapping("/yearly/{baID}")
    public ResponseEntity<?> processPaymentYearly(@PathVariable Integer baID, @RequestBody @Valid PaymentRequestDTO paymentRequestDTO){
        return ResponseEntity.ok(paymentService.processPaymentYearly(baID, paymentRequestDTO));
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

    @GetMapping("/subscription/status/{baID}")
    public ResponseEntity<?> getSubscriptionStatus(@PathVariable Integer baID){
        return ResponseEntity.ok(paymentService.getSubscriptionStatus(baID));
    }

    @PostMapping("/subscription/cancel/{baID}")
    public ResponseEntity<?> cancelSubscription(@PathVariable Integer baID){
        paymentService.cancelSubscription(baID);
        return ResponseEntity.ok(new ApiResponse("subscription canceled successfully"));
    }
}
