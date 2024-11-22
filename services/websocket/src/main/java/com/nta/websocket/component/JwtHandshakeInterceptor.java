package com.nta.websocket.component;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtHandshakeInterceptor implements HandshakeInterceptor {
    CustomJwtDecoder jwtDecoder;
    JwtAuthenticationConverter jwtAuthenticationConverter;
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String uri = request.getURI().toString();
        String token = null;

        if (uri.contains("token=")) {
            String[] parts = uri.split("token=");
            if (parts.length > 1) {
                token = parts[1];
            }
        }
        if(token == null) return false;
        try{
            Jwt decodedJwt = jwtDecoder.decode(token);
            Authentication authentication = jwtAuthenticationConverter.convert(decodedJwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
