package com.backend.auth_service.api.open;

import com.backend.auth_service.common.web.context.GatewayRequestHeaderUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/test/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class GatewayController {
    
    @GetMapping("/healthy/route")
    public ResponseEntity<String> testGatewayHeaders() {
        log.info("test gateway routing");

        String userId = GatewayRequestHeaderUtils.getUserId();

        if (userId == null) {
            return ResponseEntity.ok("null");
        }
        return ResponseEntity.ok(userId);
    }

    @GetMapping("/healthy/auth")
    public ResponseEntity<String> testGatewayAuth() {
        log.info("test gateway auth");

        String userId = GatewayRequestHeaderUtils.getUserId();

        if (userId == null) {
            return ResponseEntity.ok("null");
        }
        return ResponseEntity.ok(userId);
    }
}
