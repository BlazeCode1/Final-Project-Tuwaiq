package org.example.finalprojecttuwaiq.Service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.example.finalprojecttuwaiq.DTO.PaymentRequestDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class PaymentService {

    @Value("${moyasar.api.key}")
    private String apiKey;

    private static final String MOYASAR_API_URL = "https://api.moyasar.com/v1/payments";

    public ResponseEntity<String> processPaymentYearly(PaymentRequestDTO paymentRequestDTO) {
        String callbackUrl = "http://localhost:8080/api/v1/invoice/callback";


        //create the body
        String requestBody =String.format("source[type]=card&source[name]=%s&source[number]=%s&source[cvc]=%s&source[month]=%s&source[year]=%s&amount=%d&currency=%s&callback_url=%s",
                paymentRequestDTO.getCardName(),
                paymentRequestDTO.getCardNumber(),
                paymentRequestDTO.getCardCvc(),
                paymentRequestDTO.getCardMonth(),
                paymentRequestDTO.getCardYear(),
                 (600 * 100),
                "SAR",
                callbackUrl);

        //set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(apiKey,"");
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

    public ResponseEntity<String> processPaymentMonthly(PaymentRequestDTO paymentRequestDTO) {


        String callbackUrl = "http://localhost:8080/api/v1/invoice/callback";


        //create the body
        String requestBody =String.format("source[type]=card&source[name]=%s&source[number]=%s&source[cvc]=%s&source[month]=%s&source[year]=%s&amount=%d&currency=%s&callback_url=%s",
                paymentRequestDTO.getCardName(),
                paymentRequestDTO.getCardNumber(),
                paymentRequestDTO.getCardCvc(),
                paymentRequestDTO.getCardMonth(),
                paymentRequestDTO.getCardYear(),
                (99 * 100),
                "SAR",
                callbackUrl);

        //set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(apiKey,"");
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

    public String getPaymentStatus(String paymentId){
        HttpHeaders headers = new HttpHeaders();

        //prepare headers
        headers.setBasicAuth(apiKey,"");
        headers.setContentType(MediaType.APPLICATION_JSON);

        //create the request entity
        HttpEntity<String> entity = new HttpEntity<>(headers);

        //call the api

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(
                MOYASAR_API_URL + "/" + paymentId ,HttpMethod.GET, entity, String.class
        );

        //return the response
        return response.getBody();
    }
}

