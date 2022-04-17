package com.lucia.nft.config.AuthProvider;

import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import javax.annotation.PostConstruct;

import com.lucia.nft.dto.CredentialsDto;
import com.lucia.nft.dto.UserDto;
import com.lucia.nft.services.AuthenticationService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class UserAuthenticationProvider {

    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey;

    private final AuthenticationService authenticationService;

    public UserAuthenticationProvider(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());    //this is to avoid having the raw secret key available in the JVM
    }

    /*CLAIMS:

    “sub”: es el asunto, que coincide con el identificador la persona que se identifica.
    “exp”: la hora de expiración, a partir de la cual, el token no será válido.  
    
    */

    public String createToken(String login) {
        Claims claims = Jwts.claims().setSubject(login);  //“sub”
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
   
    public Authentication validateToken(String token) {
        Jwts.builder().setExpiration(null);             //"exp"

        String login = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();

        UserDto user = authenticationService.findByLogin(login);
        return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
    }
    

    public Authentication validateCredentials(CredentialsDto credentialsDto) {
        UserDto user = authenticationService.authenticate(credentialsDto);
        return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
    }
}
