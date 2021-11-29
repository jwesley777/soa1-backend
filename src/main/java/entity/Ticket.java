package entity;

import enums.TicketType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import xml.LocalDateAdapter;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@NoArgsConstructor
@Entity
@Table(name = "TICKET")
@NamedQuery(name = "Entity.Ticket.getAll", query = "SELECT m FROM Ticket m")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;//Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически

    @NotNull(message = "name should not be null")
    @NotEmpty(message = "name should not be empty")
    private String name;//Поле не может быть null, empty

    @Column(name = "date")
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    @NotNull(message = "creation date should not be null")
    private Date creationDate;//Значение этого поля должно генерироваться автоматически

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coordinate_id")
    @XmlElement(name = "coordinates")
    @NotNull(message = "coordinates should not be null")
    private Coordinates coordinates;//Поле не может быть null

    @Column(name = "discount", nullable = true)
    @DecimalMin(value = "0", inclusive = false, message = "discount must be > 0")
    @DecimalMax(value = "100", inclusive = true, message = "discount must be <= 100")
    private Long discount; //Значение поля должно быть больше 0, Поле может быть null

    @DecimalMin(value = "0", inclusive = false, message = "price must be > 0")
    @Column(nullable = true)
    private Double price;//Значение поля должно быть больше 0

    @Enumerated(EnumType.STRING)
    @NotNull(message = "ticket type should not be null")
    private TicketType type; //Поле может быть null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    @XmlElement(name = "person")
    @NotNull(message = "person should not be null")
    private Person person;//Поле не может быть null
}
