package com.companie.companyproject.service.client;

import com.companie.companyproject.dto.AdDTO;
import com.companie.companyproject.dto.AdDetailsForClientDTO;
import com.companie.companyproject.dto.ReservationDTO;
import com.companie.companyproject.dto.ReviewDTO;

import java.util.List;

public interface ClientService {
    public List<AdDTO> getAllAds();
    public List<AdDTO> searchAdByName(String name);
    public boolean bookService(ReservationDTO reservationDTO);
    public AdDetailsForClientDTO getAdDetailsByAdId(Long adId);
    public List<ReservationDTO> getAllBookingsByUserId(Long userId);
    public Boolean giveReview(ReviewDTO reviewDTO);
}
