package com.ioco.lindagcaba.Invoicing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
public class LineItem implements Serializable{
    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    @Positive
    @Column
    private Long quantity;
    @NotBlank
    @Column
    private String description;
    @NotNull
    @PositiveOrZero
    @Column
    private BigDecimal unitPrice;
    @ManyToOne
    @JoinColumn(name = "invoice_id")
    @JsonIgnore
    private Invoice invoice;

    public void setId(Long id) {
        this.id = id;

    }

    public LineItem(String description, BigDecimal unitPrice, Long quantity) {
        this.quantity = quantity;
        this.description = description;
        this.unitPrice = unitPrice;
    }
    public LineItem(){
        super();
    }
    public void setQuantity(Long quantity) {

        this.quantity = quantity;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }


    public Long getId() {
        return id;
    }

    public Long getQuantity() {
        return quantity;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getLineItemTotal(){
        return BigDecimal.valueOf(quantity).multiply(unitPrice).setScale(2, RoundingMode.HALF_UP);
    }

}
