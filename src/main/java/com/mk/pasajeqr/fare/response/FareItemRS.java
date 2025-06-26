package com.mk.pasajeqr.fare.response;

import com.mk.pasajeqr.fare.Fare;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FareItemRS {
    private Long id;
    private String code;
    private String originStopName;
    private String destinationStopName;
    private BigDecimal price;

    public FareItemRS(Fare fare) {
        this.id = fare.getId();
        this.code = fare.getCode();
        this.originStopName = fare.getOriginStop().getName();
        this.destinationStopName = fare.getDestinationStop().getName();
        this.price = fare.getPrice();
    }
}
