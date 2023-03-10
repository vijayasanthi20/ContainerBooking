
package com.project.codingexcercise.model;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * @author vijayasanthi
 *
 */
@Table
public class Bookings {

	@PrimaryKey
	 private Long id;

	@NotNull
	private int container_size ; 
	
	@NotNull
	@Size(max=20,min=5,message="origin value must be – min 5, max 20")
	
	private String	origin=null;
	@NotNull
	@Size(max=20,min=5,message="destination value must be – min 5, max 20")
	private String destination =null;
	@NotNull
	@Min(value=1)
	@Max(value=100)
	private int quantity ;
	@NotNull
	private String timestamp;
	@NotNull
	private ContainerType container_type;
	
	
	
	


	public ContainerType getContainerType() {
		return container_type;
	}
	public Bookings(Long id, int container_size, ContainerType container_type, String origin, String destination, int quantity,
			String timestamp ) {
		
		super();
		this.id = id;
		this.container_size = container_size;
		
		this.origin = origin;
		this.destination = destination;
		this.quantity = quantity;
		this.timestamp = timestamp;
		this.container_type = container_type;
	}
	public void setContainerType(ContainerType containerType) {
		this.container_type = containerType;
	}
	public int getContainerSize() {
		return container_size;
	}
	public void setContainerSize(int container_size) {
		this.container_size = container_size;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}

	public enum ContainerType { DRY, REEFER};

}
