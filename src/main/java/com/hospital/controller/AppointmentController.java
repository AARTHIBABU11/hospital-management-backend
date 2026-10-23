package com.hospital.controller;

import com.hospital.dto.ApiResponse;
import com.hospital.dto.AppointmentRequest;
import com.hospital.entity.Appointment;
import com.hospital.entity.AppointmentStatus;
import com.hospital.entity.Patient;
import com.hospital.repository.PatientRepository;
import com.hospital.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final PatientRepository patientRepository;

    public AppointmentController(AppointmentService appointmentService,
                                 PatientRepository patientRepository) {
        this.appointmentService = appointmentService;
        this.patientRepository = patientRepository;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RECEPTIONIST')")
    public ResponseEntity<ApiResponse<List<Appointment>>> getAllAppointments() {
        return ResponseEntity.ok(ApiResponse.ok("Appointments fetched", appointmentService.getAllAppointments()));
    }

    @GetMapping("/today")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_RECEPTIONIST')")
    public ResponseEntity<ApiResponse<List<Appointment>>> getTodayAppointments() {
        return ResponseEntity.ok(ApiResponse.ok("Today's appointments fetched", appointmentService.getTodayAppointments()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Appointment>> getAppointmentById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Appointment fetched", appointmentService.getAppointmentById(id)));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<ApiResponse<List<Appointment>>> getAppointmentsByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(ApiResponse.ok("Patient appointments fetched", appointmentService.getAppointmentsByPatient(patientId)));
    }

    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_RECEPTIONIST')")
    public ResponseEntity<ApiResponse<List<Appointment>>> getAppointmentsByDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(ApiResponse.ok("Doctor appointments fetched", appointmentService.getAppointmentsByDoctor(doctorId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Appointment>> bookAppointment(@Valid @RequestBody AppointmentRequest request,
                                                                    Authentication authentication) {
        // If patientId not sent, try to derive from authenticated user
        if (request.getPatientId() == null && authentication != null) {
            patientRepository.findByEmail(authentication.getName()).ifPresent(p -> request.setPatientId(p.getId()));
        }

        Appointment booked = appointmentService.bookAppointment(request);
        return ResponseEntity.ok(ApiResponse.ok("Appointment booked successfully", booked));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_RECEPTIONIST')")
    public ResponseEntity<ApiResponse<Appointment>> updateStatus(@PathVariable Long id,
                                                                 @RequestBody Map<String, String> body) {
        String statusStr = body.get("status");
        String notes = body.get("notes");
        AppointmentStatus status = AppointmentStatus.valueOf(statusStr.toUpperCase());
        Appointment updated = appointmentService.updateStatus(id, status, notes);
        return ResponseEntity.ok(ApiResponse.ok("Status updated", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> cancelAppointment(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
        return ResponseEntity.ok(ApiResponse.ok("Appointment cancelled successfully"));
    }
}
