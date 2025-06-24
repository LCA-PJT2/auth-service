package com.backend.auth_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "qna-service", path = "/api/qna/v1/user")
public interface QnaClient {
    @DeleteMapping("/clear/{userId}")
    void deleteQnaDataByUser(@PathVariable("userId") String userId);
}
