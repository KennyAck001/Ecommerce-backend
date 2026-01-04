package com.ecommerce.EcomProj.Controller;

import com.ecommerce.EcomProj.Model.AppRoles;
import com.ecommerce.EcomProj.Model.Role;
import com.ecommerce.EcomProj.Model.Users;
import com.ecommerce.EcomProj.Repository.RoleRepository;
import com.ecommerce.EcomProj.Repository.UserRepository;
import com.ecommerce.EcomProj.security.jwt.JwtUtils;
import com.ecommerce.EcomProj.security.jwt.requests.LoginRequest;
import com.ecommerce.EcomProj.security.jwt.requests.SignupRequest;
import com.ecommerce.EcomProj.security.jwt.requests.UserInfoResponse;
import com.ecommerce.EcomProj.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserRepository  userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("signin")
    public ResponseEntity<?> signin(@RequestBody LoginRequest loginRequest){
        // Authentication is an interface that is used to define or store Users or user's details
        Authentication authentication = null;
        try {
            // authenticating the user using authenticationManager with UsernamePasswordAuthenticationToken
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                            loginRequest.getPassword())
            );
        }catch (Exception ex){
            Map<String, Object> map = new HashMap<>();
            map.put("message","Bad Credentials");
            map.put("status","false");
            return  new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }
        // Storing the user in Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // using cookies to store the jwt
        ResponseCookie jwtCookie = jwtUtils.generateJwtFromCookie(userDetails);
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();

        UserInfoResponse response =
                new UserInfoResponse(userDetails.getId(),userDetails.getUsername(), roles, jwtCookie.getValue());

        // returning response with cookie to the client.
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,
                                            jwtCookie.toString()).body(response);
    }


    @PostMapping("signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest signupRequest){
        if (userRepository.existsByUserName(signupRequest.getUserName())){
            return new ResponseEntity<>("Username is already taken", HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())){
            return new ResponseEntity<>("Email is already in use", HttpStatus.BAD_REQUEST);
        }

        // Creating a new User
        Users user = new Users(signupRequest.getUserName(), signupRequest.getEmail(),
                        passwordEncoder.encode(signupRequest.getPassword()));

        Set<String> roles = signupRequest.getRoles();
        Set<Role> roleSet = new HashSet<>();

        // if no roles then the user is by default a user
        if (roles == null){
            Role userRole = roleRepository.findByRoleName(AppRoles.ROLES_USER)
                    .orElseThrow(()->new RuntimeException("Error: Role not found"));
            roleSet.add(userRole);
        }else {
            // setting multiple roles if there is
            roles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByRoleName(AppRoles.ROLES_ADMIN)
                                .orElseThrow(()->new RuntimeException("Role is not found"));
                        roleSet.add(adminRole);
                        break;
                    case "seller":
                        Role sellerRole = roleRepository.findByRoleName(AppRoles.ROLES_SELLER)
                                .orElseThrow(()->new RuntimeException("Role is not found"));
                        roleSet.add(sellerRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByRoleName(AppRoles.ROLES_USER)
                                .orElseThrow(()->new RuntimeException("Role is not found"));
                        roleSet.add(userRole);
                }
            });
        }

        user.setRoles(roleSet);
        userRepository.save(user);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/username")
    public String currentUsername(Authentication authentication){
        if (authentication != null){
            return  authentication.getName();
        }else  {
            return  null;
        }
    }

    @GetMapping("/user")
    public ResponseEntity<UserInfoResponse> getCurrentUser(Authentication authentication){
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();

        UserInfoResponse response =
                new UserInfoResponse(userDetails.getId(),userDetails.getUsername(), roles);
        return ResponseEntity.ok().body(response);

    }


    @PostMapping("/signout")
    public ResponseEntity<?> signout(){
        ResponseCookie cookie = jwtUtils.getCleanCookie();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("You have been signed out");
    }
}
