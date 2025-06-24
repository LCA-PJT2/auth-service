package com.backend.auth_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "community-service", url="http://community-service-service:8080/", path = "/backend/community/v1")
public interface CommunityClient {
    @PostMapping("/user/clear/{userId}")
    void deleteCommunityDataByUser(@PathVariable("userId") String userId);
}