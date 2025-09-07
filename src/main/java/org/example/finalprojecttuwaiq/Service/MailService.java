package org.example.finalprojecttuwaiq.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.example.finalprojecttuwaiq.Model.BA;
import org.example.finalprojecttuwaiq.Model.Document;
import org.example.finalprojecttuwaiq.Model.Stakeholder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final SimpleMailMessage mailMessage = new SimpleMailMessage();


    public void sendApprovalRequestEmail(Stakeholder stakeholder, Document document, BA requester) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setFrom("alimuaffag@gmail.com");
        helper.setTo(stakeholder.getUser().getEmail());
        helper.setSubject("Approval Request: " + document.getTitle());

        String body = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<style>" +
                "  .container { font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px; }" +
                "  .card { background: #ffffff; border-radius: 10px; padding: 20px; box-shadow: 0 2px 6px rgba(0,0,0,0.1); }" +
                "  .title { font-size: 20px; font-weight: bold; color: #333333; margin-bottom: 10px; }" +
                "  .message { font-size: 14px; color: #555555; line-height: 1.6; }" +
                "  .footer { margin-top: 30px; font-size: 12px; color: #888888; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "  <div class='container'>" +
                "    <div class='card'>" +
                "      <div class='title'>Approval Request for: " + document.getTitle() + "</div>" +
                "      <div class='message'>" +
                "        Dear <b>" + stakeholder.getUser().getUsername() + "</b>,<br><br>" +
                "        <b>" + requester.getUser().getUsername() + "</b> has requested your approval for the document: " +
                "        <b>" + document.getTitle() + "</b>.<br><br>" +
                "        Please review the document at your earliest convenience." +
                "      </div>" +
                "      <div class='footer'>" +
                "        Best regards,<br>" +
                "        <i>BA Copilot</i>" +
                "      </div>" +
                "    </div>" +
                "  </div>" +
                "</body>" +
                "</html>";

        helper.setText(body, true);
        mailSender.send(mimeMessage);
    }

    public void sendApprovedEmail(BA requester, Document document, Stakeholder approver, String Comment) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setFrom("alimuaffag@gmail.com");
        helper.setTo(requester.getUser().getEmail());
        helper.setSubject("Document Approved: " + document.getTitle());

        String body = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<style>" +
                "  .container { font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px; }" +
                "  .card { background: #ffffff; border-radius: 10px; padding: 20px; box-shadow: 0 2px 6px rgba(0,0,0,0.1); }" +
                "  .title { font-size: 20px; font-weight: bold; color: #2e7d32; margin-bottom: 10px; }" + // أخضر يرمز للـ approval
                "  .message { font-size: 14px; color: #555555; line-height: 1.6; }" +
                "  .comment { margin-top: 15px; padding: 12px; background: #f1f8e9; border-left: 4px solid #4CAF50; font-style: italic; }" +
                "  .footer { margin-top: 30px; font-size: 12px; color: #888888; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "  <div class='container'>" +
                "    <div class='card'>" +
                "      <div class='title'>Document Approved: " + document.getTitle() + "</div>" +
                "      <div class='message'>" +
                "        Dear <b>" + requester.getUser().getUsername() + "</b>,<br><br>" +
                "        Your document <b>\"" + document.getTitle() + "\"</b> has been approved by " +
                "        <b>" + approver.getUser().getUsername() + "</b>.<br><br>";

        if (Comment != null && !Comment.isBlank()) {
            body += "      <div class='comment'>Comment: " + Comment + "</div>";
        }

        body += "      </div>" +
                "      <div class='footer'>" +
                "        Best regards,<br>" +
                "        <i>BA Copilot</i>" +
                "      </div>" +
                "    </div>" +
                "  </div>" +
                "</body>" +
                "</html>";

        helper.setText(body, true); // true = HTML
        mailSender.send(mimeMessage);
    }

    public void sendRejectedEmail(BA requester, Document document, Stakeholder approver, String Comment) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setFrom("no-reply@yourdomain.com");
        helper.setTo(requester.getUser().getEmail());
        helper.setSubject("Document Rejected: " + document.getTitle());

        String body = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<style>" +
                "  .container { font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px; }" +
                "  .card { background: #ffffff; border-radius: 10px; padding: 20px; box-shadow: 0 2px 6px rgba(0,0,0,0.1); }" +
                "  .title { font-size: 20px; font-weight: bold; color: #c62828; margin-bottom: 10px; }" + // أحمر يدل على رفض
                "  .message { font-size: 14px; color: #555555; line-height: 1.6; }" +
                "  .comment { margin-top: 15px; padding: 12px; background: #ffebee; border-left: 4px solid #e53935; font-style: italic; }" +
                "  .footer { margin-top: 30px; font-size: 12px; color: #888888; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "  <div class='container'>" +
                "    <div class='card'>" +
                "      <div class='title'>Document Rejected: " + document.getTitle() + "</div>" +
                "      <div class='message'>" +
                "        Dear <b>" + requester.getUser().getUsername() + "</b>,<br><br>" +
                "        Unfortunately, your document <b>\"" + document.getTitle() + "\"</b> has been rejected by " +
                "        <b>" + approver.getUser().getUsername() + "</b>.<br><br>";

        if (Comment != null && !Comment.isBlank()) {
            body += "      <div class='comment'>Comment: " + Comment + "</div>";
        }

        body += "      </div>" +
                "      <div class='footer'>" +
                "        Best regards,<br>" +
                "        <i>BA Copilot</i>" +
                "      </div>" +
                "    </div>" +
                "  </div>" +
                "</body>" +
                "</html>";

        helper.setText(body, true); // true = HTML
        mailSender.send(mimeMessage);
    }
}
