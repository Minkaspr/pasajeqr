package com.mk.pasajeqr.driver.response;

import com.mk.pasajeqr.utils.DriverStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverStatusCountRS {
    private DriverStatus status;
    private long count;
}