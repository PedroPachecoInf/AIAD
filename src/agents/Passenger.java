package agents;
import behaviours.PassengerSendHqBehaviour;
import behaviours.PassengerTaxiBehaviour;
import jade.core.*;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import tools.PassengerInfo;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.DFService;
import jade.domain.FIPAException;


public class Passenger extends Agent{
	private PassengerInfo info;
	private String my_taxi_name;
	
	protected void setup(){
		Object[] args = getArguments();
		if(args.length != 4){
			System.out.println("Agent Passenger requieres four argument");
			return;
		}
		
		int initial_x = Integer.parseInt((String) args[0]);
		int initial_y = Integer.parseInt((String) args[1]);
		int final_x = Integer.parseInt((String) args[2]);
		int final_y = Integer.parseInt((String) args[3]);
		
		info = new PassengerInfo(initial_x, initial_y, final_x, final_y, this.getLocalName());
		
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setName(getName());
		sd.setType("PASSENGER");
		dfd.addServices(sd);
		try{
			DFService.register(this, dfd);
		} catch(FIPAException e) {
			e.printStackTrace();
		}
		
		System.out.println("Created agent: " + this.getName());
		
		this.addBehaviour(new PassengerSendHqBehaviour(this));
		this.addBehaviour(new PassengerTaxiBehaviour(this));
	}
	
	protected void takeDown(){
		try{
			DFService.deregister(this);
		}catch(FIPAException e){
			e.printStackTrace();
		}
	}
	
	public PassengerInfo getInfo(){
		return this.info;
	}
	
	public void setMyTaxyName(String taxi_name){
		this.my_taxi_name = taxi_name;
	}
	

	public String getMyTaxyName(){
		return this.my_taxi_name;
	}
}




