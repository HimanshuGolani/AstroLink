package com.astrolink.AstroLink.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    @EventListener
    public void handelWebSocketDisconnectListener(SessionDisconnectEvent event) {
//             TODO: Delete the chat room and remove that from the db as well.
    }

}
