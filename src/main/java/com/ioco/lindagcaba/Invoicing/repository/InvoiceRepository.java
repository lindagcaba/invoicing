package com.ioco.lindagcaba.Invoicing.repository;

import com.ioco.lindagcaba.Invoicing.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice,Long> {
}
