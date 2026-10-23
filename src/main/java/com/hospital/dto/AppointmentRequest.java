package com.hospital.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class AppointmentRequest {
    private Long patientId; // optional if booked by logged-in patient

    @NotNull(message = "Doctor is required")
    private Long doctorId;

    private Long departmentId;

    @NotNull(message = "Date is required")
    private LocalDate appointmentDate;

    @NotNull(message = "Time is required")
    private String appointmentTime;

    private String reason;
    private String notes;

    public AppointmentRequest() {}

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }

    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }

    public LocalDate getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; }

    public String getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(String appointmentTime) { this.appointmentTime = appointmentTime; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
