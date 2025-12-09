package com.controller;
import com.dto.NotificationRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")

public class NotificationController {
        @PostMapping("/send")
        public String sendNotification(@RequestBody NotificationRequest req){
            String result = String.format("Notification to %s : %s", req.getToAccount(), req.getMessage());
            System.out.println(result);
            return result;
        }
}
