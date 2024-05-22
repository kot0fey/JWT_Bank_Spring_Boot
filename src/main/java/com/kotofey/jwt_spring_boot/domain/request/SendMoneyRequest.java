package com.kotofey.jwt_spring_boot.domain.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendMoneyRequest {
    private String username;
    private Float amount;
}
