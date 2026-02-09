package com.example.usermanagement.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDTO {
    private String token;
    private String tipo;
    private Long expiracaoEm;
}
