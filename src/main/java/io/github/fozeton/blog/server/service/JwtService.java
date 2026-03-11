package io.github.fozeton.blog.server.service;

import io.github.fozeton.blog.server.exceptions.InvalidTokenException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

@Service
public class JwtService {
    private PublicKey publicKey;

    @PostConstruct
    private void init() {
        try {
            byte[] keyByte = Files.readAllBytes(Paths.get("token.pub"));
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyByte);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            publicKey = kf.generatePublic(spec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public void checkToken(String token) {
        try {
            Jwts.parser().verifyWith(publicKey).build().parseSignedClaims(token.substring(7)).getPayload();
        } catch (JwtException e) {
            throw new InvalidTokenException(e.getMessage());
        }
    }
}
