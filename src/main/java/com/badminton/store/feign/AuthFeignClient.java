package com.badminton.store.feign;

import com.badminton.store.dto.AuthTokenRequest;
import feign.Headers;
import feign.RequestLine;
import java.util.Map;

public interface AuthFeignClient {
    @RequestLine("POST /api/token")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Map<String, Object> loginByEmail(Map<String, ?> data);
}