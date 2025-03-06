package Lol.example.tasks.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "9LEeDVv7KoTB0YobwsiX0MwTuj56LroUEhBDahKICQSRef0jWwBCF51Wb4RKUnNa";


    public String extractUserName(String jwt){
        return extractClaim(jwt, Claims::getSubject);
    }

    // Function to extract any claim
    public <T> T extractClaim (String token, Function<Claims,T> claimResolver){
        // Extracts a specific claim from the JWT token
        final  Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);

    }
    // Let's extract the payload from the jwt
    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey()) // Uses the signing key to validate the token
                .build()
                .parseClaimsJws(token) // Parses the JWT and verifies its signature
                .getBody(); // Returns the payload (claims)
    }

    private Key getSignInKey() {
        byte [] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes); // Creates an HMAC SHA key for signing JWTs
    }


}
