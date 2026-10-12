package com.okcapital.product;

import com.okcapital.product.dto.CreateProductRequest;
import com.okcapital.product.entity.LoanProduct;
import com.okcapital.product.repository.ProductRepository;
import com.okcapital.product.service.ProductService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProductServiceTest {

    @Test
    void createProductStoresProductDetails() {
        ProductRepository repository = mock(ProductRepository.class);
        when(repository.save(any(LoanProduct.class))).thenAnswer(invocation -> invocation.getArgument(0));
        ProductService service = new ProductService(repository);

        LoanProduct product = service.createProduct(new CreateProductRequest(
                "PERSONAL_12M",
                "Personal Loan",
                5000.00,
                50000.00,
                12,
                60,
                12.75,
                149.00,
                650));

        assertNotNull(product.getProductId());
        assertEquals("PERSONAL_12M", product.getCode());
        assertEquals(12.75, product.getInterestRate(), 0.0001);
    }
}
