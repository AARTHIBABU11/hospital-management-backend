package com.hospital.service;

import com.hospital.config.JwtUtils;
import com.hospital.dto.AuthResponse;
import com.hospital.dto.LoginRequest;
import com.hospital.dto.RegisterRequest;
import com.hospital.entity.Patient;
import com.hospital.entity.Role;
import com.hospital.entity.User;
import com.hospital.exception.BadRequestException;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repository.DoctorRepository;
import com.hospital.repository.PatientRepository;
import com.hospital.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthService(UserRepository userRepository,
                       PatientRepository patientRepository,
                       DoctorRepository doctorRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new BadRequestException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid email or password");
        }

        String token = jwtUtils.generateToken(user.getEmail(), user.getRole().name());

        Long roleId = null;
        if (user.getRole() == Role.ROLE_DOCTOR) {
            roleId = doctorRepository.findByUserId(user.getId())
                    .map(d -> d.getId()).orElse(null);
        } else if (user.getRole() == Role.ROLE_PATIENT) {
            roleId = patientRepository.findByUserId(user.getId())
                    .map(p -> p.getId()).orElse(null);
        }

        return new AuthResponse(token, user.getId(), user.getEmail(), user.getName(), user.getRole(), roleId);
    }

    @Transactional
    public AuthResponse registerPatient(RegisterRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("Email is already registered");
        }

        User user = new User(
                email,
                passwordEncoder.encode(request.getPassword()),
                request.getName(),
                request.getPhone(),
                Role.ROLE_PATIENT
        );
        user = userRepository.save(user);

        Patient patient = new Patient();
        patient.setUser(user);
        patient.setName(request.getName());
        patient.setEmail(email);
        patient.setPhone(request.getPhone());
        patient.setDob(request.getDob());
        patient.setGender(request.getGender());
        patient.setBloodGroup(request.getBloodGroup());
        patient.setAddress(request.getAddress());
        patient.setEmergencyContact(request.getEmergencyContact());
        patient.setAllergies(request.getAllergies());
        patient.setMedicalHistory(request.getMedicalHistory());
        patient = patientRepository.save(patient);

        String token = jwtUtils.generateToken(user.getEmail(), user.getRole().name());
        return new AuthResponse(token, user.getId(), user.getEmail(), user.getName(), user.getRole(), patient.getId());
    }

    public AuthResponse getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Long roleId = null;
        if (user.getRole() == Role.ROLE_DOCTOR) {
            roleId = doctorRepository.findByUserId(user.getId())
                    .map(d -> d.getId()).orElse(null);
        } else if (user.getRole() == Role.ROLE_PATIENT) {
            roleId = patientRepository.findByUserId(user.getId())
                    .map(p -> p.getId()).orElse(null);
        }

        return new AuthResponse(null, user.getId(), user.getEmail(), user.getName(), user.getRole(), roleId);
    }
}
