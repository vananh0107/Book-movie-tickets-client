package com.example.movie_client.model;

import java.time.LocalDateTime;
import java.util.List;

public class BillDTO {
    private int id;
    private LocalDateTime createdTime;
    private List<TicketDTO> listTickets;
    private User user;
}
