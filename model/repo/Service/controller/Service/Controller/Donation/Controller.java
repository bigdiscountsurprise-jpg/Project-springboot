package com.aiqueen.controller;
import com.aiqueen.service.DonationService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import java.util.Map;

@RestController
@RequestMapping("/api/donate")
public class DonationController {
  private final DonationService ds;
  @Value("${server.port}")
  private String port;
  @Value("${server.address:}")
  private String address;
  @Value("${aiq.donation-static-url:}")
  private String staticUrl;

  public DonationController(DonationService ds){ this.ds = ds; }

  @PostMapping
  public ResponseEntity<?> donate(@RequestBody Map<String,Object> body){
    try {
      if (staticUrl != null && !staticUrl.isBlank()) return ResponseEntity.ok(Map.of("ok", true, "url", staticUrl));
      Double amount = ((Number)body.getOrDefault("amount",0)).doubleValue();
      String currency = (String) body.getOrDefault("currency","usd");
      String desc = (String) body.getOrDefault("description","Donation to AI QUEEN");
      long cents = Math.max(50L, Math.round(amount * 100)); // min 50c
      String base = "http://localhost:" + (port==null? "8080":port);
      String url = ds.createCheckout(cents, currency, desc, base);
      return ResponseEntity.ok(Map.of("ok",true,"url",url));
    } catch (Exception e) {
      return ResponseEntity.status(500).body(Map.of("ok",false,"error", e.getMessage()));
    }
  }
}
