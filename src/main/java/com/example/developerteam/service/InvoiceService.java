package com.example.developerteam.service;

import com.example.developerteam.entity.*;
import com.example.developerteam.repository.InvoiceLineRepository;
import com.example.developerteam.repository.InvoiceRepository;
import com.example.developerteam.repository.WorkLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceLineRepository invoiceLineRepository;
    private final WorkLogRepository workLogRepository;

    public InvoiceService(InvoiceRepository invoiceRepository,
                          InvoiceLineRepository invoiceLineRepository,
                          WorkLogRepository workLogRepository) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceLineRepository = invoiceLineRepository;
        this.workLogRepository = workLogRepository;
    }


    @Transactional
    public Invoice createInvoice(Long projectId) {
        List<WorkLog> logs = workLogRepository.findByProjectProjectId(projectId);
        if (logs.isEmpty()) {
            throw new RuntimeException("No work logs for project");
        }

        // Обчислюємо totalAmount
        BigDecimal total = BigDecimal.ZERO;
        for (WorkLog w : logs) {
            BigDecimal lineAmount = w.getHours().multiply(w.getDeveloper().getHourlyRate());
            total = total.add(lineAmount);
        }

        Invoice invoice = new Invoice();
        invoice.setProject(logs.get(0).getProject());
        invoice.setIssuedDate(LocalDate.now());
        invoice.setTotalAmount(total);
        invoice.setIsPaid(false);

        Invoice savedInvoice = invoiceRepository.save(invoice);

        // створюємо InvoiceLine для кожного WorkLog
        for (WorkLog w : logs) {
            InvoiceLine line = new InvoiceLine();
            line.setInvoice(savedInvoice);
            line.setWorkLog(w);
            line.setHours(w.getHours());
            line.setHourlyRate(w.getDeveloper().getHourlyRate());
            line.setLineAmount(w.getHours().multiply(w.getDeveloper().getHourlyRate()));
            invoiceLineRepository.save(line);
        }

        return savedInvoice;
    }

    public List<Invoice> getInvoicesByProject(Long projectId) {
        return invoiceRepository.findByProjectProjectId(projectId);
    }

    public void markInvoicePaid(Long invoiceId) {
        Invoice inv = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
        inv.setIsPaid(true);
        invoiceRepository.save(inv);
    }
}
