package com.project.codingexcercise;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * @author vijayasanthi
 *
 */

public class BookingObjectResponseDTO {

	 @JsonProperty("bookingRef")
     private String bookingRef;
	 


	public String getBookingRef() {
		return bookingRef;
	}

	public void setBookingRef(String bookingRef) {
		this.bookingRef = bookingRef;
	}

	
	
     
}
