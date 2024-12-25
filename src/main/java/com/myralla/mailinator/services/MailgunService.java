package com.myralla.mailinator.services;

import com.myralla.mailinator.config.MailgunConfig;
import com.myralla.mailinator.dto.EmailDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;


@Slf4j
@Service
public class MailgunService {
    private final MailgunConfig mailgunConfig;
    private final RestClient restClient;

    public MailgunService(MailgunConfig mailgunConfig, RestClient restClient) {
        this.mailgunConfig = mailgunConfig;
        this.restClient = restClient;
    }

//    TODO: Add Error Handling
    public ResponseEntity<Object> sendEmail(EmailDTO emailDTO) {
        String url = mailgunConfig.getApi().getUrl();
        String key = mailgunConfig.getApi().getKey();
        String sender = mailgunConfig.getSender().getDefaultEmail();

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("api", key);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<EmailDTO> request = new HttpEntity<>(emailDTO, headers);
        String response = restClient.post().uri(url)
                .body(request)
                .retrieve()
                .body(String.class);

        log.info("Mailgun Response: {}", response);
        return ResponseEntity.ok().body(response);
    }
}
