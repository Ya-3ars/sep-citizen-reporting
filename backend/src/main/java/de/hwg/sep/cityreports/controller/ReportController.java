package de.hwg.sep.cityreports.controller;

import de.hwg.sep.cityreports.dto.CreateReportRequest;
import de.hwg.sep.cityreports.dto.UpdateReportRequest;
import de.hwg.sep.cityreports.entity.Report;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Reports + their photos. Photo routes are sub-resources of a report
 * (no standalone PhotoController, by the YAGNI decision in the architecture).
 *
 * Endpoints are stubbed (HTTP 501) until ReportService / PhotoService land.
 */
@RestController
@RequestMapping("/api/reports")
public class ReportController {

    // ── Reports ────────────────────────────────────────────────

    /** Public: citizen submits a report. Server sets id, status=OPEN, created_at. */
    @PostMapping
    public ResponseEntity<Report> create(@RequestBody CreateReportRequest body) {
        return ResponseEntity.status(501).build();
    }

    /** Staff: paginated list, optionally filtered by status/category. */
    @GetMapping
    public ResponseEntity<List<Report>> list(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.status(501).build();
    }

    /** Staff: one report's full detail. 404 if id unknown. */
    @GetMapping("/{id}")
    public ResponseEntity<Report> getOne(@PathVariable Long id) {
        return ResponseEntity.status(501).build();
    }

    /** Staff: edit a report. Server sets updated_at and updated_by. */
    @PutMapping("/{id}")
    public ResponseEntity<Report> update(@PathVariable Long id, @RequestBody UpdateReportRequest body) {
        return ResponseEntity.status(501).build();
    }

    /** Staff: delete a report. Photos are cascade-deleted by the DB. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return ResponseEntity.status(501).build();
    }

    // ── Photo sub-routes ───────────────────────────────────────

    /** Public: attach a photo to a report. */
    @PostMapping("/{id}/photo")
    public ResponseEntity<?> uploadPhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.status(501).build();
    }

    /** Public: download/display the report's photo. 404 if none attached. */
    @GetMapping("/{id}/photo")
    public ResponseEntity<byte[]> getPhoto(@PathVariable Long id) {
        return ResponseEntity.status(501).build();
    }
}
