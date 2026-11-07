package com.tea.primary.inventory;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "finished_inventory")
@Data
public class FinishedInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productBatchNo;
    private Long processingBatchId;
    private String grade;
    private BigDecimal weightKg;
    private LocalDateTime stockedAt;
    private String storageLocation;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
