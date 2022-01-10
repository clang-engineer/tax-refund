package com.szs.web;

import com.szs.domain.User;
import com.szs.repository.UserRepository;
import com.szs.security.SecurityUtils;
import com.szs.service.ScrapService;
import com.szs.service.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/szs")
@Transactional
public class UserResource {
    private Logger log = LoggerFactory.getLogger(UserResource.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final ScrapService scrapService;

    public UserResource(UserRepository userRepository,
                        PasswordEncoder passwordEncoder,
                        ScrapService scrapService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.scrapService = scrapService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDTO userDTO) throws Exception {
        log.debug("REST request to save User: {}", userDTO);

        User user = new User(userDTO);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRegNo(AES256Utils.encrypt(user.getRegNo()));
        User result = userRepository.save(user);

        new Thread(new Runnable() {
            public void run() {
                try {
                    scrapService.saveScrapInfo(user.getName(), AES256Utils.decrypt(user.getRegNo()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return ResponseEntity.created(new URI("/api/users" + result.getId())).body(result);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getUser() throws Exception {
        log.debug("REST request to get User");

        UserDTO userDTO = SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByUserId).map(UserDTO::new).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        userDTO.setRegNo(AES256Utils.decrypt(userDTO.getRegNo()));

        return ResponseEntity.ok(userDTO);
    }
}
