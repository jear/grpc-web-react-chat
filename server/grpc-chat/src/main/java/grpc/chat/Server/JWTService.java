package grpc.chat.Server;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

public class JWTService {
    private static JWTService jwtService;

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    private final SecretKey key = Keys.secretKeyFor(signatureAlgorithm);

    private JWTService() {}

    public static JWTService getJwtService() {
        if (jwtService == null) {
            jwtService = new JWTService();
        }
        return jwtService;
    }

    public String getJWT(String userName) {

        Key signingKey = getSigningKey();

        return Jwts.builder()
                .setSubject(userName)
                .signWith(signingKey, signatureAlgorithm)
                .compact();

    }

    private Key getSigningKey() {
        return new SecretKeySpec(key.getEncoded(), signatureAlgorithm.getJcaName());
    }

    public String parseJwt(String jwt) throws Exception {

        Claims claims = Jwts.parser()
                .setSigningKey(getSigningKey())
                .parseClaimsJws(jwt).getBody();

        return claims.getSubject();
    }

}
