package com.example.developerteam.controller;

import com.example.developerteam.entity.Invoice;
import com.example.developerteam.service.InvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping("/project/{projectId}")
    public ResponseEntity<Invoice> createInvoice(@PathVariable Long projectId) {
        Invoice inv = invoiceService.createInvoice(projectId);
        return ResponseEntity.ok(inv);
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Invoice>> getByProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(invoiceService.getInvoicesByProject(projectId));
    }

    @PutMapping("/{invoiceId}/paid")
    public ResponseEntity<?> markPaid(@PathVariable Long invoiceId) {
        invoiceService.markInvoicePaid(invoiceId);
        return ResponseEntity.ok("Marked as paid");
    }
}
