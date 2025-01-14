package application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import application.models.Image;

public interface ImageRepo extends JpaRepository<Image, Integer> {
}