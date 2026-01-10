package com.lmkhanhs.home_net.utils;

import org.springframework.stereotype.Component;

import com.lmkhanhs.home_net.entities.TenantEntity;
import com.lmkhanhs.home_net.exceptions.AppException;
import com.lmkhanhs.home_net.exceptions.ErrorCode;
import com.lmkhanhs.home_net.repositories.TenantRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class RequestHttpUitlls {
    TenantRepository tenantRepository;    
    public  String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    public  String getTenantIDValid(HttpServletRequest request) {
        TenantEntity tenant = this.tenantRepository.findByName(request.getHeader("X-Tenant-Name"))
            .orElseThrow(() -> new AppException(ErrorCode.TENANT_NOT_FOUND));
        return tenant.getName();
    }
}