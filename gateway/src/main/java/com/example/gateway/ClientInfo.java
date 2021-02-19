package com.example.gateway;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientInfo {
    private String ip;
    private String agentType;
    private String requestId;
}
