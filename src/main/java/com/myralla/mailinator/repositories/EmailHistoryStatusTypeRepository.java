package com.myralla.mailinator.repositories;

import com.myralla.mailinator.models.EmailHistoryStatusType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailHistoryStatusTypeRepository extends JpaRepository<EmailHistoryStatusType, Long> {
    EmailHistoryStatusType findByStatus(String status);
}
