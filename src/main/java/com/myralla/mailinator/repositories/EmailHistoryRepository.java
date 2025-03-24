package com.myralla.mailinator.repositories;

import com.myralla.mailinator.models.EmailHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailHistoryRepository extends JpaRepository<EmailHistory, Long> {
}
