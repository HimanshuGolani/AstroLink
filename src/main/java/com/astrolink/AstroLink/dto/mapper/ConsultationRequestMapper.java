package com.astrolink.AstroLink.dto.mapper;

import com.astrolink.AstroLink.dto.request.ConsultationRequestCreateDto;
import com.astrolink.AstroLink.dto.response.ConsultationRequestDto;
import com.astrolink.AstroLink.entity.ConsultationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ConsultationRequestMapper {

    @Mapping(target = "acceptingAstrologersCount", expression = "java(consultationRequest.getAcceptingAstrologersId().size())")
    ConsultationRequestDto toDto(ConsultationRequest consultationRequest);

    List<ConsultationRequestDto> toDtoList(List<ConsultationRequest> consultationRequests);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "paymentStatus", ignore = true)
    @Mapping(target = "acceptingAstrologersId", ignore = true)
    @Mapping(target = "openForAll", ignore = true)  // Changed from isOpenForAll to openForAll
    @Mapping(target = "createdAt", ignore = true)
    ConsultationRequest toEntity(ConsultationRequestCreateDto createDto);
}