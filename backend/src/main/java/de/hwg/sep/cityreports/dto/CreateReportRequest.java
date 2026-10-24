package de.hwg.sep.cityreports.dto;

import de.hwg.sep.cityreports.entity.Category;

import java.math.BigDecimal;

/** Body for POST /api/reports (citizen submission). */
public record CreateReportRequest(
        String title,
        String description,
        Category category,
        BigDecimal latitude,
        BigDecimal longitude
) {}
