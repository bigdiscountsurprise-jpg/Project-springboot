package com.aiqueen.repo;
import com.aiqueen.model.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.Instant;
import java.util.List;

public interface CampaignRepository extends JpaRepository<Campaign,Long> {
  List<Campaign> findByStatusAndScheduledAtBefore(String status, Instant ts);
}
