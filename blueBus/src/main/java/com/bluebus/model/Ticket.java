package com.bluebus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class Ticket {
    private int ticketId;
    private int userId;
    private int paymentId;
    private int busId;
    private int seatNo;
    private Date travellingDate;
    private Date bookedDate;

}
