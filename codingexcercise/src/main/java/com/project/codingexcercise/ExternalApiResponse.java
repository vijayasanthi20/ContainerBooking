package com.project.codingexcercise;



import com.fasterxml.jackson.annotation.JsonProperty;


public class ExternalApiResponse {
	

	@JsonProperty("availableSpace")
     private int availableSpace;
	
	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	@JsonProperty("available")
     private boolean available;
	


 public int getAvailableSpace() {
	return availableSpace;
}

public void setAvailableSpace(int availableSpace) {
	 this.availableSpace = availableSpace;
}

	 
	 



}
