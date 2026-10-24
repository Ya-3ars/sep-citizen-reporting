package de.hwg.sep.cityreports.repository;

import de.hwg.sep.cityreports.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

    /** A report has at most one photo in the UI, though the schema allows many. */
    Optional<Photo> findFirstByReportId(Long reportId);
}
