package tools;

import jade.core.AID;

public class TaxiService {
	private PassengerInfo passenger_info;
	private AID passenger;
	private boolean is_in_car;
	
	public TaxiService(PassengerInfo info, AID passenger) {
		this.passenger_info = info;
		this.passenger = passenger;
		this.is_in_car = false;
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
	
	public void setInfo(PassengerInfo info){
		this.passenger_info = info;
	}
}
