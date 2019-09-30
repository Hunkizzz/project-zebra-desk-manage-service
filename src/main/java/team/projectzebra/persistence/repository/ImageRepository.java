package team.projectzebra.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import team.projectzebra.persistence.entity.Image;

import java.util.UUID;

public interface ImageRepository extends CrudRepository<Image, UUID> {
}
