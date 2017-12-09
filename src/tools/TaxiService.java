package tools;

import jade.core.AID;

public class TaxiService {
	private PassengerInfo passenger_info;
	private AID passenger;
	private boolean is_in_car;
	private boolean completed;
	private TaxiService share_service;
	
	public TaxiService(PassengerInfo info, AID passenger) {
		this.passenger_info = info;
		this.passenger = passenger;
		this.is_in_car = false;
		this.completed = false;
	}
	
	public PassengerInfo getInfo(){
		return this.passenger_info;
	}
	
	public AID getPassenger() {
		return this.passenger;
	}
	
	public boolean isInCar() {
		return this.is_in_car;
	}
	
	public void setIsInCar(boolean bool){
		this.is_in_car = bool;
	}
	
	public boolean isCompleted() {
		return this.completed;
	}
	
	public void setCompleted(boolean bool){
		this.completed = bool;
	}
	
	public void setInfo(PassengerInfo info){
		this.passenger_info = info;
	}
	
	public void setShareService(TaxiService share_service){
		this.share_service = share_service;
	}
	
	public TaxiService getShareService(){
		return this.share_service;
	}
}
