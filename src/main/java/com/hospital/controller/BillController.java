package com.hospital.controller;

import com.hospital.dto.ApiResponse;
import com.hospital.dto.BillRequest;
import com.hospital.entity.Bill;
import com.hospital.entity.PaymentMethod;
import com.hospital.entity.PaymentStatus;
import com.hospital.service.BillService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RECEPTIONIST')")
    public ResponseEntity<ApiResponse<List<Bill>>> getAllBills() {
        return ResponseEntity.ok(ApiResponse.ok("Bills fetched", billService.getAllBills()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Bill>> getBillById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Bill fetched", billService.getBillById(id)));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<ApiResponse<List<Bill>>> getBillsByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(ApiResponse.ok("Patient bills fetched", billService.getBillsByPatient(patientId)));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RECEPTIONIST')")
    public ResponseEntity<ApiResponse<Bill>> createBill(@Valid @RequestBody BillRequest request) {
        Bill created = billService.createBill(request);
        return ResponseEntity.ok(ApiResponse.ok("Bill generated successfully", created));
    }

    @PutMapping("/{id}/pay")
    public ResponseEntity<ApiResponse<Bill>> payBill(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String statusStr = payload.get("status") != null ? payload.get("status") : "PAID";
        String methodStr = payload.get("method") != null ? payload.get("method") : "ONLINE";

        PaymentStatus status = PaymentStatus.valueOf(statusStr.toUpperCase());
        PaymentMethod method = PaymentMethod.valueOf(methodStr.toUpperCase());

        Bill updated = billService.updateBillPayment(id, status, method);
        return ResponseEntity.ok(ApiResponse.ok("Payment processed successfully", updated));
    }
}
