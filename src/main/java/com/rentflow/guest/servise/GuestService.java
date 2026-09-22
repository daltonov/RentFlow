package com.rentflow.guest.service;

import com.rentflow.common.exception.NotFoundException;
import com.rentflow.guest.domain.Guest;
import com.rentflow.guest.dto.CreateGuestRequest;
import com.rentflow.guest.dto.GuestResponse;
import com.rentflow.guest.mapper.GuestMapper;
import com.rentflow.guest.repository.GuestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class GuestService {

    private final GuestRepository guestRepository;
    private final GuestMapper guestMapper;

    public GuestService(GuestRepository guestRepository,
                        GuestMapper guestMapper) {
        this.guestRepository = guestRepository;
        this.guestMapper = guestMapper;
    }

    public List<GuestResponse> getAll() {
        return guestRepository.findAll()
                .stream()
                .map(guestMapper::toResponse)
                .toList();
    }

    public GuestResponse getById(UUID id) {
        Guest guest = guestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Guest", id));

        return guestMapper.toResponse(guest);
    }

    @Transactional
    public GuestResponse create(CreateGuestRequest request) {
        Guest guest = guestMapper.toEntity(request);
        Guest savedGuest = guestRepository.save(guest);

        return guestMapper.toResponse(savedGuest);
    }
}