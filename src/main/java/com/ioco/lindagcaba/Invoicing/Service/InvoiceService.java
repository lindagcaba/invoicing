package com.ioco.lindagcaba.Invoicing.Service;

import com.ioco.lindagcaba.Invoicing.model.Invoice;
import com.ioco.lindagcaba.Invoicing.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;

    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public List<Invoice> getAllInvoices(){
        return invoiceRepository.findAll();
    }
    public void addInvoice(Invoice invoice){
        invoiceRepository.save(invoice);
    }

    public Invoice findById(Long invoiceId) {
        return invoiceRepository.findById(invoiceId).orElse(null);
    }
}
