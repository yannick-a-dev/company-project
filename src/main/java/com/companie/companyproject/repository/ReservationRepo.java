package com.companie.companyproject.repository;

import com.companie.companyproject.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ReservationRepo extends JpaRepository<Reservation,Long> {
    List<Reservation> findAllByCompanyId(Long companyId);
    List<Reservation> findAllByUserId(Long userId);
}
