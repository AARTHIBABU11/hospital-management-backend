package com.hospital.controller;

import com.hospital.dto.ApiResponse;
import com.hospital.entity.Patient;
import com.hospital.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_RECEPTIONIST')")
    public ResponseEntity<ApiResponse<List<Patient>>> getAllPatients(@RequestParam(required = false) String search) {
        List<Patient> list = (search != null && !search.isBlank())
                ? patientService.searchPatients(search)
                : patientService.getAllPatients();
        return ResponseEntity.ok(ApiResponse.ok("Patients fetched", list));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_RECEPTIONIST', 'ROLE_PATIENT')")
    public ResponseEntity<ApiResponse<Patient>> getPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Patient fetched", patientService.getPatientById(id)));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RECEPTIONIST')")
    public ResponseEntity<ApiResponse<Patient>> createPatient(@RequestBody Map<String, Object> payload) {
        Patient patient = new Patient();
        patient.setName((String) payload.get("name"));
        patient.setEmail((String) payload.get("email"));
        patient.setPhone((String) payload.get("phone"));
        patient.setGender((String) payload.get("gender"));
        patient.setBloodGroup((String) payload.get("bloodGroup"));
        patient.setAddress((String) payload.get("address"));
        patient.setEmergencyContact((String) payload.get("emergencyContact"));
        patient.setAllergies((String) payload.get("allergies"));
        patient.setMedicalHistory((String) payload.get("medicalHistory"));
        patient.setStatus("Active");

        if (payload.get("age") != null) {
            patient.setAge(Integer.parseInt(payload.get("age").toString()));
        }

        String password = (String) payload.get("password");
        Patient created = patientService.createPatient(patient, password);
        return ResponseEntity.ok(ApiResponse.ok("Patient registered successfully", created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RECEPTIONIST', 'ROLE_PATIENT')")
    public ResponseEntity<ApiResponse<Patient>> updatePatient(@PathVariable Long id, @RequestBody Patient patient) {
        Patient updated = patientService.updatePatient(id, patient);
        return ResponseEntity.ok(ApiResponse.ok("Patient updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.ok(ApiResponse.ok("Patient deleted successfully"));
    }
}
