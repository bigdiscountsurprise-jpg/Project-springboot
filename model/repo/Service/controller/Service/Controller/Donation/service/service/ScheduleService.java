package com.aiqueen.service;

import com.aiqueen.repo.CampaignRepository;
import com.aiqueen.model.Campaign;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;

@Service
public class SchedulerService {
  private final CampaignRepository repo;
  private final TwilioService twilio;
  public SchedulerService(CampaignRepository repo, TwilioService twilio){ this.repo = repo; this.twilio = twilio; }

  // poll every 30 seconds
  @Scheduled(fixedDelay = 30000)
  public void pollAndDispatch() {
    List<Campaign> due = repo.findByStatusAndScheduledAtBefore("SCHEDULED", Instant.now());
    for (Campaign c : due) {
      try {
        // send via twilio (example), or call connectors
        if ("whatsapp".equalsIgnoreCase(c.getChannel())) {
          // The recipient list/logic must be defined elsewhere, and only for consented recipients
          twilio.sendWhatsApp("+1234567890", c.getBrief());
        }
        c.setStatus("SENT");
      } catch (Exception ex) {
        c.setStatus("FAILED");
      } finally {
        repo.save(c);
      }
    }
  }
}
