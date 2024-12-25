package com.bluebus.controller;

import com.bluebus.model.Bus;
import com.bluebus.model.Login;
import com.bluebus.model.Ticket;
import com.bluebus.model.User;
import com.bluebus.service.BusService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins="http://localhost:5173")
public class HomeController {
    @Autowired
    private BusService ser;

    @PostMapping("/routes")
    public ResponseEntity<List<Bus>> getBus(@RequestBody JsonNode f){
        String s=f.get("source").asText();
        String d=f.get("destination").asText();
//        System.out.println(s+ d);
      List<Bus> r=  ser.getBus(s,d);
      return ResponseEntity.ok(r);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> login(@RequestBody Login l){
        User user=ser.login(l);
        Map<String,String> mp= new HashMap<>();
        if(user!=null)
       {
           mp.put("userId",String.valueOf(user.getUid()));
           mp.put("userName", user.getName());
           return ResponseEntity.ok(mp);
       }
        mp.put("msg","user not found");
       return ResponseEntity.status(204).body(mp);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> adduser(@RequestBody JsonNode u){
        return new ResponseEntity<>(ser.addUser(u), HttpStatus.CREATED);
    }

        @PostMapping("/addTicket")
    public ResponseEntity<String> addTicket(@RequestBody JsonNode j){
        return new ResponseEntity<>(ser.addTicket(j),HttpStatus.CREATED);
    }



    @GetMapping("/tickets/{uid}")
    public ResponseEntity<List<Ticket>> getTicket(@PathVariable int uid){
        return ResponseEntity.ok(ser.getTicket(uid));
    }



}
