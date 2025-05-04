package com.astrolink.AstroLink.service;

import com.astrolink.AstroLink.dto.response.AcceptingAstrologersRequestDto;
import com.astrolink.AstroLink.dto.response.DashboardDto;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<AcceptingAstrologersRequestDto> getAllPendingAcceptationRequests(UUID userId);
    DashboardDto getDashboardData(UUID userId);
}
