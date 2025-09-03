package org.example.finalprojecttuwaiq.DTO;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDTO {

    @NotEmpty(message = "Card name cannot be empty")
    private String cardName;

    @NotEmpty(message = "Card number cannot be empty")
    private String cardNumber;

    @NotEmpty(message = "Card CVC cannot be empty")
    private String cardCvc;

    @NotEmpty(message = "Card month cannot be empty")
    private String cardMonth;

    @NotEmpty(message = "Card year cannot be null")
    private String cardYear;
}
