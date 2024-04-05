package com.companie.companyproject.service.client;

import com.companie.companyproject.dto.AdDTO;
import com.companie.companyproject.dto.AdDetailsForClientDTO;
import com.companie.companyproject.dto.ReservationDTO;
import com.companie.companyproject.dto.ReviewDTO;
import com.companie.companyproject.enums.ReservationStatus;
import com.companie.companyproject.enums.ReviewStatus;
import com.companie.companyproject.repository.ReviewRepo;
import com.companie.companyproject.model.Ad;
import com.companie.companyproject.model.Reservation;
import com.companie.companyproject.model.Review;
import com.companie.companyproject.model.User;
import com.companie.companyproject.repository.AdRepo;
import com.companie.companyproject.repository.ReservationRepo;
import com.companie.companyproject.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService{
  @Autowired
    private AdRepo adRepo;
  @Autowired
  private UserRepo userRepo;
  @Autowired
  private ReservationRepo reservationRepo;
  @Autowired
  private ReviewRepo reviewRepo;
    public List<AdDTO> getAllAds(){
        return adRepo.findAll().stream().map(Ad::getAdDTO).collect(Collectors.toList());
    }

    public List<AdDTO> searchAdByName(String name){
        return adRepo.findAllByServiceNameContaining(name).stream().map(Ad::getAdDTO).collect(Collectors.toList());
    }

    public boolean bookService(ReservationDTO reservationDTO){
        Optional<Ad> optionalAd = adRepo.findById(reservationDTO.getAdId());
        Optional<User> optionalUser = userRepo.findById(reservationDTO.getUserId());

        if(optionalAd.isPresent() && optionalUser.isPresent()){
            Reservation reservation = new Reservation();
            reservation.setBookDate(reservationDTO.getBookDate());
            reservation.setReservationStatus(ReservationStatus.PENDING);
            reservation.setUser(optionalUser.get());
            reservation.setAd(optionalAd.get());
            reservation.setCompany(optionalAd.get().getUser());
            reservation.setReviewStatus(ReviewStatus.FALSE);

            reservationRepo.save(reservation);
            return true;
        }
        return false;
    }

    public AdDetailsForClientDTO getAdDetailsByAdId(Long adId){
        Optional<Ad> optionalAd = adRepo.findById(adId);
        AdDetailsForClientDTO adDetailsForClientDTO = new AdDetailsForClientDTO();
        if(optionalAd.isPresent()){
            adDetailsForClientDTO.setAdDTO(optionalAd.get().getAdDTO());

            List<Review> reviewList = reviewRepo.findAllByAdId(adId);
            adDetailsForClientDTO.setReviewDTOList(reviewList.stream().map(Review::getDTO).collect(Collectors.toList()));
        }
        return adDetailsForClientDTO;
    }

    public List<ReservationDTO> getAllBookingsByUserId(Long userId){
        return reservationRepo.findAllByUserId(userId).stream().map(Reservation::getReservationDTO).collect(Collectors.toList());
    }

    public Boolean giveReview(ReviewDTO reviewDTO){
        Optional<User> optionalUser = userRepo.findById(reviewDTO.getUserId());
        Optional<Reservation> optionalBooking = reservationRepo.findById(reviewDTO.getBookId());

        if(optionalUser.isPresent() && optionalBooking.isPresent()){
            Review review = new Review();
            review.setReviewDate(new Date());
            review.setReview(reviewDTO.getReview());
            review.setRating(reviewDTO.getRating());

            review.setUser(optionalUser.get());
            review.setAd(optionalBooking.get().getAd());

            reviewRepo.save(review);

            Reservation booking = optionalBooking.get();
            booking.setReviewStatus(ReviewStatus.TRUE);

            reservationRepo.save(booking);
            return true;
        }
        return false;
    }
}
