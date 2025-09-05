package org.example.finalprojecttuwaiq.Service;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.example.finalprojecttuwaiq.Api.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WhatsappService {

    @Value("${whatsapp.api.token}")
    private String token;

    @Value("${whatsapp.api.instance.id}")
    private String instanceId;

    public void sendMessage(String to, String message) {

        if (to.isEmpty()){
            throw new ApiException("Error, please enter a phone number");
        }

        if (to.length()<10 ||
                !to.matches("^\\+?(\\d{1,3})?\\(?\\d{1,4}\\)?[\\s.-]?\\d{1,4}[\\s.-]?\\d{1,9}$\n")){
            throw new ApiException("Error, please enter a correct phone number");

        }

        if (message.isEmpty()){
            throw new ApiException("Error, please enter a message");
        }

        try {
            HttpResponse<String> response =
                    Unirest.post("https://api.ultramsg.com/"+instanceId+"/messages/chat")
                                    .header("Content-Type", "application/x-www-form-urlencoded")
                                    .field("token", token)
                                    .field("to", to.trim().replaceAll("\\+",""))
                                    .field("body", message)
                                    .asString();
        } catch (Exception e) {
            throw new ApiException("Failed to send WhatsApp message: " + e.getMessage());
        }
    }

}
