package com.okcapital.product.service;

import com.okcapital.shared.UuidV8Generator;
import com.okcapital.product.dto.CreateProductRequest;
import com.okcapital.product.entity.LoanProduct;
import com.okcapital.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public LoanProduct createProduct(CreateProductRequest request) {
        LoanProduct product = new LoanProduct(
                UuidV8Generator.generate(),
                request.code(),
                request.name(),
                request.minAmount(),
                request.maxAmount(),
                request.minTenureMonths(),
                request.maxTenureMonths(),
                request.interestRate(),
                request.processingFee(),
                request.minCreditScore());
        return productRepository.save(product);
    }

    public List<LoanProduct> findAll() {
        return productRepository.findAll();
    }
}
