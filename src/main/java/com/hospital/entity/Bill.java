package com.hospital.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bills")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String invoiceNumber;

    @ManyToOne(optional = false)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    private Double consultationFee = 0.0;
    private Double medicineCharges = 0.0;
    private Double labCharges = 0.0;
    private Double otherCharges = 0.0;
    private Double totalAmount = 0.0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private LocalDate billDate = LocalDate.now();

    @Column(length = 1000)
    private String notes;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Bill() {}

    public void calculateTotal() {
        double cf = consultationFee != null ? consultationFee : 0.0;
        double mc = medicineCharges != null ? medicineCharges : 0.0;
        double lc = labCharges != null ? labCharges : 0.0;
        double oc = otherCharges != null ? otherCharges : 0.0;
        this.totalAmount = cf + mc + lc + oc;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public Appointment getAppointment() { return appointment; }
    public void setAppointment(Appointment appointment) { this.appointment = appointment; }

    public Double getConsultationFee() { return consultationFee; }
    public void setConsultationFee(Double consultationFee) { this.consultationFee = consultationFee; }

    public Double getMedicineCharges() { return medicineCharges; }
    public void setMedicineCharges(Double medicineCharges) { this.medicineCharges = medicineCharges; }

    public Double getLabCharges() { return labCharges; }
    public void setLabCharges(Double labCharges) { this.labCharges = labCharges; }

    public Double getOtherCharges() { return otherCharges; }
    public void setOtherCharges(Double otherCharges) { this.otherCharges = otherCharges; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }

    public LocalDate getBillDate() { return billDate; }
    public void setBillDate(LocalDate billDate) { this.billDate = billDate; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
