package dto.dtoList;

import dto.TicketDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.*;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@NoArgsConstructor
public class TicketDTOList {
    @XmlElementWrapper(name = "tickets")
    @XmlElement(name = "ticket")
    private List<TicketDTO> ticketList;
}
