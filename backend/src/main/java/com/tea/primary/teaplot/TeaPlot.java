package com.tea.primary.teaplot;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tea_plot")
@Data
public class TeaPlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long farmerId;
    private String plotName;
    private BigDecimal areaMu;
    @Column(name = "altitude_m")
    private Integer altitudeM;
    private String pickingSeason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
