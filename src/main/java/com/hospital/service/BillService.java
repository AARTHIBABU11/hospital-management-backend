package com.hospital.service;

import com.hospital.dto.BillRequest;
import com.hospital.entity.*;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repository.AppointmentRepository;
import com.hospital.repository.BillRepository;
import com.hospital.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class BillService {

    private final BillRepository billRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final NotificationService notificationService;

    public BillService(BillRepository billRepository,
                       PatientRepository patientRepository,
                       AppointmentRepository appointmentRepository,
                       NotificationService notificationService) {
        this.billRepository = billRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.notificationService = notificationService;
    }

    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    public Bill getBillById(Long id) {
        return billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with ID: " + id));
    }

    public List<Bill> getBillsByPatient(Long patientId) {
        return billRepository.findByPatientIdOrderByBillDateDesc(patientId);
    }

    public Bill createBill(BillRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        Appointment appointment = null;
        if (request.getAppointmentId() != null) {
            appointment = appointmentRepository.findById(request.getAppointmentId()).orElse(null);
        }

        Bill bill = new Bill();
        bill.setInvoiceNumber("INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        bill.setPatient(patient);
        bill.setAppointment(appointment);
        bill.setConsultationFee(request.getConsultationFee() != null ? request.getConsultationFee() : 0.0);
        bill.setMedicineCharges(request.getMedicineCharges() != null ? request.getMedicineCharges() : 0.0);
        bill.setLabCharges(request.getLabCharges() != null ? request.getLabCharges() : 0.0);
        bill.setOtherCharges(request.getOtherCharges() != null ? request.getOtherCharges() : 0.0);
        bill.calculateTotal();

        bill.setPaymentStatus(request.getPaymentStatus() != null ? request.getPaymentStatus() : PaymentStatus.PENDING);
        bill.setPaymentMethod(request.getPaymentMethod());
        bill.setBillDate(request.getBillDate() != null ? request.getBillDate() : LocalDate.now());
        bill.setNotes(request.getNotes());

        Bill saved = billRepository.save(bill);

        if (patient.getEmail() != null) {
            notificationService.sendNotification(
                    patient.getEmail(),
                    "New Invoice Generated",
                    "Invoice #" + saved.getInvoiceNumber() + " for $" + saved.getTotalAmount() + " has been generated.",
                    "BILL"
            );
        }

        return saved;
    }

    public Bill updateBillPayment(Long id, PaymentStatus status, PaymentMethod method) {
        Bill bill = getBillById(id);
        bill.setPaymentStatus(status);
        if (method != null) {
            bill.setPaymentMethod(method);
        }
        Bill saved = billRepository.save(bill);

        if (status == PaymentStatus.PAID && bill.getPatient() != null && bill.getPatient().getEmail() != null) {
            notificationService.sendNotification(
                    bill.getPatient().getEmail(),
                    "Payment Received",
                    "Thank you! Payment of $" + bill.getTotalAmount() + " for invoice #" + bill.getInvoiceNumber() + " is confirmed.",
                    "BILL"
            );
        }

        return saved;
    }
}
