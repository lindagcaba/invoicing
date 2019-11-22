package com.ioco.lindagcaba.Invoicing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
public class LineItem implements Serializable{
    @Id
    @GeneratedValue
    private Long id;
    @NonNull
    @Positive
    @Column
    private Long quantity;
    @NotBlank
    @Column
    private String description;
    @NonNull
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

    public LineItem(Long id, Long quantity, String description, BigDecimal unitPrice, Invoice invoice) {
        this.id = id;
        this.quantity = quantity;
        this.description = description;
        this.unitPrice = unitPrice;
        this.invoice = invoice;
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
        return BigDecimal.valueOf(quantity).multiply(unitPrice).setScale(2,BigDecimal.ROUND_HALF_UP);
    }

}
