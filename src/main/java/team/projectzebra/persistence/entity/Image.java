package team.projectzebra.persistence.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
public class Image {

    @ApiModelProperty(notes = "The Java generated image uuid")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @Column(name="image")
    private byte[] image;
}

