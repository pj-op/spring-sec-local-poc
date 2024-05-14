package com.poc.service.impl;

import com.poc.customexception.UserAlreadyExistException;
import com.poc.dtos.ActorDto;
import com.poc.dtos.UserInfoDto;
import com.poc.entities.Actor;
import com.poc.entities.UserInformation;
import com.poc.repo.ActorRepo;
import com.poc.repo.UserInfoRepo;
import com.poc.service.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ActorServiceImpl implements ActorService {

    @Autowired
    private ActorRepo actorRepo;

    @Autowired
    private UserInfoRepo userInfoRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<ActorDto> getAllActors() {
        return convertEntityToDto(actorRepo.findAll());
    }

    @Override
    public ActorDto getActorDataFromId(int actorId) {
        return convertEntityToDto(actorRepo.getActorByActorId(actorId));
    }

    @Override
    public ActorDto updateActorDetails(int actorId) {
        return null;
    }

    @Override
    public String addUserToDb(UserInfoDto userInfoDto) {
        //Checking the existing user by email
        Optional<UserInformation> user = userInfoRepo.findByEmail(userInfoDto.emailId());
        if (user.isPresent()) throw new UserAlreadyExistException("supplied user already exists");
        UserInformation userInformation = convertDtoToEntity(userInfoDto);
        userInfoRepo.save(userInformation);
        return "";
    }

    private UserInformation convertDtoToEntity(UserInfoDto userInfoDto){
        return new UserInformation(userInfoDto.userName(), passwordEncoder.encode(userInfoDto.password()), userInfoDto.emailId(), userInfoDto.roles());
    }

    private ActorDto convertEntityToDto(Actor actor) {
        return new ActorDto(actor.getActorId(), actor.getFirstName(), actor.getLastName(), actor.getLastUpdate());
    }

    private List<ActorDto> convertEntityToDto(List<Actor> actors) {
        return actors.stream().map(actor -> new ActorDto(actor.getActorId(), actor.getFirstName(), actor.getLastName(), actor.getLastUpdate())).toList();
    }

}
