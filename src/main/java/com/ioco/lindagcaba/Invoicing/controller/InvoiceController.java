package com.ioco.lindagcaba.Invoicing.controller;

import com.ioco.lindagcaba.Invoicing.Service.InvoiceService;
import com.ioco.lindagcaba.Invoicing.model.Invoice;
import com.ioco.lindagcaba.Invoicing.model.LineItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {
    private final InvoiceService invoiceService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public List<Invoice> viewAllInvoices(){
        return invoiceService.getAllInvoices();
    }
    @PostMapping
    public void addInvoice(@Valid @RequestBody Invoice invoiceData){
        Invoice invoice = new Invoice(invoiceData.getClient(),invoiceData.getInvoiceDate(),invoiceData.getVatRate());
        List<LineItem> lineItems = invoiceData.getLineItems();
        lineItems.forEach(invoice::addLineItem);
        invoiceService.addInvoice(invoice);
    }
    @GetMapping("/{invoiceId}")
    public Invoice viewInvoice(@PathVariable Long invoiceId){
        return invoiceService.findById(invoiceId);
    }
}
