package com.example.mySchedule.services;

import com.example.mySchedule.config.EnvVariablesConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {
    @Autowired
    private EnvVariablesConfig variablesConfig;

    /**
     * En este metodo se pueden definir las claims si fuera necesario
     * @param user
     * @return
     */
    public String getTokenService(UserDetails user){
        return getToken(new HashMap<>(),user);
    }

    //Genera el token para enviar
    public String getToken(Map<String, Object> claims, UserDetails user){
        return Jwts
                .builder()
                .setClaims(claims)      //Aqui se podria haber metido informacion adicional
                .setSubject(user.getUsername())     //Creador del token
                .setIssuedAt(new Date(System.currentTimeMillis()))  //Creacion del token
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))  //Cuando deja de ser valido el token
                .signWith(getKey(), SignatureAlgorithm.HS256)       //Codificacion
                .compact();
    }

    private Key getKey(){
        byte[] keyBytes = Decoders.BASE64.decode(variablesConfig.getS_KEY());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUsernameFromToken(String token) {
        return getClaim(token, (Claims claims)->claims.getSubject());
//        return getClaim(token, Claims::getSubject);
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(getKey())
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }

    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }
}
