package com.mk.pasajeqr.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserStatusRS {
    private Long userId;
    private boolean active;
}
