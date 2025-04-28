package com.myralla.mailinator.services;

import com.myralla.mailinator.config.MailgunConfig;
import com.myralla.mailinator.dto.EmailDTO;
import com.myralla.mailinator.models.EmailHistory;
import com.myralla.mailinator.models.EmailHistoryStatusType;
import com.myralla.mailinator.repositories.EmailHistoryRepository;
import com.myralla.mailinator.repositories.EmailHistoryStatusTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Base64;


@Slf4j
@Service
public class MailgunService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${mailgun.api.url}")
    private String mailgunApiUrl;

    @Value("${mailgun.api.key}")
    private String mailgunApiKey;

    @Autowired
    private EmailHistoryStatusTypeRepository emailHistoryStatusTypeRepository;

    @Autowired
    private EmailHistoryRepository emailHistoryRepository;

    public void sendEmail(EmailDTO emailDTO) {
        log.info("Sending email to: {}", emailDTO.getRecepient());

        // Create HTML email content
        String htmlContent = buildHtmlEmail(emailDTO.getBody(), emailDTO.getSubject());

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("from", emailDTO.getSender());
        formData.add("to", emailDTO.getRecepient());
        formData.add("subject", emailDTO.getSubject());
        formData.add("html", htmlContent); // Changed from "text" to "html"

        String auth = "api:" + mailgunApiKey;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("Authorization", "Basic " + encodedAuth);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);

        // Save to history
        EmailHistoryStatusType emailStatus = emailHistoryStatusTypeRepository.findByStatus("PROCESSING");
        EmailHistory emailHistory = new EmailHistory();
        emailHistory.setRecipient(emailDTO.getRecepient());
        emailHistory.setSubject(emailDTO.getSubject());
        emailHistory.setBody(emailDTO.getBody());
        emailHistory.setEmailHistoryStatusType(emailStatus);
        emailHistoryRepository.save(emailHistory);

        try {
            ResponseEntity<Object> response = restTemplate.postForEntity(mailgunApiUrl, requestEntity, Object.class);
            log.info("Email sent successfully. Response: {}", response);
        } catch (HttpClientErrorException e) {
            log.error("Error sending email: {}", e.getMessage());
        }
    }

    private String buildHtmlEmail(String bodyContent, String subject) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>" + subject + "</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: 'Helvetica Neue', Arial, sans-serif;\n" +
                "            line-height: 1.6;\n" +
                "            color: #333333;\n" +
                "            background-color: #f7f7f7;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "        .email-container {\n" +
                "            max-width: 600px;\n" +
                "            margin: 20px auto;\n" +
                "            background: #ffffff;\n" +
                "            border-radius: 8px;\n" +
                "            overflow: hidden;\n" +
                "            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);\n" +
                "        }\n" +
                "        .header {\n" +
                "            background-color: #ffffff;\n" +
                "            padding: 30px 20px 20px;\n" +
                "            text-align: center;\n" +
                "            border-bottom: 1px solid #f0f0f0;\n" +
                "        }\n" +
                "        .logo {\n" +
                "            max-width: 180px;\n" +
                "            height: auto;\n" +
                "        }\n" +
                "        .content {\n" +
                "            padding: 30px;\n" +
                "            color: #444444;\n" +
                "            font-size: 16px;\n" +
                "        }\n" +
                "        .footer {\n" +
                "            background-color: #f9f9f9;\n" +
                "            padding: 20px;\n" +
                "            text-align: center;\n" +
                "            font-size: 13px;\n" +
                "            color: #777777;\n" +
                "        }\n" +
                "        .button {\n" +
                "            display: inline-block;\n" +
                "            padding: 12px 24px;\n" +
                "            background: linear-gradient(135deg, #6e8efb, #a777e3);\n" +
                "            color: white !important;\n" +
                "            text-decoration: none;\n" +
                "            border-radius: 6px;\n" +
                "            margin: 20px 0;\n" +
                "            font-weight: 600;\n" +
                "        }\n" +
                "        .social-links {\n" +
                "            margin: 20px 0;\n" +
                "        }\n" +
                "        .social-links a {\n" +
                "            margin: 0 10px;\n" +
                "            display: inline-block;\n" +
                "        }\n" +
                "        .divider {\n" +
                "            height: 1px;\n" +
                "            background-color: #eeeeee;\n" +
                "            margin: 20px 0;\n" +
                "        }\n" +
                "        @media only screen and (max-width: 600px) {\n" +
                "            .email-container {\n" +
                "                width: 100%;\n" +
                "                border-radius: 0;\n" +
                "            }\n" +
                "            .content {\n" +
                "                padding: 20px;\n" +
                "            }\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"email-container\">\n" +
                "        <div class=\"header\">\n" +
                "            <img src=\"https://github.com/CJ-Felicitas/opulenz-cdn-test/blob/main/logo.png?raw=true\" alt=\"Opulenz Logo\" class=\"logo\">\n" +
                "        </div>\n" +
                "        \n" +
                "        <div class=\"content\">\n" +
                "            " + bodyContent.replace("\n", "<br>") + "\n" +
                "            \n" +
                "            <div class=\"divider\"></div>\n" +
                "            \n" +
                "            <div class=\"social-links\">\n" +
                "                <a href=\"[FACEBOOK_URL]\"><img src=\"https://cdn-icons-png.flaticon.com/512/124/124010.png\" width=\"24\" alt=\"Facebook\"></a>\n" +
                "                <a href=\"[TWITTER_URL]\"><img src=\"https://cdn-icons-png.flaticon.com/512/733/733579.png\" width=\"24\" alt=\"Twitter\"></a>\n" +
                "                <a href=\"[INSTAGRAM_URL]\"><img src=\"https://cdn-icons-png.flaticon.com/512/2111/2111463.png\" width=\"24\" alt=\"Instagram\"></a>\n" +
                "                <a href=\"[LINKEDIN_URL]\"><img src=\"https://cdn-icons-png.flaticon.com/512/3536/3536505.png\" width=\"24\" alt=\"LinkedIn\"></a>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        \n" +
                "        <div class=\"footer\">\n" +
                "            <p>© " + LocalDate.now().getYear() + " Opulenz. All rights reserved.</p>\n" +
                "            <p>\n" +
                "                <a href=\"[UNSUBSCRIBE_URL]\" style=\"color: #777777; text-decoration: none;\">Unsubscribe</a> | \n" +
                "                <a href=\"[PRIVACY_POLICY_URL]\" style=\"color: #777777; text-decoration: none;\">Privacy Policy</a> | \n" +
                "                <a href=\"[CONTACT_URL]\" style=\"color: #777777; text-decoration: none;\">Contact Us</a>\n" +
                "            </p>\n" +
                "            <p style=\"margin-top: 10px; font-size: 12px; color: #aaaaaa;\">\n" +
                "                Opulenz, [Your Company Address]\n" +
                "            </p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }
}
