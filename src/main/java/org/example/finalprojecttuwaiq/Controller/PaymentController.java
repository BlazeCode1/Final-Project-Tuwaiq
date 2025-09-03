package org.example.finalprojecttuwaiq.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/monthly")
    public ResponseEntity<?> processPaymentMonthly(@RequestBody @Valid PaymentRequestDTO paymentRequestDTO){

        return ResponseEntity.ok(paymentService.processPaymentMonthly(paymentRequestDTO));
    }
    @PostMapping("/yearly")
    public ResponseEntity<?> processPaymentYearly(@RequestBody @Valid PaymentRequestDTO paymentRequestDTO){
        return ResponseEntity.ok(paymentService.processPaymentYearly(paymentRequestDTO));
    }



}
