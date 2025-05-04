package com.astrolink.AstroLink.dto.mapper;

import com.astrolink.AstroLink.dto.response.AcceptingAstrologersRequestDto;
import com.astrolink.AstroLink.dto.response.ConsultationResponseDto;
import com.astrolink.AstroLink.entity.ConsultationRequest;
import com.astrolink.AstroLink.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

//    @Mapping(target = "consultationRequest", expression = "java(toConsultationDto(consultationRequest))")
//    @Mapping(target = "userId", source = "creator.id")
//    @Mapping(target = "astrologerId", source = "consultationRequest.acceptedAstrologerId")
//    @Mapping(target = "astrologerRating", source = "creator.rating")
//    AcceptingAstrologersRequestDto toAcceptingAstrologerDto(User creator, ConsultationRequest consultationRequest);

    ConsultationResponseDto toConsultationDto(ConsultationRequest consultationRequest);
}