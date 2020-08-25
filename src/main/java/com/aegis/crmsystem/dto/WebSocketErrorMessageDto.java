package com.aegis.crmsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketErrorMessageDto {
    private Integer status;
    private String message;
}
