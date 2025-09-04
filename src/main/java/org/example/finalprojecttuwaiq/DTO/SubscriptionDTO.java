package org.example.finalprojecttuwaiq.DTO;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionDTO {
    // output the data to the end user

    String status;
    Boolean isSubscribed;
    LocalDate subscriptionStartDate;
    LocalDate subscriptionEndDate;

}
