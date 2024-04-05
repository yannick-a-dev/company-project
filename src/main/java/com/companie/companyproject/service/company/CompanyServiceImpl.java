package com.companie.companyproject.service.company;

import com.companie.companyproject.dto.AdDTO;
import com.companie.companyproject.dto.ReservationDTO;
import com.companie.companyproject.enums.ReservationStatus;
import com.companie.companyproject.model.Ad;
import com.companie.companyproject.model.Reservation;
import com.companie.companyproject.model.User;
import com.companie.companyproject.repository.AdRepo;
import com.companie.companyproject.repository.ReservationRepo;
import com.companie.companyproject.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService{
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private AdRepo adRepo;

    @Autowired
    private ReservationRepo reservationRepo;

    public boolean postAd(Long userId, AdDTO adDTO) throws IOException {
        Optional<User> optionalUser = userRepo.findById(userId);
        if(optionalUser.isPresent() && adDTO.getImg() != null){
            Ad ad = new Ad();
            ad.setServiceName(adDTO.getServiceName());
            ad.setDescription(adDTO.getDescription());
            ad.setImg(adDTO.getImg().getBytes());
            ad.setPrice(adDTO.getPrice());
            ad.setUser(optionalUser.get());

            adRepo.save(ad);
            return true;
        }
        return false;
    }

    public List<AdDTO> getAllAds(Long userId) {
        return adRepo.findAllByUserId(userId)
                .stream()
                .map(Ad::getAdDTO)
                .collect(Collectors.toList());
    }

    public AdDTO getAdById(Long adId){
        Optional<Ad> optionalAd = adRepo.findById(adId);
        if(optionalAd.isPresent()){
            return optionalAd.get().getAdDTO();
        }
        return null;
    }

    public boolean updateAd(Long adId, AdDTO adDTO) throws IOException {
        Optional<Ad> optionalAd = adRepo.findById(adId);
        if(optionalAd.isPresent()){
            Ad ad = optionalAd.get();

            ad.setServiceName(adDTO.getServiceName());
            ad.setDescription(adDTO.getDescription());
            ad.setPrice(adDTO.getPrice());

            if(adDTO.getImg() != null){
                ad.setImg(adDTO.getImg().getBytes());
            }

            adRepo.save(ad);
            return true;
        }else{
           return false;
        }
    }

    public boolean deleteAd(Long adId){
        Optional<Ad> optionalAd = adRepo.findById(adId);
        if(optionalAd.isPresent()){
            adRepo.delete(optionalAd.get());
            return true;
        }
        return false;
    }

    public List<ReservationDTO> getAllAdBookings(Long companyId){
        return reservationRepo.findAllByCompanyId(companyId).stream().map(Reservation::getReservationDTO).collect(Collectors.toList());
    }

    public boolean changeBookingStatus(Long bookingId, String status){
        Optional<Reservation> optionalReservation = reservationRepo.findById(bookingId);
        if(optionalReservation.isPresent()){
            Reservation existingReservation = optionalReservation.get();
            if(Objects.equals(status,"Approve")){
                existingReservation.setReservationStatus(ReservationStatus.APPROVED);
            }else{
                existingReservation.setReservationStatus(ReservationStatus.REJECTED);
            }
            reservationRepo.save(existingReservation);
            return true;
        }
        return false;
    }
}
