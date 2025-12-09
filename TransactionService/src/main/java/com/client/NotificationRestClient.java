package com.client;

import com.dto.NotificationRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class NotificationRestClient {

    private final RestTemplate restTemplate;

    public NotificationRestClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String sendNotification(NotificationRequest req) {
        return restTemplate.postForObject(
                "http://NOTIFICATIONSERVICE/api/notifications/send",
                req,
                String.class
        );
    }
}

