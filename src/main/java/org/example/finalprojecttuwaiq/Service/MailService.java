package org.example.finalprojecttuwaiq.Service;

import lombok.RequiredArgsConstructor;
import org.example.finalprojecttuwaiq.Model.BA;
import org.example.finalprojecttuwaiq.Model.Document;
import org.example.finalprojecttuwaiq.Model.Stakeholder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final SimpleMailMessage mailMessage = new SimpleMailMessage();

    public void sendApprovalRequestEmail(Stakeholder stakeholder, Document document, BA requester) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("alimuaffag@gmail.com");
        mailMessage.setTo(stakeholder.getUser().getEmail());
        mailMessage.setSubject("Approval Request: " + document.getTitle());

        String body = "Dear " + stakeholder.getUser().getName() + ",\n\n"
                + requester.getUser().getName()+ " has requested your approval for the document: "
                + document.getTitle() + ".\n\n"
                + "Please review and approve.\n\n"
                + "Best regards,\n"
                + "Document Management System";
        mailMessage.setText(body);
        mailSender.send(mailMessage);
    }
}
