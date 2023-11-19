package com.example.springwebflux;

import org.springframework.web.server.ServerWebExchange;

public abstract class BaseController {
    SpringWebfluxApplication.OnlineUser onlineUser;
    void init(ServerWebExchange webExchange){
        onlineUser = webExchange.getAttribute("onlineUser");
    }
}
