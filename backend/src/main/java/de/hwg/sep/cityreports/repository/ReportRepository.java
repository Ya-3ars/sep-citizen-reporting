package de.hwg.sep.cityreports.repository;

import de.hwg.sep.cityreports.entity.Category;
import de.hwg.sep.cityreports.entity.Report;
import de.hwg.sep.cityreports.entity.ReportStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {

    Page<Report> findByStatus(ReportStatus status, Pageable pageable);

    Page<Report> findByCategory(Category category, Pageable pageable);

    Page<Report> findByStatusAndCategory(ReportStatus status, Category category, Pageable pageable);
}
