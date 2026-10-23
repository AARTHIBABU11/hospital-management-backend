package com.hospital.service;

import com.hospital.dto.PrescriptionRequest;
import com.hospital.entity.*;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repository.AppointmentRepository;
import com.hospital.repository.DoctorRepository;
import com.hospital.repository.PatientRepository;
import com.hospital.repository.PrescriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final NotificationService notificationService;

    public PrescriptionService(PrescriptionRepository prescriptionRepository,
                               PatientRepository patientRepository,
                               DoctorRepository doctorRepository,
                               AppointmentRepository appointmentRepository,
                               NotificationService notificationService) {
        this.prescriptionRepository = prescriptionRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.notificationService = notificationService;
    }

    public List<Prescription> getAllPrescriptions() {
        return prescriptionRepository.findAll();
    }

    public Prescription getPrescriptionById(Long id) {
        return prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription not found with ID: " + id));
    }

    public List<Prescription> getPrescriptionsByPatient(Long patientId) {
        return prescriptionRepository.findByPatientIdOrderByPrescriptionDateDesc(patientId);
    }

    public List<Prescription> getPrescriptionsByDoctor(Long doctorId) {
        return prescriptionRepository.findByDoctorIdOrderByPrescriptionDateDesc(doctorId);
    }

    @Transactional
    public Prescription createPrescription(PrescriptionRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        Appointment appointment = null;
        if (request.getAppointmentId() != null) {
            appointment = appointmentRepository.findById(request.getAppointmentId()).orElse(null);
        }

        Prescription prescription = new Prescription();
        prescription.setPatient(patient);
        prescription.setDoctor(doctor);
        prescription.setAppointment(appointment);
        prescription.setPrescriptionDate(request.getPrescriptionDate() != null ? request.getPrescriptionDate() : LocalDate.now());
        prescription.setDiagnosis(request.getDiagnosis());
        prescription.setInstructions(request.getInstructions());

        if (request.getItems() != null) {
            for (PrescriptionRequest.PrescriptionItemDto itemDto : request.getItems()) {
                PrescriptionItem item = new PrescriptionItem(
                        itemDto.getMedicineName(),
                        itemDto.getDosage(),
                        itemDto.getFrequency(),
                        itemDto.getDuration(),
                        itemDto.getInstructions()
                );
                prescription.addItem(item);
            }
        }

        Prescription saved = prescriptionRepository.save(prescription);

        if (patient.getEmail() != null) {
            notificationService.sendNotification(
                    patient.getEmail(),
                    "New Prescription Issued",
                    "Dr. " + doctor.getName() + " has added a new prescription to your profile.",
                    "PRESCRIPTION"
            );
        }

        return saved;
    }
}
