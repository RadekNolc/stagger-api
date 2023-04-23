package cz.radeknolc.stagger.repository;

import cz.radeknolc.stagger.model.University;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UniversityRepository extends JpaRepository<University, Long> {
}
