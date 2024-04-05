package com.companie.companyproject.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@Slf4j
public class JwtUtil {

    private static final Logger logger = Logger.getLogger(JwtUtil.class.getName());
   public static final String SECRET = "5367566859703373367639792F423F452848284D6251655468576D5A71347437";

   private String createToken(Map<String, Object> claims, String userName){
       logger.log(Level.INFO, "Creating token for user: " + userName);
       return Jwts.builder()
               .setClaims(claims)
               .setSubject(userName)
               .setIssuedAt(new Date(System.currentTimeMillis()))
               .setExpiration(new Date(System.currentTimeMillis()+1000*60*30))
               .signWith(SignatureAlgorithm.HS256, getSignKey()).compact();
   }

    private SecretKey getSignKey() {
       byte[] keyBytes= Decoders.BASE64.decode(SECRET);
       return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String userName){
       Map<String,Object> claims = new HashMap<>();
       return createToken(claims,userName);
    }

    private Claims extractAllClaims(String token){
       logger.log(Level.INFO, "Extracting all claims from token: " + token);
       return Jwts
               .parser()
               .setSigningKey(getSignKey())
               .parseClaimsJws(token)
               .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
       final Claims claims = extractAllClaims(token);
       logger.log(Level.INFO, "Extracting claim from token: " + token);
       return claimsResolver.apply(claims);
    }

    public Date extractExpiration(String token){
       logger.log(Level.INFO, "Extracting expiration from token: " + token);
       return extractClaim(token, Claims::getExpiration);
    }

    public String extractUserName(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
            return claims.getSubject();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error extracting username from token: " + e.getMessage());
            return null;
        }
    }

    private Boolean isTokenExpired(String token){
       logger.log(Level.INFO, "Checking token expiration: " + token);
       return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails){
       logger.log(Level.INFO, "Validating token for user: " + userDetails.getUsername());
       final String userName = extractUserName(token);
       return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
