package com.hospital.controller;

import com.hospital.dto.ApiResponse;
import com.hospital.entity.Doctor;
import com.hospital.service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Doctor>>> getAllDoctors() {
        return ResponseEntity.ok(ApiResponse.ok("Doctors fetched", doctorService.getAllDoctors()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Doctor>> getDoctorById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Doctor fetched", doctorService.getDoctorById(id)));
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<ApiResponse<List<Doctor>>> getDoctorsByDepartment(@PathVariable Long departmentId) {
        return ResponseEntity.ok(ApiResponse.ok("Doctors fetched", doctorService.getDoctorsByDepartment(departmentId)));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Doctor>> createDoctor(@RequestBody Map<String, Object> payload) {
        Doctor doctor = new Doctor();
        doctor.setName((String) payload.get("name"));
        doctor.setEmail((String) payload.get("email"));
        doctor.setPhone((String) payload.get("phone"));
        doctor.setGender((String) payload.get("gender"));
        doctor.setSpecialization((String) payload.get("specialization"));
        doctor.setQualification((String) payload.get("qualification"));
        if (payload.get("experienceYears") != null) {
            doctor.setExperienceYears(Integer.parseInt(payload.get("experienceYears").toString()));
        }
        if (payload.get("consultationFee") != null) {
            doctor.setConsultationFee(Double.parseDouble(payload.get("consultationFee").toString()));
        }
        doctor.setAvailableDays((String) payload.get("availableDays"));
        doctor.setAvailableTime((String) payload.get("availableTime"));
        doctor.setStatus(payload.get("status") != null ? (String) payload.get("status") : "Active");
        doctor.setBio((String) payload.get("bio"));
        doctor.setAvatarUrl((String) payload.get("avatarUrl"));

        Long deptId = payload.get("departmentId") != null ? Long.parseLong(payload.get("departmentId").toString()) : null;
        String password = (String) payload.get("password");

        Doctor created = doctorService.createDoctor(doctor, deptId, password);
        return ResponseEntity.ok(ApiResponse.ok("Doctor created successfully", created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DOCTOR')")
    public ResponseEntity<ApiResponse<Doctor>> updateDoctor(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        Doctor doctor = new Doctor();
        doctor.setName((String) payload.get("name"));
        doctor.setPhone((String) payload.get("phone"));
        doctor.setGender((String) payload.get("gender"));
        doctor.setSpecialization((String) payload.get("specialization"));
        doctor.setQualification((String) payload.get("qualification"));
        if (payload.get("experienceYears") != null) {
            doctor.setExperienceYears(Integer.parseInt(payload.get("experienceYears").toString()));
        }
        if (payload.get("consultationFee") != null) {
            doctor.setConsultationFee(Double.parseDouble(payload.get("consultationFee").toString()));
        }
        doctor.setAvailableDays((String) payload.get("availableDays"));
        doctor.setAvailableTime((String) payload.get("availableTime"));
        doctor.setStatus((String) payload.get("status"));
        doctor.setBio((String) payload.get("bio"));
        doctor.setAvatarUrl((String) payload.get("avatarUrl"));

        Long deptId = payload.get("departmentId") != null ? Long.parseLong(payload.get("departmentId").toString()) : null;

        Doctor updated = doctorService.updateDoctor(id, doctor, deptId);
        return ResponseEntity.ok(ApiResponse.ok("Doctor updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.ok(ApiResponse.ok("Doctor deleted successfully"));
    }
}
