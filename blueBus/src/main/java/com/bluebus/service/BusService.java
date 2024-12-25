package com.bluebus.service;

import com.bluebus.model.Bus;
import com.bluebus.model.Login;
import com.bluebus.model.Ticket;
import com.bluebus.model.User;
import com.bluebus.repository.Repo;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusService {
    @Autowired
    private Repo repo;
    public List<Bus> getBus(String s, String d) {

        return repo.getBus(s,d);
    }

    public String addUser(JsonNode u) {
        return repo.adduser(u.get("name").asText(),u.get("email").asText(),u.get("password").asText(),u.get("phoneNumber").asText());
    }

    public String addTicket(JsonNode t) {

        return repo.addTicket(t.get("userId").asInt(),t.get("busId").asInt(),t.get("bookingDate").asText(),t.get("seatNo").asInt());
    }

    public User login(Login l) {
        return repo.login(l);
    }
    public List<Ticket> getTicket(int id){

        return repo.getTicket(id);
    }

}
