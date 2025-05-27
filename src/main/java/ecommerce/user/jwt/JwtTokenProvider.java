package ecommerce.user.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

	private final SecretKey jwtSecret;
	private final long jwtExpirationMs;

	public JwtTokenProvider(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") long expiration) {
		this.jwtSecret = Keys.hmacShaKeyFor(secret.getBytes());
		this.jwtExpirationMs = expiration;
	}

	public String generateToken(Long authUserId) {
		Date now = new Date();
		Date expiry = new Date(now.getTime() + jwtExpirationMs);

		return Jwts.builder()
				.setSubject(authUserId.toString()) 
				.setIssuedAt(now)
				.setExpiration(expiry)
				.signWith(jwtSecret, SignatureAlgorithm.HS256)
				.compact();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	public Long getAuthUserIdFromToken(String token) {
		String subject = Jwts.parserBuilder()
				.setSigningKey(jwtSecret)
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
		return Long.parseLong(subject);
	}
}
