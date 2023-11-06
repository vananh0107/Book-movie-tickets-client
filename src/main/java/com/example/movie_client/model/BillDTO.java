package com.example.movie_client.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
@Data
public class BillDTO {
    private int id;
    private String createdTime;
    private List<TicketDTO> listTickets;
    private User user;
}
