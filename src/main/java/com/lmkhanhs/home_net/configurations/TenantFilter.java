package com.lmkhanhs.home_net.configurations;

import java.io.IOException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.lmkhanhs.home_net.context.TenantContext;
import com.lmkhanhs.home_net.repositories.TenantRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TenantFilter extends OncePerRequestFilter {
    @Autowired
    private TenantRepository tenantRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String tenantId = request.getHeader("X-Tenant-Name");
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        // case 0 : tenantId is null
        if (tenantId == null || tenantId.isEmpty()) {
            forbidden(response, "X-Tenant-Name header is required");
            return;
        }
        // case 0.5 : tenantId not exist
        if (tenantId != null) {
            boolean exists = tenantRepository.existsById(tenantId);
            if (!exists) {
                forbidden(response, "Tenant ID does not exist");
                return;
            }
        }
        // case 1: authentication is not null
        if (authentication != null && authentication.isAuthenticated()) {
            var principal = authentication.getPrincipal();
            var jwt = (org.springframework.security.oauth2.jwt.Jwt) principal;
            String jwtTenantId = jwt.getClaim("tenant-name");
            if (Objects.isNull(jwtTenantId)) {
                forbidden(response, "Tenant Name doesn't exist in token");
                return;
            }
            if ( !jwtTenantId.equals(tenantId)){
                forbidden(response, "Tenant ID in header does not match with Tenant ID in token");
                return;
            }
        }
        try {
            TenantContext.setTenant(tenantId);
            filterChain.doFilter(request, response);
        } 
        finally {
            TenantContext.clear();
        }
        
    }

    private void forbidden(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        response.getWriter().write("""
            {
              "code": 403,
              "message": "%s"
            }
        """.formatted(message));
        response.flushBuffer();
    }
    
}
