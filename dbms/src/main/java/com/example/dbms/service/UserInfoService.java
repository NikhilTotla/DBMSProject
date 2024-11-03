//package com.example.dbms.service;
//
//import com.example.dbms.entity.UserInfo;
//import com.example.dbms.repository.UserInfoRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//public class UserInfoService implements UserDetailsService {
//
//    @Autowired
//    private UserInfoRepository repository;
//
//    @Autowired
//    private PasswordEncoder encoder;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<UserInfo> userDetail = repository.findByName(username); // Assuming 'email' is used as username
//
//        // Converting UserInfo to UserDetails
//        return userDetail.map(UserInfoDetails::new)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
//    }
//
//    public String addUser(UserInfo userInfo) {
//        // Encode password before saving the user
//        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
//        repository.save(userInfo);
//        return "User Added Successfully";
//    }
//}

package com.example.dbms.service;

import com.example.dbms.entity.Admin;
import com.example.dbms.entity.Client;
import com.example.dbms.entity.Worker;
import com.example.dbms.repository.ClientRepository;
import com.example.dbms.repository.WorkerRepository;
import com.example.dbms.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Service
public class UserInfoService implements UserDetailsService {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private WorkerRepository workerRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Check if the user is a client
        Optional<Client> client = clientRepository.findByUsername(username);
        if (client.isPresent()) {
            return new org.springframework.security.core.userdetails.User(
                    client.get().getUsername(),
                    client.get().getPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_CLIENT"))
            );
        }

        // Check if the user is a worker
        Optional<Worker> worker = workerRepository.findByUsername(username);
        if (worker.isPresent()) {
            return new org.springframework.security.core.userdetails.User(
                    worker.get().getUsername(),
                    worker.get().getPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_WORKER"))
            );
        }
        Optional<Admin> admin = adminRepository.findByUsername(username);
        if (admin.isPresent()) {
            return new org.springframework.security.core.userdetails.User(
                    admin.get().getUsername(),
                    admin.get().getPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
            );
        }

        throw new UsernameNotFoundException("User not found");
    }

//    public ResponseEntity<Client> addClient(Client client) {
//        try {
//            client.setPassword(encoder.encode(client.getPassword()));
//            Client savedClient = clientRepository.save(client);
//            return new ResponseEntity<>(savedClient, HttpStatus.CREATED);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    public ResponseEntity<Worker> addWorker(Worker worker) {
//        try {
//            worker.setPassword(encoder.encode(worker.getPassword()));
//            Worker savedWorker = workerRepository.save(worker);
//            return new ResponseEntity<>(savedWorker, HttpStatus.CREATED);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    public String addAdmin(Admin admin) {
//        // Encode password before saving the admin
//        admin.setPassword(encoder.encode(admin.getPassword()));
//        adminRepository.save(admin);
//        return "Admin Added Successfully";
//    }

}
