

package com.project.codingexcercise.controller;

import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import org.slf4j.LoggerFactory;
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


/**
 * @author vijayasanthi
 *
 */
@RestController
@Validated
@RequestMapping("/api/bookings")
public class ContainerBookingController {

	 Logger logger = (Logger) LoggerFactory.getLogger(ContainerBookingController.class);

	@Autowired
	private BookingsRepository bookingRepository;
	WebClient webClient = WebClient.create();

//This API to call external API and check for availability
	 @PostMapping("/checkAvailablity")
	 
	    public Mono<Object> check(@Valid @RequestBody Bookings bookings) {
		 String methodName="checkAvailablity";
		logger.info("calling method "+methodName);
		return callexternalAPI();
		     
	 }
	 
	
	 /**
	  * @Object
	 * @callexternalAPI
	 */
	Mono<Object>  callexternalAPI() {
		
		String methodName="callexternalAPI";
		 logger.info("calling method "+methodName);
		 
		 HttpClient httpClient = HttpClient.create().baseUrl("https://maersk.com/");
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

 
	 /**
	 * @GetMapping("/all")
	 */
	@GetMapping("/all")
	  public Flux<Bookings> getAllbookingIds() {
		return bookingRepository.findAll();
	    
	  }
	
	 /**
	 * @PostMapping("/createBooking")
	 */
	    
	//This call is to save booking detail and to return booking reference to customer	  
	 
	 @PostMapping("/createBooking")
	    public Mono<Object> createUser(@Valid @RequestBody Bookings bookings) {
		 
		 String methodName="checkAvailablity";
		logger.info("calling method "+methodName);
		
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





