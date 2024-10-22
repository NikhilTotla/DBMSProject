//package com.example.dbms.service;
//
//import com.example.dbms.entity.UserInfo;
//import com.example.dbms.repository.UserInfoRepository;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import java.security.Key;
//import java.util.*;
//import java.util.function.Function;
//
//@Component
//public class JwtService {
//
//    // Replace this with a secure key in a real application, ideally fetched from environment variables
//    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
//    @Autowired
//    private UserInfoRepository repository;
//    // Generate token with given user name
////    public String generateToken(String userName) {
////        Map<String, Object> claims = new HashMap<>();
////        return createToken(claims, userName);
////    }
//    public String generateToken(String userName) {
//        Map<String, Object> claims = new HashMap<>();
//        Optional<UserInfo> userInfo = repository.findByName(userName);
//        userInfo.ifPresent(userinfo -> {
//            // Assuming userinfo.getRoles() returns a String
//            claims.put("roles", List.of(userinfo.getRoles())); // Wrapping the String in a List
//        });
//
////        userInfo.ifPresent(userinfo->{claims.put("roles", List.of(userinfo.getRoles())});
////        claims.put("roles", List.of("ROLE_ADMIN")); // Add roles or other claims
//        return createToken(claims, userName);
//    }
//
//    // Create a JWT token with specified claims and subject (user name)
//    private String createToken(Map<String, Object> claims, String userName) {
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(userName)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30)) // Token valid for 30 minutes
//                .signWith(getSignKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    // Get the signing key for JWT token
//    private Key getSignKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
//
//    // Extract the username from the token
//    public String extractUsername(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//
//    // Extract the expiration date from the token
//    public Date extractExpiration(String token) {
//        return extractClaim(token, Claims::getExpiration);
//    }
//
//    // Extract a claim from the token
//    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = extractAllClaims(token);
//        return claimsResolver.apply(claims);
//    }
//
//    // Extract all claims from the token
//    private Claims extractAllClaims(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(getSignKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//    // Check if the token is expired
//    private Boolean isTokenExpired(String token) {
//        return extractExpiration(token).before(new Date());
//    }
//
//    // Validate the token against user details and expiration
//    public Boolean validateToken(String token, UserDetails userDetails) {
//        final String username = extractUsername(token);
//        System.out.println(userDetails.getUsername());
//        System.out.println(username);
//        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
//    }
//}
package com.example.dbms.service;

import com.example.dbms.entity.Admin;
import com.example.dbms.entity.Client;
import com.example.dbms.entity.Worker;
import com.example.dbms.repository.AdminRepository;
import com.example.dbms.repository.ClientRepository;
import com.example.dbms.repository.WorkerRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtService {

    // Replace this with a secure key in a real application, ideally fetched from environment variables
    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private WorkerRepository workerRepository;

    // Generate token based on the type of user (Client or Worker)
    public String generateToken(String userName) {
        Map<String, Object> claims = new HashMap<>();

        // First, check if the user is a Client
        Optional<Client> client = clientRepository.findByUsername(userName);
        if (client.isPresent()) {
            // Add client roles or other information as claims
            claims.put("roles", List.of("ROLE_CLIENT")); // Assuming getRoles() returns a String
            return createToken(claims, userName);
        }

        // If the user is not a client, check if the user is a Worker
        Optional<Worker> worker = workerRepository.findByUsername(userName);
        if (worker.isPresent()) {
            // Add worker roles or other information as claims
            claims.put("roles", List.of("ROLE_WORKER"));
            return createToken(claims, userName);
        }
        Optional<Admin> admin = adminRepository.findByUsername(userName);
        if (admin.isPresent()) {
            // Add admin roles or other information as claims
            claims.put("roles", List.of("ROLE_ADMIN"));
            return createToken(claims, userName);
        }

        // If the user is neither a Client nor a Worker, throw an exception
        throw new UsernameNotFoundException("User not found in both client and worker repositories");
    }

    // Create a JWT token with specified claims and subject (user name)
    private String createToken(Map<String, Object> claims, String userName) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30)) // Token valid for 30 minutes
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Get the signing key for JWT token
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Extract the username from the token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract the expiration date from the token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extract a specific claim from the token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extract all claims from the token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Check if the token is expired
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Validate the token against user details and check if it has expired
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
