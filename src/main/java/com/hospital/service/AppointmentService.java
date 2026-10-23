package com.hospital.service;

import com.hospital.dto.AppointmentRequest;
import com.hospital.entity.*;
import com.hospital.exception.BadRequestException;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repository.AppointmentRepository;
import com.hospital.repository.DepartmentRepository;
import com.hospital.repository.DoctorRepository;
import com.hospital.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
    private final NotificationService notificationService;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              PatientRepository patientRepository,
                              DoctorRepository doctorRepository,
                              DepartmentRepository departmentRepository,
                              NotificationService notificationService) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.departmentRepository = departmentRepository;
        this.notificationService = notificationService;
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + id));
    }

    public List<Appointment> getAppointmentsByPatient(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    public List<Appointment> getAppointmentsByDoctor(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }

    public List<Appointment> getTodayAppointments() {
        return appointmentRepository.findByAppointmentDate(LocalDate.now());
    }

    @Transactional
    public Appointment bookAppointment(AppointmentRequest request) {
        if (request.getAppointmentDate().isBefore(LocalDate.now())) {
            throw new BadRequestException("Appointment date cannot be in the past");
        }

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        // Check double booking
        boolean slotTaken = appointmentRepository.existsByDoctorIdAndAppointmentDateAndAppointmentTimeAndStatusNot(
                doctor.getId(),
                request.getAppointmentDate(),
                request.getAppointmentTime(),
                AppointmentStatus.CANCELLED
        );
        if (slotTaken) {
            throw new BadRequestException("The selected time slot is already booked for Dr. " + doctor.getName());
        }

        Department department = doctor.getDepartment();
        if (request.getDepartmentId() != null) {
            department = departmentRepository.findById(request.getDepartmentId()).orElse(department);
        }

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setDepartment(department);
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setReason(request.getReason());
        appointment.setNotes(request.getNotes());
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        appointment = appointmentRepository.save(appointment);

        // Send notifications
        if (patient.getEmail() != null) {
            notificationService.sendNotification(
                    patient.getEmail(),
                    "Appointment Scheduled",
                    "Your appointment with Dr. " + doctor.getName() + " is confirmed for " +
                            appointment.getAppointmentDate() + " at " + appointment.getAppointmentTime(),
                    "APPOINTMENT"
            );
        }
        if (doctor.getEmail() != null) {
            notificationService.sendNotification(
                    doctor.getEmail(),
                    "New Appointment Booked",
                    "Patient " + patient.getName() + " booked an appointment for " +
                            appointment.getAppointmentDate() + " at " + appointment.getAppointmentTime(),
                    "APPOINTMENT"
            );
        }

        return appointment;
    }

    public Appointment updateStatus(Long id, AppointmentStatus status, String notes) {
        Appointment appointment = getAppointmentById(id);
        appointment.setStatus(status);
        if (notes != null && !notes.isBlank()) {
            appointment.setNotes(notes);
        }
        appointment = appointmentRepository.save(appointment);

        if (appointment.getPatient() != null && appointment.getPatient().getEmail() != null) {
            notificationService.sendNotification(
                    appointment.getPatient().getEmail(),
                    "Appointment Status Updated",
                    "Your appointment on " + appointment.getAppointmentDate() + " is now " + status.name(),
                    "APPOINTMENT"
            );
        }

        return appointment;
    }

    public void cancelAppointment(Long id) {
        updateStatus(id, AppointmentStatus.CANCELLED, "Cancelled by user");
    }
}
