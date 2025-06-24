package com.backend.auth_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "qna-service",
        url="http://qna-service-service:8080/",
        path = "/backend/qna/v1")
public interface QnaClient {
    @PostMapping("/user/clear/{userId}")
    void deleteQnaDataByUser(@PathVariable("userId") String userId);
}