package ecommerce.user.jwt;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
                                    throws ServletException, IOException {

        System.out.println(">>> Executando JwtAuthenticationFilter");

        String token = extractTokenFromHeader(request);
        System.out.println(">>> Token extraído: " + token);

        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            Long authUserId = jwtTokenProvider.getAuthUserIdFromToken(token);
            System.out.println(">>> AuthUserId extraído do token: " + authUserId);

            // Atenção: definimos como principal um Long, para evitar cast incorreto
            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(authUserId, null, null);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            System.out.println(">>> Token inválido ou ausente");
        }

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
