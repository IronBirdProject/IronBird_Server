package com.trip.IronBird_Server.plan.domain;

import com.trip.IronBird_Server.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.N;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name="started_Date")
    private String startedDate;

    @Column(name="end_Date")
    private String endDate;

    @CreationTimestamp
    @Column(name="created_time")
    private LocalDateTime created_time;

    @UpdateTimestamp
    @Column(name="modified_time")
    private LocalDateTime modified_time;


    @Builder
    public Plan(User user, String startedDate, String endDate, LocalDateTime created_time, LocalDateTime modified_time) {

        this.user = user;
        this.startedDate = startedDate;
        this.endDate = endDate;
        this.created_time = created_time;
        this.modified_time = modified_time;
    }
    public void updateDates(String startedDate, String endDate) {
        this.startedDate = startedDate;
        this.endDate = endDate;
        this.modified_time = LocalDateTime.now();
    }
}
