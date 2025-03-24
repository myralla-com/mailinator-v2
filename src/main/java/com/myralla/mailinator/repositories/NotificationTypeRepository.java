package com.myralla.mailinator.repositories;
import com.myralla.mailinator.models.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationTypeRepository extends JpaRepository<NotificationType, Long> {
    NotificationType findByType(String type);
}
