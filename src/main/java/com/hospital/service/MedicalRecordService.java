package com.hospital.service;

import com.hospital.dto.MedicalRecordRequest;
import com.hospital.entity.Appointment;
import com.hospital.entity.Doctor;
import com.hospital.entity.MedicalRecord;
import com.hospital.entity.Patient;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repository.AppointmentRepository;
import com.hospital.repository.DoctorRepository;
import com.hospital.repository.MedicalRecordRepository;
import com.hospital.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository,
                                PatientRepository patientRepository,
                                DoctorRepository doctorRepository,
                                AppointmentRepository appointmentRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public List<MedicalRecord> getAllRecords() {
        return medicalRecordRepository.findAll();
    }

    public MedicalRecord getRecordById(Long id) {
        return medicalRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medical Record not found with ID: " + id));
    }

    public List<MedicalRecord> getRecordsByPatient(Long patientId) {
        return medicalRecordRepository.findByPatientIdOrderByVisitDateDesc(patientId);
    }

    public List<MedicalRecord> getRecordsByDoctor(Long doctorId) {
        return medicalRecordRepository.findByDoctorIdOrderByVisitDateDesc(doctorId);
    }

    public MedicalRecord createRecord(MedicalRecordRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        Doctor doctor = null;
        if (request.getDoctorId() != null) {
            doctor = doctorRepository.findById(request.getDoctorId()).orElse(null);
        }

        Appointment appointment = null;
        if (request.getAppointmentId() != null) {
            appointment = appointmentRepository.findById(request.getAppointmentId()).orElse(null);
        }

        MedicalRecord record = new MedicalRecord();
        record.setPatient(patient);
        record.setDoctor(doctor);
        record.setAppointment(appointment);
        record.setVisitDate(request.getVisitDate() != null ? request.getVisitDate() : LocalDate.now());
        record.setSymptoms(request.getSymptoms());
        record.setDiagnosis(request.getDiagnosis());
        record.setClinicalNotes(request.getClinicalNotes());
        record.setTreatment(request.getTreatment());
        record.setFollowUpDate(request.getFollowUpDate());

        return medicalRecordRepository.save(record);
    }

    public MedicalRecord updateRecord(Long id, MedicalRecordRequest request) {
        MedicalRecord record = getRecordById(id);
        record.setSymptoms(request.getSymptoms());
        record.setDiagnosis(request.getDiagnosis());
        record.setClinicalNotes(request.getClinicalNotes());
        record.setTreatment(request.getTreatment());
        record.setFollowUpDate(request.getFollowUpDate());
        return medicalRecordRepository.save(record);
    }
}
