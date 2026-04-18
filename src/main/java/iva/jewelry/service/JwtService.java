package iva.jewelry.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Component
public class JwtService {

    private final SecretKey Key;
    private  static  final long EXPIRATION_TIME = 86400000;
    public JwtService(){
        String secreteString = "5c70271e0b9da99a3515e6ca8fa74e201b4da453aaada9c4e0460d0bc85c21596ebfa6c1936f396f67a18da70f92d76ebb11b9659b7a1993a575c38155b4bbc284e02a2541e32dfd0f2be92c3ab56e93205cd7e1473fa91219ce76329da4fd5ced12bdd0908af8e40dbb98f15166820fd098752e6196ab343a4ee14e32293580c47b586c203005a42b900dc318991a21db018eb02d3eda191880fa8230636e774625eed73d2d54e80ca6ef9d6ee8b08c4c0cb9faf6474f884f35cbcedf93d3713e0c3ea27b1c4e1ed33b1d87e1cc9ddf8a1300d097c204d4b01f7b1a9fdc599367acf3f816bbbc6b1da6ebd11655d5213594616abec398117ab949fee75a1365";
        byte[] keyBytes = Base64.getDecoder().decode(secreteString.getBytes(StandardCharsets.UTF_8));
        this.Key = new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    public String generateToken(UserDetails userDetails){
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(Key)
                .compact();
    }

    public String generateRefreshToken(HashMap<String, Object> claims, UserDetails userDetails){
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(Key)
                .compact();
    }

    public String extractUsername(String token){
        return extractClaims(token, Claims::getSubject);
    }
    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction){
        return claimsTFunction.apply(Jwts.parser().verifyWith(Key).build().parseSignedClaims(token).getPayload());
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    public boolean isTokenExpired(String token){
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }

}
