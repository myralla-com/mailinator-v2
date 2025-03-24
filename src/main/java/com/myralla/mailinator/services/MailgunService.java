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
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("from", emailDTO.getSender());
        formData.add("to", emailDTO.getRecepient());
        formData.add("subject", emailDTO.getSubject());
        formData.add("text", emailDTO.getBody());
        String auth = "api:" + mailgunApiKey;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("Authorization", "Basic " + encodedAuth);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);

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
            if (response.getStatusCode().is2xxSuccessful()) {

            }
        } catch (HttpClientErrorException e) {
            log.error("Error sending email: {}", e.getMessage());
        }
    }
}
