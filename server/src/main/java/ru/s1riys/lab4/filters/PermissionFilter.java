package ru.s1riys.lab4.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.s1riys.lab4.annotation.HasPermissions;
import ru.s1riys.lab4.domain.dto.ErrorDetailsResponse;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

@Component
public class PermissionFilter extends OncePerRequestFilter {
    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Get method that processes the request
        String requestURI = request.getRequestURI();
        String httpMethod = request.getMethod();
        Method method = getMethodByRequest(requestURI, httpMethod);

        if (method != null && method.isAnnotationPresent(HasPermissions.class)) {
            HasPermissions hasPermissionsAnnotation = method.getAnnotation(HasPermissions.class);
            String[] requiredPermissions = hasPermissionsAnnotation.value();
            String[] userPermissions = getUserPermissions();

            // Check if user has required permissions
            if (!hasRequiredPermissions(userPermissions, requiredPermissions)) {
                writeErrorResponse(response, HttpStatus.FORBIDDEN, "User does not have required permissions");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    public Method getMethodByRequest(String requestURI, String httpMethod) {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            RequestMappingInfo mappingInfo = entry.getKey();

            boolean patternMatches = mappingInfo.getPatternValues().contains(requestURI);
            boolean methodMatches = mappingInfo.getMethodsCondition().getMethods().stream()
                    .anyMatch(method -> method.name().equalsIgnoreCase(httpMethod));

            if (patternMatches && methodMatches)
                return entry.getValue().getMethod();
        }
        return null; // No matching handler method found
    }

    private String[] getUserPermissions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getAuthorities().stream()
                    .map(grantedAuthority -> grantedAuthority.getAuthority())
                    .toArray(String[]::new);
        }
        return new String[] {}; // If user is not authenticated - empty permissions
    }

    private boolean hasRequiredPermissions(String[] userPermissions, String[] requiredPermissions) {
        for (String required : requiredPermissions) {
            boolean hasPermission = false;
            for (String userPermission : userPermissions) {
                if (userPermission.equals(required)) {
                    hasPermission = true;
                    break;
                }
            }
            if (!hasPermission) {
                return false;
            }
        }
        return true;
    }

    private void writeErrorResponse(HttpServletResponse response, HttpStatus status, String details)
            throws IOException {
        ErrorDetailsResponse errorResponse = ErrorDetailsResponse.builder()
                .error(status.name())
                .title("Authorization error")
                .details(details)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);

        response.setContentType("application/json");
        response.setStatus(status.value());
        response.getWriter().write(jsonResponse);
    }
}
