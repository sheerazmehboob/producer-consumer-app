package com.spring.producer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebTokenDto {
    private String access_token;
    private String expires_in;
    private String token_type;
    private String scope;
}
