package com.hospital.dto;

import com.hospital.entity.PaymentMethod;
import com.hospital.entity.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class BillRequest {
    @NotNull(message = "Patient ID is required")
    private Long patientId;

    private Long appointmentId;
    private Double consultationFee = 0.0;
    private Double medicineCharges = 0.0;
    private Double labCharges = 0.0;
    private Double otherCharges = 0.0;
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;
    private PaymentMethod paymentMethod;
    private LocalDate billDate;
    private String notes;

    public BillRequest() {}

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }

    public Double getConsultationFee() { return consultationFee; }
    public void setConsultationFee(Double consultationFee) { this.consultationFee = consultationFee; }

    public Double getMedicineCharges() { return medicineCharges; }
    public void setMedicineCharges(Double medicineCharges) { this.medicineCharges = medicineCharges; }

    public Double getLabCharges() { return labCharges; }
    public void setLabCharges(Double labCharges) { this.labCharges = labCharges; }

    public Double getOtherCharges() { return otherCharges; }
    public void setOtherCharges(Double otherCharges) { this.otherCharges = otherCharges; }

    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }

    public LocalDate getBillDate() { return billDate; }
    public void setBillDate(LocalDate billDate) { this.billDate = billDate; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
