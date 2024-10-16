package com.ufpb.sicred.utils;

import org.modelmapper.ModelMapper;

public class Mapper{

    private static final ModelMapper mapper = new ModelMapper();

    public static <Entity, Dto> Dto convertToDto(Entity entity, Class<Dto> dtoClass){

        return mapper.map(entity,dtoClass);
    }

    public static <Entity, Dto> Entity convertToEntity(Dto dto, Class<Entity> entityClass){

        return mapper.map(dto, entityClass);
    }

}
