package com.hospital.controller;

import com.hospital.dto.ApiResponse;
import com.hospital.dto.PrescriptionRequest;
import com.hospital.entity.Prescription;
import com.hospital.service.PrescriptionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<List<Prescription>>> getAllPrescriptions() {
        return ResponseEntity.ok(ApiResponse.ok("Prescriptions fetched", prescriptionService.getAllPrescriptions()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Prescription>> getPrescriptionById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Prescription fetched", prescriptionService.getPrescriptionById(id)));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<ApiResponse<List<Prescription>>> getPrescriptionsByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(ApiResponse.ok("Patient prescriptions fetched", prescriptionService.getPrescriptionsByPatient(patientId)));
    }

    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DOCTOR')")
    public ResponseEntity<ApiResponse<List<Prescription>>> getPrescriptionsByDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(ApiResponse.ok("Doctor prescriptions fetched", prescriptionService.getPrescriptionsByDoctor(doctorId)));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DOCTOR')")
    public ResponseEntity<ApiResponse<Prescription>> createPrescription(@Valid @RequestBody PrescriptionRequest request) {
        Prescription created = prescriptionService.createPrescription(request);
        return ResponseEntity.ok(ApiResponse.ok("Prescription issued successfully", created));
    }
}
