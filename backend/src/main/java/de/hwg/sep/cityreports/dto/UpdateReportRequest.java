package de.hwg.sep.cityreports.dto;

import de.hwg.sep.cityreports.entity.Category;
import de.hwg.sep.cityreports.entity.ReportStatus;

/** Body for PUT /api/reports/{id} (staff edit). All fields optional. */
public record UpdateReportRequest(
        String title,
        String description,
        Category category,
        ReportStatus status
) {}
