package entity;

import enums.Color;
import lombok.*;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@XmlRootElement
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@Table(name = "PERSON")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @DecimalMin(value = "0.0", inclusive = false, message = "height must be > 0")
    @Column(nullable = true)
    private Double height; //Значение поля должно быть больше 0

    @Enumerated(EnumType.STRING)
    //@NotNull(message = "eye color should not be null")
    @Column(nullable = true)
    private Color eyeColor; //Поле не может быть null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    @XmlTransient
    @NotNull(message = "location should not be null")
    private Location location; //Поле может быть null

}
