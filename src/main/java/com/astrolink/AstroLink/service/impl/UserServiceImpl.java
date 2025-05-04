package com.astrolink.AstroLink.service.impl;

import com.astrolink.AstroLink.dto.response.AcceptingAstrologersRequestDto;
import com.astrolink.AstroLink.dto.response.ConsultationResponseDto;
import com.astrolink.AstroLink.dto.response.DashboardDto;
import com.astrolink.AstroLink.entity.ConsultationRequest;
import com.astrolink.AstroLink.entity.User;
import com.astrolink.AstroLink.exception.custom.DataNotFoundException;
import com.astrolink.AstroLink.repository.ConsultationRequestRepository;
import com.astrolink.AstroLink.repository.UserRepository;
import com.astrolink.AstroLink.service.UserService;
import com.astrolink.AstroLink.dto.mapper.ConsultationRequestMapper;
import com.astrolink.AstroLink.util.FinderClassUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ConsultationRequestRepository consultationRequestRepository;
    private final ConsultationRequestMapper consultationRequestMapper;
    private final FinderClassUtil util;

    @Override
    public List<AcceptingAstrologersRequestDto> getAllPendingAcceptationRequests(UUID userId) {
        User user = util.findUserById(userId);

        return user.getAcceptedConsultationIds().stream()
                .map(consultationRequestRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .flatMap(consultationRequest ->
                        consultationRequest.getToAcceptAstrologerIds().stream()
                                .map(astrologer ->
                                        consultationRequestMapper.toAcceptingAstrologersDto(
                                                user,
                                                astrologer,
                                                consultationRequest
                                        )
                                )
                )
                .toList();
    }

    @Override
    public DashboardDto getDashboardData(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        List<ConsultationRequest> consultations = user.getAcceptedConsultationIds().stream()
                .map(consultationRequestRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        List<ConsultationResponseDto> activeConsultations =
                consultationRequestMapper.toDtoList(consultations);

        return new DashboardDto(activeConsultations,user.getActiveChatSessionIds().size());
    }
}
