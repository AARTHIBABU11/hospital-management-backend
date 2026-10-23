package com.hospital.service;

import com.hospital.entity.Patient;
import com.hospital.entity.Role;
import com.hospital.entity.User;
import com.hospital.exception.BadRequestException;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repository.PatientRepository;
import com.hospital.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public PatientService(PatientRepository patientRepository,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + id));
    }

    public Patient getPatientByEmail(String email) {
        return patientRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with email: " + email));
    }

    public List<Patient> searchPatients(String query) {
        return patientRepository.findByNameContainingIgnoreCase(query);
    }

    @Transactional
    public Patient createPatient(Patient patient, String password) {
        String email = patient.getEmail() != null ? patient.getEmail().trim().toLowerCase() : null;

        User user = null;
        if (email != null && !email.isBlank()) {
            if (userRepository.existsByEmail(email)) {
                throw new BadRequestException("User already registered with this email");
            }
            user = new User(
                    email,
                    passwordEncoder.encode(password != null && !password.isBlank() ? password : "patient123"),
                    patient.getName(),
                    patient.getPhone(),
                    Role.ROLE_PATIENT
            );
            user = userRepository.save(user);
        }

        patient.setUser(user);
        return patientRepository.save(patient);
    }

    @Transactional
    public Patient updatePatient(Long id, Patient details) {
        Patient patient = getPatientById(id);
        patient.setName(details.getName());
        patient.setDob(details.getDob());
        patient.setAge(details.getAge());
        patient.setGender(details.getGender());
        patient.setBloodGroup(details.getBloodGroup());
        patient.setPhone(details.getPhone());
        patient.setAddress(details.getAddress());
        patient.setEmergencyContact(details.getEmergencyContact());
        patient.setAllergies(details.getAllergies());
        patient.setMedicalHistory(details.getMedicalHistory());
        patient.setStatus(details.getStatus());

        if (patient.getUser() != null) {
            patient.getUser().setName(details.getName());
            patient.getUser().setPhone(details.getPhone());
            userRepository.save(patient.getUser());
        }

        return patientRepository.save(patient);
    }

    public void deletePatient(Long id) {
        Patient patient = getPatientById(id);
        patientRepository.delete(patient);
    }
}
