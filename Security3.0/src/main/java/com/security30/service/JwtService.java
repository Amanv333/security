package com.security30.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class JwtService {

    private Algorithm algorithm;
    @Value("${jwt.issuer}")
    private String issuer;
    @Value("${jwt.expiry.time}")
    private int expiryTime;
    @Value("${jwt.secret.key}")
    private String secretKey;

    private final static String USER_NAME = "";

    @PostConstruct
    public void postConstruct(){
        this.algorithm = Algorithm.HMAC256(secretKey);
    }

    public String generateToken(String username) {
        //computer engineer is unemployed
        return JWT.create().withClaim(USER_NAME, username)
                .withExpiresAt(new Date(System.currentTimeMillis() + expiryTime))
                .withIssuer(issuer)
                .sign(algorithm);
    }
    public String extractUserName(String Token) {
        try {
            DecodedJWT decodedJWT = JWT.require(algorithm).withIssuer(issuer).build()
                    .verify(Token);
            return decodedJWT.getClaim(USER_NAME).asString();
        } catch (Exception e) {
            return null;
        }

    }
}
