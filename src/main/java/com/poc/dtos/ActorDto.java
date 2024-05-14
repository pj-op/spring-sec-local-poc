package com.poc.dtos;

import java.util.Date;

public record ActorDto(Integer actorId, String firstName, String lastName, Date lastUpdate){}
