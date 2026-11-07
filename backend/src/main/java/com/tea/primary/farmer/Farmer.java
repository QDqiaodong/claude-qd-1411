package com.tea.primary.farmer;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "farmer")
@Data
public class Farmer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String phone;
    private String villageGroup;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
