package com.tea.primary.processing;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "processing_batch")
@Data
public class ProcessingBatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String batchNo;
    private Long purchaseOrderId;
    private String currentProcess;
    private String batchStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
