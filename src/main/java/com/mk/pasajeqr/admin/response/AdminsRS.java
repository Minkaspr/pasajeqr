package com.mk.pasajeqr.admin.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminsRS {
    private List<AdminUserItemRS> admins;
    private int currentPage;
    private int totalPages;
    private long totalItems;
}
