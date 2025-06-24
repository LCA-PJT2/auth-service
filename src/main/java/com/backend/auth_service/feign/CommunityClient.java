package com.backend.auth_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "community-service", path = "/api/community/v1/user")
public interface CommunityClient {
    @DeleteMapping("/clear/{userId}")
    void deleteCommunityDataByUser(@PathVariable("userId") String userId);
}
