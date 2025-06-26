package com.mk.pasajeqr.stop;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "stop")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Stop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del paradero es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre del paradero debe tener entre 2 y 100 caracteres")
    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(nullable = false, updatable = false)
    private LocalDate createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
    }
}
