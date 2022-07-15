package it.unipd.pdp2021.mnaut;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

class GameLocation {
  public final String game;

  GameLocation(String id) {
    this.game = "/game/" + id;
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
