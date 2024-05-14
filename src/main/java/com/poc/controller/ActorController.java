package com.poc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.databind.util.JSONWrappedObject;
import com.poc.dtos.ActorDto;
import com.poc.dtos.AuthUserDto;
import com.poc.dtos.UserInfoDto;
import com.poc.service.ActorService;
import com.poc.service.impl.JwtService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class ActorController {

    private static final String FORMAT_FULL = "full";
    private static final String FORMAT_SHORT = "short";
    private static final String STATUS_OK = "OK";
    private static final String STATUS = "status";
    private static final String CURRENT_TIME = "currentTime";

    @Autowired
    private ActorService actorServiceImpl;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "spring-sec-latest-poc")
    @GetMapping(value = "/actor", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ActorDto>> queryAllActors() {
        return ResponseEntity.ok(actorServiceImpl.getAllActors());
    }

    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "spring-sec-latest-poc")
    @GetMapping(value = "/actor/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ActorDto> queryActorById(@PathVariable(name = "id") Integer id) {
        return ResponseEntity.ok(actorServiceImpl.getActorDataFromId(id));
    }

    @GetMapping(value = "/welcome", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> greetPerson() {
        return ResponseEntity.ok("Welcome to the Application!!!");
    }

    @PostMapping(value = "/add-user", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addingUserToLogin(@RequestBody UserInfoDto userInfoDto) {
        if (!StringUtils.hasLength(actorServiceImpl.addUserToDb(userInfoDto))) {
            return ResponseEntity.status(HttpStatus.OK).body("SUCCESS");
        } else return ResponseEntity.badRequest().body("Facing issue when persisting user" + userInfoDto.userName());
    }

    @PostMapping(value = "/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> authenticateUser(@RequestBody AuthUserDto authUserDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authUserDto.username(), authUserDto.password()));
        if (authentication.isAuthenticated()) {
            log.info("User is authenticated: {}", authUserDto.username());
            return ResponseEntity.ok(JwtService.getGeneratedToken(authUserDto.username()));
        } else {
            return ResponseEntity.badRequest().body("User is either not authenticated or invalid.");
        }
    }

    @GetMapping(value = "/healthcheck", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> healthcheck(@RequestParam("format") String format) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        ObjectNode objectNode = mapper.createObjectNode();

        if (FORMAT_FULL.equals(format)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
            objectNode.put(CURRENT_TIME, ZonedDateTime.now(ZoneId.of("Europe/Warsaw")).format(formatter));
            objectNode.put(STATUS, STATUS_OK);
        } else if (FORMAT_SHORT.equals(format)) {
            objectNode.put(STATUS, STATUS_OK);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
        }
        return ResponseEntity.ok(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectNode));
    }
}
