package com.ioco.lindagcaba.Invoicing.repository;

import com.ioco.lindagcaba.Invoicing.model.LineItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LineItemRepository extends JpaRepository<LineItem,Long> {
}
