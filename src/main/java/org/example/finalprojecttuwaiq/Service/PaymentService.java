package org.example.finalprojecttuwaiq.Service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.example.finalprojecttuwaiq.Api.ApiException;
import org.example.finalprojecttuwaiq.DTO.PaymentRequestDTO;
import org.example.finalprojecttuwaiq.DTO.SubscriptionDTO;
import org.example.finalprojecttuwaiq.Model.BA;
import org.example.finalprojecttuwaiq.Repository.BARepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PaymentService {

    @Value("${moyasar.api.key}")
    private String apiKey;

    private static final String MOYASAR_API_URL = "https://api.moyasar.com/v1/payments";

    private final BARepository baRepository;
    private final Integer yearlyAmount = 600;
    private final Integer monthlyAmount = 99;

    public ResponseEntity<String> processPaymentYearly(Integer baID, PaymentRequestDTO paymentRequestDTO) {
        BA ba = baRepository.findBAById(baID);

        if (ba == null) {
            throw new ApiException("Error, the business analyst does not exist");
        }

        if (ba.getIsSubscribed() && ba.getSubscriptionEndDate().isAfter(LocalDate.now())) {
            throw new ApiException("Error, the current subscription is still active (not expired)");
        }

        String callbackUrl = "http://ba-copilot.eu-central-1.elasticbeanstalk.com/api/v1/payment/callback/yearly/" + baID;


        //create the body
        String requestBody = String.format("source[type]=card&source[name]=%s&source[number]=%s&source[cvc]=%s&source[month]=%s&source[year]=%s&amount=%d&currency=%s&callback_url=%s",
                paymentRequestDTO.getCardName(),
                paymentRequestDTO.getCardNumber(),
                paymentRequestDTO.getCardCvc(),
                paymentRequestDTO.getCardMonth(),
                paymentRequestDTO.getCardYear(),
                (yearlyAmount * 100),
                "SAR",
                callbackUrl);

        //set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(apiKey, "");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        //send the request

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<JsonNode> response = restTemplate.exchange(
                MOYASAR_API_URL,
                HttpMethod.POST,
                entity,
                JsonNode.class);
        // the response body from Moyasar includes the payment process URL as a JSON
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody().toString());
    }

    public ResponseEntity<String> processPaymentMonthly(Integer baID, PaymentRequestDTO paymentRequestDTO) {
        BA ba = baRepository.findBAById(baID);

        if (ba == null) {
            throw new ApiException("Error, the business analyst does not exist");
        }

        if (ba.getIsSubscribed() && ba.getSubscriptionEndDate().isAfter(LocalDate.now())) {
            throw new ApiException("Error, the current subscription is still active (not expired)");
        }

        String callbackUrl = "http://ba-copilot.eu-central-1.elasticbeanstalk.com/api/v1/payment/callback/monthly/" + baID;


        //create the body
        String requestBody = String.format("source[type]=card&source[name]=%s&source[number]=%s&source[cvc]=%s&source[month]=%s&source[year]=%s&amount=%d&currency=%s&callback_url=%s",
                paymentRequestDTO.getCardName(),
                paymentRequestDTO.getCardNumber(),
                paymentRequestDTO.getCardCvc(),
                paymentRequestDTO.getCardMonth(),
                paymentRequestDTO.getCardYear(),
                (monthlyAmount * 100),
                "SAR",
                callbackUrl);

        //set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(apiKey, "");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        //send the request

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<JsonNode> response = restTemplate.exchange(
                MOYASAR_API_URL,
                HttpMethod.POST,
                entity,
                JsonNode.class);

        return ResponseEntity.status(response.getStatusCode()).body(response.getBody().toString());
    }

    public String getPaymentStatus(String paymentId) {
        HttpHeaders headers = new HttpHeaders();

        //prepare headers
        headers.setBasicAuth(apiKey, "");
        headers.setContentType(MediaType.APPLICATION_JSON);

        //create the request entity
        HttpEntity<String> entity = new HttpEntity<>(headers);

        //call the api

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(
                MOYASAR_API_URL + "/" + paymentId, HttpMethod.GET, entity, String.class
        );

        //return the response
        return response.getBody();
    }

    public SubscriptionDTO subScribeYearly(Integer baID, String transaction_id, String status, String message) {
        BA ba = baRepository.findBAById(baID);

        if (ba == null) {
            throw new ApiException("Error, the business analyst does not exist");
        }

        //  check the payment status from Moyasar to see if it is a correct call back vs malicious callback
        String response = getPaymentStatus(transaction_id);
        JSONObject paymentStatus = new JSONObject(response);

        // sample from Moyasar
        // {"id":"6bef16fa-37a1-42af-ae0b-3b52e9c441f2"},
        // "status":"paid",
        // "amount":3000000,
        // ...
        // "callback_url":"http://localhost:8080/api/v1/payment/callback/yearly/1",
        // ...
        // "message":"APPROVED",
        // ...

        String moyasarStatus = paymentStatus.getString("status");

        if (!status.equalsIgnoreCase(moyasarStatus)) {
            throw new ApiException("Error, the status received is inconsistent with moyasar");
        }

        // check the payment amount if it is correct
        Integer moyasarAmount = paymentStatus.getInt("amount") / 100; // return the amount without floats

        if (!moyasarAmount.equals(yearlyAmount)) {
            throw new ApiException("Error, the amount " + moyasarAmount + "is not enough for a yearly subscription of " + yearlyAmount);
        }

        // save the subscription to the user if paid
        if (!moyasarStatus.equalsIgnoreCase("PAID")) {
            throw new ApiException("Error, the invoice was not paid");
        }

        SubscriptionDTO subscriptionStatus = new SubscriptionDTO();

        subscriptionStatus.setStatus("Subscribed successfully: Yearly, status: " + message);
        subscriptionStatus.setIsSubscribed(true);
        subscriptionStatus.setSubscriptionStartDate(LocalDate.now());
        subscriptionStatus.setSubscriptionEndDate(LocalDate.now().plusMonths(12));

        ba.setIsSubscribed(subscriptionStatus.getIsSubscribed());
        ba.setSubscriptionStartDate(subscriptionStatus.getSubscriptionStartDate());
        ba.setSubscriptionEndDate(subscriptionStatus.getSubscriptionEndDate());
        baRepository.save(ba);

        //  return subscriptionStatus to the user
        return subscriptionStatus;
    }

    public SubscriptionDTO subScribeMonthly(Integer baID, String transaction_id, String status, String message) {
        BA ba = baRepository.findBAById(baID);

        if (ba == null) {
            throw new ApiException("Error, the business analyst does not exist");
        }

        //  check the payment status from Moyasar to see if it is a correct call back vs malicious callback
        String response = getPaymentStatus(transaction_id);
        JSONObject paymentStatus = new JSONObject(response);

        // sample from Moyasar
        // {"id":"6bef16fa-37a1-42af-ae0b-3b52e9c441f2"},
        // "status":"paid",
        // "amount":3000000,
        // ...
        // "callback_url":"http://localhost:8080/api/v1/payment/callback/yearly/1",
        // ...
        // "message":"APPROVED",
        // ...

        String moyasarStatus = paymentStatus.getString("status");

        if (!status.equalsIgnoreCase(moyasarStatus)) {
            throw new ApiException("Error, the status received is inconsistent with moyasar");
        }

        // check the payment amount if it is correct
        Integer moyasarAmount = paymentStatus.getInt("amount") / 100; // return the amount without floats

        if (!moyasarAmount.equals(monthlyAmount)) {
            throw new ApiException("Error, the amount " + moyasarAmount + "is not enough for a monthly subscription of " + monthlyAmount);
        }

        // save the subscription to the user if paid
        if (!moyasarStatus.equalsIgnoreCase("PAID")) {
            throw new ApiException("Error, the invoice was not paid");
        }

        SubscriptionDTO subscriptionStatus = new SubscriptionDTO();

        subscriptionStatus.setStatus("Subscribed successfully: Monthly, status: " + message);
        subscriptionStatus.setIsSubscribed(true);
        subscriptionStatus.setSubscriptionStartDate(LocalDate.now());
        subscriptionStatus.setSubscriptionEndDate(LocalDate.now().plusMonths(1));

        ba.setIsSubscribed(subscriptionStatus.getIsSubscribed());
        ba.setSubscriptionStartDate(subscriptionStatus.getSubscriptionStartDate());
        ba.setSubscriptionEndDate(subscriptionStatus.getSubscriptionEndDate());
        baRepository.save(ba);

        //  return subscriptionStatus to the user
        return subscriptionStatus;

    }

    public SubscriptionDTO getSubscriptionStatus(Integer baID) {
        BA ba = baRepository.findBAById(baID);

        if (ba == null) {
            throw new ApiException("Error, the business analyst does not exist");
        }

        SubscriptionDTO subscriptionStatus = new SubscriptionDTO();

        // check and invalidate subscription if it has ended
        if(ba.getSubscriptionEndDate() != null && ba.getSubscriptionEndDate().isBefore(LocalDate.now())) {
            // the subscription has ended
            ba.setIsSubscribed(false);
            baRepository.save(ba); // plus +, keep the date for user reference

        }

        if (ba.getIsSubscribed()){
            subscriptionStatus.setStatus("Subscription is valid");
        } else if (ba.getSubscriptionEndDate() == null){
            subscriptionStatus.setStatus("You are not subscribed yet");
        } else { // it will be false if the user canceled it, even with a valid date (end-date > today)
            // and that is to be expected (please do not change this logic).
            // when in doubt, the user shall contact customer service for help
            subscriptionStatus.setStatus("Subscription ended, please subscribe again");
        }

        subscriptionStatus.setIsSubscribed(ba.getIsSubscribed());
        subscriptionStatus.setSubscriptionStartDate(ba.getSubscriptionStartDate());
        subscriptionStatus.setSubscriptionEndDate(ba.getSubscriptionEndDate());

        return subscriptionStatus;
    }

    public void cancelSubscription(Integer baID) {
        // the front-end must show "are you sure?" confirmation message before calling this
        BA ba = baRepository.findBAById(baID);

        if (ba == null) {
            throw new ApiException("Error, the business analyst does not exist");
        }

        ba.setIsSubscribed(false);

        // maybe it is best to keep the date for technical support reference if canceled by mistake:
//        ba.setSubscriptionStartDate(null);
//        ba.setSubscriptionEndDate(null);

        baRepository.save(ba);
    }
}

