package com.mk.pasajeqr.bus;

import com.mk.pasajeqr.utils.BusStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "bus")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Bus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "La placa es obligatoria")
    @Size(max = 10, message = "La placa no debe exceder los 10 caracteres")
    @Column(nullable = false, unique = true, length = 10)
    private String plate;

    @NotBlank(message = "El modelo es obligatorio")
    @Size(max = 50, message = "El modelo no debe exceder los 50 caracteres")
    @Column(length = 50)
    private String model;

    @NotNull(message = "La capacidad es obligatoria")
    @Min(value = 1, message = "La capacidad debe ser al menos 1")
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "El estado del bus es obligatorio")
    private BusStatus status;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
