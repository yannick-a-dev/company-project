package com.companie.companyproject.service.company;

import com.companie.companyproject.dto.AdDTO;
import com.companie.companyproject.dto.ReservationDTO;

import java.io.IOException;
import java.util.List;

public interface CompanyService {
    public boolean postAd(Long userId, AdDTO adDTO) throws IOException;
    public List<AdDTO> getAllAds(Long userId);
    public AdDTO getAdById(Long adId);
    public boolean updateAd(Long adId, AdDTO adDTO) throws IOException;
    public boolean deleteAd(Long adId);
    public List<ReservationDTO> getAllAdBookings(Long companyId);
//    void createReservation(ReservationDTO reservationDTO);
    public boolean changeBookingStatus(Long bookingId, String status);
}
