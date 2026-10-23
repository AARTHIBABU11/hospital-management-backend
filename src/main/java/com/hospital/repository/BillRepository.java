package com.hospital.repository;

import com.hospital.entity.Bill;
import com.hospital.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findByPatientIdOrderByBillDateDesc(Long patientId);
    List<Bill> findByPaymentStatus(PaymentStatus status);
    Optional<Bill> findByInvoiceNumber(String invoiceNumber);

    @Query("SELECT COALESCE(SUM(b.totalAmount), 0) FROM Bill b WHERE b.paymentStatus = 'PAID'")
    Double calculateTotalRevenue();
}
