package com.tks.gwa.controller;

import com.tks.gwa.dto.NotificationDTO;
import com.tks.gwa.entity.Notification;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notification")
public interface NotificationWS {

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    ResponseEntity<NotificationDTO> getListNotificationByAccountID(@RequestParam("pageNumber") int pageNumber,
                                                                   @RequestParam("accountID") int accountID);

    @RequestMapping(value = "/addNew", method = RequestMethod.POST)
    ResponseEntity<Notification> addNewNotification(@RequestBody Notification notification);

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    ResponseEntity<String> updateNotificationStatus(@RequestParam("notificationID") int notificationID);

    @RequestMapping(value = "/send", method = RequestMethod.GET, produces = "application/json")
    ResponseEntity<String> send() throws JSONException;
}
