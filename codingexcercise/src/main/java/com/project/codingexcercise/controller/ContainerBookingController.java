package com.project.codingexcercise.controller;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.datastax.oss.driver.api.core.CqlSession;
import com.project.codingexcercise.BookingObjectResponseDTO;
import com.project.codingexcercise.ExternalApiResponse;
import com.project.codingexcercise.MaxValueFinder;
import com.project.codingexcercise.model.Bookings;
import com.project.codingexcercise.repository.BookingsRepository;


import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;


@RestController
@Validated
@RequestMapping("/api/bookings")
public class ContainerBookingController {



	@Autowired
	private BookingsRepository bookingRepository;
	WebClient webClient = WebClient.create();

	// @PostMapping("/checkAvailablity1")
	 @PostMapping("/checkAvailablity")
	    public Mono<Object> check(@Valid @RequestBody Bookings bookings) {
		 
		return callexternalAPI();
		     
	 }
	 
	
	 Mono<Object>  callexternalAPI() {
	HttpClient httpClient = HttpClient.create()
		   
		    .baseUrl("https://maersk.com/");

		WebClient webClient = WebClient.builder()
		    .clientConnector(new ReactorClientHttpConnector(httpClient))
		    .baseUrl("https://maersk.com")
		    .build();

		Mono<Object> responseMono = webClient.get()
		    .uri("/checkAvailablity")
		    .exchangeToMono(response -> {
		        
		        	ExternalApiResponse dto = new ExternalApiResponse();
		        	 dto.setAvailableSpace(6);
		        	 if(dto.getAvailableSpace() == 0) {
			     			dto.setAvailable(false);
			     			return Mono.just(dto);
			     			
			     		}else {
			     			
			     			dto.setAvailable(true);
			     			return Mono.just(dto);
			     		}
		        
		    });
		return responseMono;

	 }

 
	 @GetMapping("/all")
	  public Flux<Bookings> getAllbookingIds() {
		return bookingRepository.findAll();
	    
	  }
	    
	//This call is to save booking detail and to return booking reference to customer	  
	 
	 @PostMapping("/createBooking")
	    public Mono<Object> createUser(@Valid @RequestBody Bookings bookings) {
		 AtomicLong nextSerialNumber;
 		  Long defaultRefNUmber=957000000L;
 		  
 	    	 CqlSession session = CqlSession.builder().build();
 			 MaxValueFinder maxValueFinder = new MaxValueFinder(session);		 
 			 Long maxValue = maxValueFinder.findMaxValue("my_project.bookings", "id").block();
 			 System.out.println("maxValue "+maxValue);
 			 if(maxValue==0) {
 				 nextSerialNumber = new AtomicLong(defaultRefNUmber);
 			 }else {
 				 
 				nextSerialNumber = new AtomicLong(maxValue);
 			 }
 			 
 			 long bookingrefid= nextSerialNumber.incrementAndGet();

 			return  bookingRepository.save(new Bookings(bookingrefid, bookings.getContainerSize(),bookings.getContainerType(),
     		bookings.getOrigin(),bookings.getDestination(),bookings.getQuantity(),bookings.getTimestamp())).
 					map(obj -> {
     			BookingObjectResponseDTO dto = new BookingObjectResponseDTO();
     			dto.setBookingRef(obj.getId().toString());
     			
     			return dto;
     		});

	    }
	 
	
}





