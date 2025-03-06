package Lol.example.tasks.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "9LEeDVv7KoTB0YobwsiX0MwTuj56LroUEhBDahKICQSRef0jWwBCF51Wb4RKUnNa";

// Payload from token work
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

    //Token work

    public String generateToken(UserDetails userDetails){
        // Generate a jwt for user with default claims
        return generateToken(new HashMap<>(), userDetails); // Map.of() creates an empty map, so any extra claims will be ignored
    }

    // function generate token for specified user
    String generateToken (Map<String, Object> extraClaims, UserDetails userDetails){
        return Jwts
                .builder()
                .setSubject(userDetails.getUsername()) // Sets the subject (usually the username/email)
                .addClaims(extraClaims) // Adds any additional claims (e.g., roles, permissions)
                .setIssuedAt(new java.util.Date()) // Sets the "issued at" claim to the current time
                .setExpiration(new java.util.Date(System.currentTimeMillis() + 3600000)) // Sets the expiration time (1 hour)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Signs the JWT with the secret key
                .compact(); // Compacts the JWT into a compact, URL-safe string
    }

    // function to see if token for specific user is valid or not
    boolean isTokenValid(String token ,UserDetails userDetails){
      // get the token from specific user
        String userName = extractUserName(token);
        // checks if the username of token matches with the username of userDetails and if token is not expired
        return userName.equals(userDetails.getUsername()) && !isTokenExpired(token);  // Checks if the username matches and if the token is not expired
    }

    // Function to check if the token expired
    private boolean isTokenExpired(String token) {
        // Compares the token's expiration time with the current time, returning true if expired
        return extractExpirationToken(token).before(new Date());
    }
    // function get the date of the current token time
    private Date extractExpirationToken(String token) {
        // Extracts the expiration time from the JWT token
        return extractClaim(token, Claims::getExpiration);

    }

}
