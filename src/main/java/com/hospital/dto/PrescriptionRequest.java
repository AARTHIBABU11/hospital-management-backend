package com.hospital.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public class PrescriptionRequest {
    @NotNull(message = "Patient ID is required")
    private Long patientId;

    private Long doctorId;
    private Long appointmentId;
    private LocalDate prescriptionDate;
    private String diagnosis;
    private String instructions;

    @NotEmpty(message = "At least one medicine item is required")
    private List<PrescriptionItemDto> items;

    public PrescriptionRequest() {}

    public static class PrescriptionItemDto {
        private String medicineName;
        private String dosage;
        private String frequency;
        private String duration;
        private String instructions;

        public PrescriptionItemDto() {}

        public PrescriptionItemDto(String medicineName, String dosage, String frequency, String duration, String instructions) {
            this.medicineName = medicineName;
            this.dosage = dosage;
            this.frequency = frequency;
            this.duration = duration;
            this.instructions = instructions;
        }

        public String getMedicineName() { return medicineName; }
        public void setMedicineName(String medicineName) { this.medicineName = medicineName; }

        public String getDosage() { return dosage; }
        public void setDosage(String dosage) { this.dosage = dosage; }

        public String getFrequency() { return frequency; }
        public void setFrequency(String frequency) { this.frequency = frequency; }

        public String getDuration() { return duration; }
        public void setDuration(String duration) { this.duration = duration; }

        public String getInstructions() { return instructions; }
        public void setInstructions(String instructions) { this.instructions = instructions; }
    }

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }

    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }

    public LocalDate getPrescriptionDate() { return prescriptionDate; }
    public void setPrescriptionDate(LocalDate prescriptionDate) { this.prescriptionDate = prescriptionDate; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    public List<PrescriptionItemDto> getItems() { return items; }
    public void setItems(List<PrescriptionItemDto> items) { this.items = items; }
}
