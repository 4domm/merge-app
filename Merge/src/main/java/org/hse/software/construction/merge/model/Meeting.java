package org.hse.software.construction.merge.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "meetings")
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    String description;
    @ManyToOne
    User creator;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "meeting_users",
            joinColumns = @JoinColumn(name = "meeting_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    List<User> members;
    LocalDateTime startTime;
    String place;
    LocalDateTime endTime;
    boolean notified;
}
