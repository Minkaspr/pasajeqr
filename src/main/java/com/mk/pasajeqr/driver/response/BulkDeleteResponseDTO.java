package com.mk.pasajeqr.driver.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BulkDeleteResponseDTO {
    private List<Long> deletedIds;
    private List<Long> notFoundIds;
}
