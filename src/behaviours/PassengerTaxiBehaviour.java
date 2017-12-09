package behaviours;

import agents.Passenger;
import agents.Taxi;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class PassengerTaxiBehaviour extends OneShotBehaviour {
	private Passenger passenger;
	
	public PassengerTaxiBehaviour(Passenger passenger){
		this.passenger = passenger;
	}

	public void action() {
		MessageTemplate template_request = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
														MessageTemplate.MatchProtocol("TaxiPassengerProtocol"));
		
		MessageTemplate template_query = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.QUERY_IF),
				MessageTemplate.MatchProtocol("TaxiPassengerProtocol"));
		System.out.println("inside passengerTaxiBehaviour");
		ACLMessage arrive = this.passenger.blockingReceive(template_request);
		ACLMessage arrive_reply = arrive.createReply();
		if(arrive.getSender().getLocalName().equals(passenger.getMyTaxyName()) && !taxiArrived(arrive.getContent())){
			System.out.println(this.passenger.getLocalName() + ": " + arrive.getSender().getLocalName() + " has arrived at my location");
			arrive_reply.setPerformative(ACLMessage.AGREE);
			this.passenger.send(arrive_reply);
		}
		else{
			arrive_reply.setPerformative(ACLMessage.REFUSE);
			arrive_reply.setContent(this.passenger.getInfo().toString());
			this.passenger.send(arrive_reply);
			this.reset();
		}
		
		boolean arrived_at_location = false;
		while(!arrived_at_location){
			ACLMessage current_location = this.passenger.blockingReceive(template_query);
			ACLMessage location_reply = current_location.createReply();
			if(arrivedFinalLocation(current_location.getContent())){
				location_reply.setPerformative(ACLMessage.AGREE);
				arrived_at_location = true;
			}else{
				location_reply.setPerformative(ACLMessage.REFUSE);
			}
			this.passenger.send(location_reply);
		}
		
		System.out.println(this.passenger.getLocalName() + ": I Arrived at my location! The trip costed me: " + this.passenger.getCost());
		
	}
	
	public boolean arrivedFinalLocation(String content){
		String[] content_parts = content.split("-");
		int taxi_x = Integer.parseInt(content_parts[0]);
		int taxi_y = Integer.parseInt(content_parts[1]);
		double cost = Double.parseDouble(content_parts[2]);
		this.passenger.addCost(cost);
		
		return taxi_x == this.passenger.getInfo().getFinal_x() && taxi_y == this.passenger.getInfo().getFinal_y();
	}
	
	public boolean taxiArrived(String content){
		String[] content_parts = content.split("-");
		int taxi_x = Integer.parseInt(content_parts[0]);
		int taxi_y = Integer.parseInt(content_parts[1]);
		String request = content_parts[2];
		
		return request.equals("GetIn") && taxi_x == this.passenger.getInfo().getInitial_x() && taxi_y == this.passenger.getInfo().getInitial_y();
	}

}
