package com.hospital.controller;

import com.hospital.dto.ApiResponse;
import com.hospital.dto.MedicalRecordRequest;
import com.hospital.entity.MedicalRecord;
import com.hospital.service.MedicalRecordService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-records")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<List<MedicalRecord>>> getAllRecords() {
        return ResponseEntity.ok(ApiResponse.ok("Records fetched", medicalRecordService.getAllRecords()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MedicalRecord>> getRecordById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Record fetched", medicalRecordService.getRecordById(id)));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<ApiResponse<List<MedicalRecord>>> getRecordsByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(ApiResponse.ok("Patient records fetched", medicalRecordService.getRecordsByPatient(patientId)));
    }

    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DOCTOR')")
    public ResponseEntity<ApiResponse<List<MedicalRecord>>> getRecordsByDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(ApiResponse.ok("Doctor records fetched", medicalRecordService.getRecordsByDoctor(doctorId)));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DOCTOR')")
    public ResponseEntity<ApiResponse<MedicalRecord>> createRecord(@Valid @RequestBody MedicalRecordRequest request) {
        MedicalRecord created = medicalRecordService.createRecord(request);
        return ResponseEntity.ok(ApiResponse.ok("Medical record created successfully", created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DOCTOR')")
    public ResponseEntity<ApiResponse<MedicalRecord>> updateRecord(@PathVariable Long id, @Valid @RequestBody MedicalRecordRequest request) {
        MedicalRecord updated = medicalRecordService.updateRecord(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Medical record updated successfully", updated));
    }
}
