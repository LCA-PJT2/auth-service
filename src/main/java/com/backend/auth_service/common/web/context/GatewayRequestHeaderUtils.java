package com.backend.auth_service.common.web.context;

import com.backend.auth_service.common.code.ErrorCode;
import com.backend.auth_service.common.exception.BusinessException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class GatewayRequestHeaderUtils {
    public static String getRequestHeaderParamAsString(String key) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return requestAttributes.getRequest().getHeader(key);
    }

    public static String getUserId() {
        String userId = getRequestHeaderParamAsString("X-Auth-UserId");
        if (userId == null) {
            return null;
        }
        return userId;
    }

    public static String getClientDevice() {
        String clientDevice = getRequestHeaderParamAsString("X-Client-Device");
        if (clientDevice == null) {
            return null;
        }
        return clientDevice;
    }

    public static String getClientAddress() {
        String clientAddress = getRequestHeaderParamAsString("X-Client-Address");
        if (clientAddress == null) {
            return null;
        }
        return clientAddress;
    }

    public static String getUserIdOrThrowException() {
        String userId = getUserId();
        if (userId == null) {
            throw new BusinessException(ErrorCode.HEADER_USER_ID_MISSING);
        }
        return userId;
    }

    public static String getClientDeviceOrThrowException() {
        String clientDevice = getClientDevice();
        if (clientDevice == null) {
            throw new BusinessException(ErrorCode.HEADER_DEVICE_MISSING);
        }
        return clientDevice;
    }

    public static String getClientAddressOrThrowException() {
        String clientAddress = getClientAddress();
        if (clientAddress == null) {
            throw new BusinessException(ErrorCode.HEADER_ADDRESS_MISSING);
        }
        return clientAddress;
    }
}
