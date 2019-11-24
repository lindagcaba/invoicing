package com.ioco.lindagcaba.Invoicing.model;


import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Invoice implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @NotBlank(message = "client name is required and should not be blank")
    @Column
    private String client;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column
    private LocalDate invoiceDate;
    @NotNull
    @PositiveOrZero(message = "vatRate should be a zero value or above")
    @Column
    private Long vatRate;
    @OneToMany(mappedBy = "invoice",cascade = CascadeType.PERSIST,orphanRemoval = true)
    @NotEmpty
    @Valid
    private List<LineItem> lineItems = new ArrayList<>();

    public List<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }

    public Invoice(){
        super();
    }
    public Invoice(@NotBlank String client, LocalDate invoiceDate, Long vatRate) {
        this.client = client;
        this.invoiceDate = invoiceDate;
        this.vatRate = vatRate;
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Long getVatRate() {
        return vatRate;
    }

    public void setVatRate(Long vatRate) {
        this.vatRate = vatRate;
    }
    public BigDecimal getSubTotal(){
        return lineItems.stream().map(lineItem -> lineItem.getLineItemTotal()).reduce(BigDecimal::add).get().setScale(2,RoundingMode.HALF_UP);
    }
    public BigDecimal getVat(){
            return BigDecimal.valueOf(vatRate).divide(new BigDecimal(100)).multiply(getSubTotal()).setScale(2,RoundingMode.HALF_UP);
    }

    public BigDecimal getTotal(){
        return getSubTotal().add(getVat()).setScale(2,RoundingMode.HALF_UP);
    }
    public void addLineItem(LineItem lineItem){
        lineItems.add(lineItem);
        lineItem.setInvoice(this);
    }
}
