package com.aiqueen.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name="campaign")
public class Campaign {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;
  private String title;
  @Column(length=4000) private String brief;
  private String channel;
  private Instant scheduledAt;
  private String status;
  private String ownerId;
  private Instant createdAt = Instant.now();

  // getters & setters...
}
