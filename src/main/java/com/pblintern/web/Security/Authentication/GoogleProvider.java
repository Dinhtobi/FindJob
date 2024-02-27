package com.pblintern.web.Security.Authentication;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.pblintern.web.Entities.User;
import com.pblintern.web.Exceptions.BadCredentialsException;
import com.pblintern.web.Repositories.UserRepository;
import com.pblintern.web.Security.Model.TokenSecurity;
import com.pblintern.web.Security.Model.UserSecurity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class GoogleProvider implements AuthenticationProvider {

    @Autowired
    private UserRepository userRepository;

    @Value("${google.client-id}")
    private String clientId;

    private static final JacksonFactory jacksonFactory = new JacksonFactory();
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        System.out.println("token here");
       try {
           TokenSecurity token = (TokenSecurity) authentication;
           GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), jacksonFactory)
                   .setAudience(Collections.singleton(clientId))
                   .build();
           GoogleIdToken idToken = verifier.verify(token.getToken());
           if(token != null){
               GoogleIdToken.Payload payload =idToken.getPayload();
               String email = payload.getEmail();
               Optional<User> userOptional = userRepository.findByEmail(email);
               if(userOptional.isEmpty()){
                    return new UserSecurity(null,null);
               }else{
                   User user = userOptional.get();
                   if(!user.isNonBlock()){
                       throw new LockedException("User is block");
                   }
                   return new UserSecurity(user, user.getRoles().stream().map(
                           role -> new SimpleGrantedAuthority("ROLE_" + role.getName().toString())
                   ).collect(Collectors.toList()));
               }
           }else{
//               throw new BadCredentialsException("Token is null!");
               log.info("token wrong");
               return null;
           }
       }catch(GeneralSecurityException | IOException e){
           System.out.println("errror here");
           return null;
//           throw new BadCredentialsException("Authentication error {}" , e.getMessage());
       }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(TokenSecurity.class);
    }
}
