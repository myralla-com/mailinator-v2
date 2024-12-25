package com.myralla.mailinator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "mailgun")
public class MailgunConfig {
    private Api api;
    private Sender sender;

    public static class Api {
        private String url;
        private String key;

        // Getters and setters
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
        public String getKey() { return key; }
        public void setKey(String key) { this.key = key; }
    }

    public static class Sender{
        private String defaultEmail;

        // Getters and setters
        public String getDefaultEmail() { return defaultEmail; }
        public void setDefaultEmail(String defaultEmail) { this.defaultEmail = defaultEmail; }
    }

    public Api getApi(){return api;}
    public void setApi(Api api){this.api = api;}
    public Sender getSender(){return sender;}
    public void setSender(Sender sender){this.sender = sender;}
}