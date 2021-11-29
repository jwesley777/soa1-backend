package dto;

import entity.Ticket;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PagedTicketList {
    private List<Ticket> ticketList;
    private Long count;
}
