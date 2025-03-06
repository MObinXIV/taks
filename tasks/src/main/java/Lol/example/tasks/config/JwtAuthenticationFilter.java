package Lol.example.tasks.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private  final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                   @NonNull HttpServletResponse response,
                                   @NonNull FilterChain filterChain) throws ServletException, IOException {
// Retrieve the Authorization header from the incoming HTTP request
        final String authHeader= request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        // If the Authorization header is missing or doesn't start with "Bearer ",
// allow the request to proceed without authentication instead of throwing an error.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        // Extract the token from the Authorization header
        jwt = authHeader.substring(7);
    // Extract user email from the token
        userEmail = jwtService.extractUserName(jwt);

        if(userEmail!=null && SecurityContextHolder.getContext().getAuthentication()==null)//his condition is used to prevent overwriting an existing authentication and only proceed with authentication if none is set.

        {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,  // Authenticated user details
                        null,  // No credentials needed since it's already verified
                        userDetails.getAuthorities() // Retrieves user roles/permissions
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                        // Attaches additional request details (e.g., IP address, session ID) to the authentication object.
                );
                // Stores the authentication object in the security context, marking the user as authenticated.
                SecurityContextHolder.getContext().setAuthentication(authToken);

            }
        }

        filterChain.doFilter(request, response);

    }
}
