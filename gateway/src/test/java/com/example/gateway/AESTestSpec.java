package com.example.gateway;

import org.junit.jupiter.api.Test;

public class AESTestSpec {

    @Test
    void encryptValue(){
        System.out.println(
                AES.encrypt("{\n" +
                        "\"body\":\"Test Document\"\n" +
                        "}", "123456789abcdefg")
        );
    }
}
