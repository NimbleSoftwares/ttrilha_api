package com.nimblesoftwares.ttrilha_api.utils;

import com.nimblesoftwares.ttrilha_api.application.user.command.SaveUserCommand;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;

public class JwtTestUtils {

  private static final KeyPair KEY_PAIR = generateRsaKey();
  private static final String SUB = "google-oauth2|12332103213912031321";

  public static String generateJwtWithAudience(String audience) throws Exception {

    JWTClaimsSet claims = new JWTClaimsSet.Builder()
        .subject(SUB)
        .issuer("http://localhost:8080/")
        .claim("email", "email@gmail.com")
        .claim("name", "full name")
        .claim("given_name", "first name")
        .claim("family_name", "last name")
        .claim("picture", "")
        .audience(List.of(audience))
        .issueTime(new Date())
        .expirationTime(Date.from(Instant.now().plusSeconds(3600)))
        .build();

    SignedJWT signedJWT = new SignedJWT(
        new JWSHeader(JWSAlgorithm.RS256),
        claims
    );

    signedJWT.sign(new RSASSASigner((RSAPrivateKey) KEY_PAIR.getPrivate()));

    return signedJWT.serialize();
  }

  public static KeyPair getKeyPair() {
    return KEY_PAIR;
  }

  private static KeyPair generateRsaKey() {
    try {
      KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
      generator.initialize(2048);
      return generator.generateKeyPair();
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  public static JwtClaimsSet createJwtClaimsSet(SaveUserCommand saveUserCommand) {
    return JwtClaimsSet.builder()
        .subject(SUB)
        .claim("email", saveUserCommand.email())
        .claim("name", saveUserCommand.displayName())
        .claim("given_name", saveUserCommand.firstName())
        .claim("family_name", saveUserCommand.lastName())
        .claim("picture", saveUserCommand.avatarUrl())
        .build();
  }
}