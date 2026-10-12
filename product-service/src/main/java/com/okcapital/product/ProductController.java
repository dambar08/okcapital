package com.okcapital.product;

import com.okcapital.product.dto.CreateProductRequest;
import com.okcapital.product.entity.LoanProduct;
import com.okcapital.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<LoanProduct> list() {
        return productService.findAll();
    }

    @PostMapping
    public ResponseEntity<LoanProduct> create(@RequestBody CreateProductRequest request) {
        LoanProduct product = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }
}
