package com.okcapital.product.repository;

import com.okcapital.product.entity.LoanProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<LoanProduct, java.util.UUID> {
}
