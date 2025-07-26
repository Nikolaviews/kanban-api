package com.projectmanagement.kanbanapi.services;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Notifies clients subscribed to a specific board that the board has been updated.
     * @param boardId The ID of the board that was updated.
     */
    public void notifyBoardUpdated(String boardId) {
        // The message payload can be anything. Here, a simple string is fine.
        // The frontend just needs to know *that* an update happened.
        String destination = "/topic/board/" + boardId;
        messagingTemplate.convertAndSend(destination, "BOARD_UPDATED");
    }
}