package com.myralla.mailinator.repositories;

import com.myralla.mailinator.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRespository extends JpaRepository<Notification, Long> {
}
