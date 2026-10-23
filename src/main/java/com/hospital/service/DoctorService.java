package com.hospital.service;

import com.hospital.entity.Department;
import com.hospital.entity.Doctor;
import com.hospital.entity.Role;
import com.hospital.entity.User;
import com.hospital.exception.BadRequestException;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repository.DepartmentRepository;
import com.hospital.repository.DoctorRepository;
import com.hospital.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DoctorService(DoctorRepository doctorRepository,
                         DepartmentRepository departmentRepository,
                         UserRepository userRepository,
                         PasswordEncoder passwordEncoder) {
        this.doctorRepository = doctorRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + id));
    }

    public List<Doctor> getDoctorsByDepartment(Long departmentId) {
        return doctorRepository.findByDepartmentId(departmentId);
    }

    @Transactional
    public Doctor createDoctor(Doctor doctor, Long departmentId, String password) {
        String email = doctor.getEmail().trim().toLowerCase();
        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("User already exists with email: " + email);
        }

        User user = new User(
                email,
                passwordEncoder.encode(password != null && !password.isBlank() ? password : "doctor123"),
                doctor.getName(),
                doctor.getPhone(),
                Role.ROLE_DOCTOR
        );
        user = userRepository.save(user);

        if (departmentId != null) {
            Department dept = departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
            doctor.setDepartment(dept);
        }

        doctor.setUser(user);
        return doctorRepository.save(doctor);
    }

    @Transactional
    public Doctor updateDoctor(Long id, Doctor details, Long departmentId) {
        Doctor doctor = getDoctorById(id);
        doctor.setName(details.getName());
        doctor.setPhone(details.getPhone());
        doctor.setGender(details.getGender());
        doctor.setSpecialization(details.getSpecialization());
        doctor.setQualification(details.getQualification());
        doctor.setExperienceYears(details.getExperienceYears());
        doctor.setConsultationFee(details.getConsultationFee());
        doctor.setAvailableDays(details.getAvailableDays());
        doctor.setAvailableTime(details.getAvailableTime());
        doctor.setStatus(details.getStatus());
        doctor.setBio(details.getBio());
        doctor.setAvatarUrl(details.getAvatarUrl());

        if (departmentId != null) {
            Department dept = departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
            doctor.setDepartment(dept);
        }

        if (doctor.getUser() != null) {
            doctor.getUser().setName(details.getName());
            doctor.getUser().setPhone(details.getPhone());
            userRepository.save(doctor.getUser());
        }

        return doctorRepository.save(doctor);
    }

    public void deleteDoctor(Long id) {
        Doctor doctor = getDoctorById(id);
        doctorRepository.delete(doctor);
    }
}
