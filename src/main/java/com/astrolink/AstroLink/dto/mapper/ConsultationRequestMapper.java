package com.astrolink.AstroLink.dto.mapper;

import com.astrolink.AstroLink.dto.request.ConsultationRequestCreateDto;
import com.astrolink.AstroLink.dto.response.AcceptingAstrologersRequestDto;
import com.astrolink.AstroLink.dto.response.ConsultationResponseDto;
import com.astrolink.AstroLink.entity.ConsultationRequest;
import com.astrolink.AstroLink.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ConsultationRequestMapper {

    @Mapping(target = "acceptingAstrologersCount", expression = "java(consultationRequest.getAcceptingAstrologersId().size())")
    ConsultationResponseDto toDto(ConsultationRequest consultationRequest);

    List<ConsultationResponseDto> toDtoList(List<ConsultationRequest> consultationRequests);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "paymentStatus", ignore = true)
    @Mapping(target = "acceptingAstrologersId", ignore = true)
    @Mapping(target = "openForAll", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    ConsultationRequest toEntity(ConsultationRequestCreateDto createDto);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "astrologer.id", target = "astrologerId")
    @Mapping(source = "astrologer.rating", target = "astrologerRating")
    @Mapping(source = "consultationRequest", target = "consultationRequest")
    AcceptingAstrologersRequestDto toAcceptingAstrologersDto(User user, User astrologer, ConsultationRequest consultationRequest);
}
