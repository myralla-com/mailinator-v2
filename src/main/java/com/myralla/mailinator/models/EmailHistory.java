package com.myralla.mailinator.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class EmailHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recipient")
    private String recipient;

    @Column(name = "subject")
    private String subject;

    @Column(name = "body", columnDefinition = "LONGTEXT")
    private String body;

    @ManyToOne
    @JoinColumn(name = "email_history_status_type_id")
    private EmailHistoryStatusType emailHistoryStatusType;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
