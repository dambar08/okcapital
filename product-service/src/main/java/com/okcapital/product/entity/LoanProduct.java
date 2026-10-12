package com.okcapital.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import com.okcapital.shared.IdentifiableEntity;

import java.util.UUID;

@Entity
@Table(name = "loan_products")
@AttributeOverride(name = "id", column = @Column(name = "product_id"))
public class LoanProduct extends IdentifiableEntity {
    @Column(nullable = false, unique = true)
    private String code;
    @Column(nullable = false)
    private String name;
    private double minAmount;
    private double maxAmount;
    private int minTenureMonths;
    private int maxTenureMonths;
    private double interestRate;
    private double processingFee;
    private int minCreditScore;

    protected LoanProduct() {
    }

    public LoanProduct(UUID productId, String code, String name, double minAmount, double maxAmount,
            int minTenureMonths, int maxTenureMonths, double interestRate, double processingFee,
            int minCreditScore) {
        super(productId);
        this.code = code;
        this.name = name;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.minTenureMonths = minTenureMonths;
        this.maxTenureMonths = maxTenureMonths;
        this.interestRate = interestRate;
        this.processingFee = processingFee;
        this.minCreditScore = minCreditScore;
    }

    public UUID getProductId() {
        return getId();
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public double getMinAmount() {
        return minAmount;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public int getMinTenureMonths() {
        return minTenureMonths;
    }

    public int getMaxTenureMonths() {
        return maxTenureMonths;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public double getProcessingFee() {
        return processingFee;
    }

    public int getMinCreditScore() {
        return minCreditScore;
    }
}
