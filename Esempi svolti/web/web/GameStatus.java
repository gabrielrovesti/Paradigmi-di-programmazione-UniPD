package it.unipd.pdp2021.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

record GameStatus(String status) {

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
