package com.astrolink.AstroLink.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

//     Methods
//    Dto name(SourceClass name);
}
