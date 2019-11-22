package com.ioco.lindagcaba.Invoicing.model;

import com.sun.istack.NotNull;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;


@Entity
public class Invoice implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @NotBlank
    @Column
    private String client;
    @NotNull
    @Column
    private Date invoiceDate;
    @NotNull
    @Column
    private Long vatRate;
    @OneToMany(mappedBy = "invoice",cascade = CascadeType.ALL)
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
    public Invoice(@NotBlank String client, Date invoiceDate, Long vatRate) {
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

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Long getVatRate() {
        return vatRate;
    }

    public void setVatRate(Long vatRate) {
        this.vatRate = vatRate;
    }
    public BigDecimal getSubTotal(){
        return lineItems.stream().map(lineItem -> lineItem.getLineItemTotal()).reduce(BigDecimal.ZERO,BigDecimal::add).setScale(2,BigDecimal.ROUND_HALF_UP);
    }
    public BigDecimal getVat(){
            return BigDecimal.valueOf(vatRate).divide(new BigDecimal(100)).multiply(getSubTotal());
    }

    public BigDecimal getTotal(){
        return getSubTotal().add(getVat());
    }
    public void addLineItem(LineItem lineItem){
        lineItems.add(lineItem);
        lineItem.setInvoice(this);
    }
}
