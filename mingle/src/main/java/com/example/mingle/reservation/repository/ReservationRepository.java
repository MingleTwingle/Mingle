package com.example.mingle.reservation.repository;

import com.example.mingle.accommodation.domain.AccommodationRoom;
import com.example.mingle.reservation.domain.Reservation;
import com.example.mingle.restaurant.domain.Restaurant;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @EntityGraph(attributePaths = {"restaurant", "accommodationRoom"})
    List<Reservation> findByGuest_Id(Long guestId);

    @EntityGraph(attributePaths = {"restaurant", "accommodationRoom"})
    List<Reservation> findByHost_Id(Long hostId);

    @Query("SELECT r.restaurantName FROM Reservation res JOIN res.restaurant r WHERE res.id = :reservationId")
    String findRestaurantNameByReservationId(@Param("reservationId") Long reservationId);

    @Query("SELECT r FROM Reservation r WHERE r.accommodationRoom = :accommodationRoom AND r.date = :date")
    List<Reservation> findByAccommodationRoomAndDate(AccommodationRoom accommodationRoom, String date);

    List<Reservation> findByAccommodationRoom(AccommodationRoom accommodationRoom);

    List<Reservation> findByRestaurant(Restaurant restaurant);

}
