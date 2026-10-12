package com.okcapital.application.repository;

import com.okcapital.application.entity.LoanApplication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<LoanApplication, java.util.UUID> {
}
