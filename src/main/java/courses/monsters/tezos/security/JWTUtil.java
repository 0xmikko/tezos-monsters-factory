package courses.monsters.tezos.security;

import java.security.Key;
import java.util.Date;
import javax.annotation.PostConstruct;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class JWTUtil {

    @Value("${tezosmonsters.jjwt.secret}")
    private String secret;

    @Value("${tezosmonsters.jjwt.expirationAccess}")
    private String expirationTimeAccessStr;

    @Value("${tezosmonsters.jjwt.expirationRefresh}")
    private String expirationTimeRefreshStr;

    private Long expirationTimeAccess;
    private Long expirationTimeRefresh;


    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationTimeAccess = Long.parseLong(expirationTimeAccessStr);
        this.expirationTimeRefresh = Long.parseLong(expirationTimeRefreshStr);
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public Date getExpirationDateFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

}
