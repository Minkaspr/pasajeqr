package com.mk.pasajeqr.driver;

import com.mk.pasajeqr.user.User;
import com.mk.pasajeqr.utils.DriverStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "driver")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank(message = "El número de licencia es obligatorio")
    @Size(max = 20, message = "El número de licencia no debe exceder los 20 caracteres")
    @Column(name = "license_number", nullable = false, unique = true, length = 20)
    private String licenseNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "El estado del conductor es obligatorio")
    private DriverStatus status;
}
