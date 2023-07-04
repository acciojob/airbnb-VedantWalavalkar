package com.driver;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Repository
public class HotelManagementRepository {
    Map<String, Hotel> hotelMap = new TreeMap<>();
    Map<Integer, User> userMap = new HashMap<>();
    Map<String, Booking> bookingMap = new HashMap<>();
    public String addHotel(Hotel hotel) {
        String hotelName = hotel.getHotelName();
        if(hotelMap.containsKey(hotelName)){
            return "FAILURE";
        }
        hotelMap.put(hotelName, hotel);
        return "SUCCESS";
    }

    public Integer addUser(User user) {
        userMap.put(user.getaadharCardNo(), user);
        return user.getaadharCardNo();
    }

    public String getHotelWithMostFacilities() {
        int numOfFacilities = 0;
        String name = "";
        for(String hotelName : hotelMap.keySet()){
            Hotel hotel = hotelMap.get(hotelName);
            List<Facility> list = hotel.getFacilities();
            if(list == null)
                continue;

            if(hotel.getFacilities().size() > numOfFacilities){
                name = hotelName;
                numOfFacilities = hotel.getFacilities().size();
            }
        }
        return name;
    }

    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName) {
        Hotel hotel = hotelMap.get(hotelName);
        List<Facility> oldFacilities = hotel.getFacilities();
        for(Facility facility : newFacilities){
            boolean isThere = false;
            System.out.println(facility);
            for(Facility oldFacility : oldFacilities){
//                System.out.print(oldFacility+ " ");
                if(facility == oldFacility)
                    isThere = true;
            }
            if(!isThere){
                oldFacilities.add(facility);
            }
        }
        hotel.setFacilities(oldFacilities);
//        System.out.println(oldFacilities);
        return hotel;
    }

    public int bookARoom(Booking booking) {
        String hotelName = booking.getHotelName();
        Hotel hotel = hotelMap.get(hotelName);
        int availableRooms = hotel.getAvailableRooms();
        int noOfRooms = booking.getNoOfRooms();
        if(availableRooms < noOfRooms)
            return -1;
        int price = hotel.getPricePerNight();
        int amountToPay = price * noOfRooms;
        booking.setAmountToBePaid(amountToPay);
        String bookingId = booking.getBookingId();
        bookingMap.put(bookingId, booking);
        return amountToPay;
    }

    public int getBookings(Integer aadharCard) {
        int noOfBookings = 0;
        for(String bookingId : bookingMap.keySet()){
            Booking booking = bookingMap.get(bookingId);
            int aadharNo = booking.getBookingAadharCard();
            if(aadharNo == aadharCard)
                noOfBookings++;
        }
        return noOfBookings;
    }
}
