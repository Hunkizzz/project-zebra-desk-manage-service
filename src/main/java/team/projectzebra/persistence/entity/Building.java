package team.projectzebra.persistence.entity;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.*;

@Data
@Entity
@Table(name = "building")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    private String name;
    private Integer capacity;
    private String city;
    private String street;
    private String building;
}
