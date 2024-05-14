package com.poc.service;

import com.poc.dtos.ActorDto;
import com.poc.dtos.UserInfoDto;

import java.util.List;

public interface ActorService {
    List<ActorDto> getAllActors();

    ActorDto getActorDataFromId(int actorId);

    ActorDto updateActorDetails(int actorId);

    String addUserToDb(UserInfoDto userInfoDto);
}
