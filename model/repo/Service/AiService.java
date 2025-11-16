package com.aiqueen.service;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.Duration;

@Service
public class AiService {
  private final OkHttpClient client = new OkHttpClient.Builder().callTimeout(Duration.ofSeconds(20)).build();

  @Value("${aiq.openai-key:}")
  private String openaiKey;

  @Value("${aiq.node-url:}")
  private String nodeUrl;

  public String ask(String prompt) throws IOException {
    if (nodeUrl != null && !nodeUrl.isBlank()) {
      RequestBody body = RequestBody.create(MediaType.get("application/json; charset=utf-8"),
          "{\"prompt\":\"" + escape(prompt) + "\"}");
      Request req = new Request.Builder().url(nodeUrl + "/ai").post(body).build();
      try (Response r = client.newCall(req).execute()) {
        if (!r.isSuccessful()) throw new IOException("AI proxy error " + r.code());
        return r.body() != null ? r.body().string() : "";
      }
    }
    if (openaiKey != null && !openaiKey.isBlank()) {
      RequestBody body = RequestBody.create(MediaType.get("application/json"),
        "{\"model\":\"gpt-4o-mini\",\"messages\":[{\"role\":\"user\",\"content\":\""+escape(prompt)+"\"}],\"temperature\":0.2}");
      Request req = new Request.Builder()
        .url("https://api.openai.com/v1/chat/completions")
        .addHeader("Authorization","Bearer "+openaiKey)
        .post(body).build();
      try (Response r = client.newCall(req).execute()) {
        if (!r.isSuccessful()) throw new IOException("OpenAI error " + r.code());
        return r.body() != null ? r.body().string() : "";
      }
    }
    // Safe local fallback
    return simulate(prompt);
  }

  private static String escape(String s){ return s.replace("\\","\\\\").replace("\"","\\\""); }
  private String simulate(String p){
    if (p.toLowerCase().contains("hello")) return "👑 Queenchip: hello. How can I assist?";
    return "👑 Queenchip (simulated): please configure AI backend for live responses.";
  }
}
