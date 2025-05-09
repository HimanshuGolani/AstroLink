package com.astrolink.AstroLink.scheduler;

import com.astrolink.AstroLink.service.impl.ConsultationRequestServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class ChatCleanUpScheduler {

    private final ConsultationRequestServiceImpl consultationRequestService;

    @Scheduled(cron = "0 0 2 * * ?")  // Run at 2 AM every day
    public void findUnUsedChatsAndRemoveThem(){
        consultationRequestService.findAndRemoveInactiveChats();
    }
}
