package it.unipd.pdp2021.mnaut;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

class GameStatus {
  public final String status;

  GameStatus(String status) {
    this.status = status;
  }

  public String toJson(ObjectMapper mapper) {
    String res = "{}";
    try {
      res = mapper.writeValueAsString(this);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return res;
  }
}
