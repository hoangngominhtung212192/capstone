package com.tks.gwa.controller;

import com.tks.gwa.entity.Notification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
public interface NotificationWS {

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    ResponseEntity<List<Object>> getListNotificationByAccountID(@RequestParam("pageNumber") int pageNumber,
                                                        @RequestParam("accountID") int accountID);

    @RequestMapping(value = "/addNew", method = RequestMethod.POST)
    ResponseEntity<Notification> addNewNotification(@RequestBody Notification notification);

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    ResponseEntity<String> updateNotificationStatus(@RequestParam("notificationID") int notificationID);
}
