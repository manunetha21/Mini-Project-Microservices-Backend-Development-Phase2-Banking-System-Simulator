package com.controller;

import com.dto.NotificationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

//import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
public class NotificationController {
    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper mapper;

    @Test
    void sendNotification_returnsMessage() throws Exception {
        NotificationRequest req = new NotificationRequest("RS0987", "Deposit successful");

        mockMvc.perform(post("/api/notifications/send")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Notification to RS0987")));
    }
}
