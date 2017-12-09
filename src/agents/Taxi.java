package agents;
import java.util.ArrayList;

import behaviours.HqTaxiContractNetResponderBehaviour;
import behaviours.TaxiServiceBehaviour;
import gui.Gui;
import jade.core.*;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import tools.TaxiService;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;

public class Taxi extends Agent{
	private int x, y, capacity;
	private ArrayList<TaxiService> services = new ArrayList<>();
	
	protected void setup(){
		Object[] args = getArguments();
		if(args.length != 4){
			System.out.println("Agent Taxi requieres four argument");
			return;
		}
		
		this.capacity = Integer.parseInt((String) args[1]);
		this.x = Integer.parseInt((String) args[2]);
		this.y = Integer.parseInt((String) args[3]);
		
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setName(getName());
		sd.setType("TAXI");
		dfd.addServices(sd);
		try{
			DFService.register(this, dfd);
		} catch(FIPAException e) {
			e.printStackTrace();
		}
		
		Gui.newTaxi(x, y, this.getLocalName());
		MessageTemplate template = MessageTemplate.and(
				MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET),
				MessageTemplate.MatchPerformative(ACLMessage.CFP) );
		
		boolean share_on = Boolean.parseBoolean((String) args[0]);
		
		this.addBehaviour(new HqTaxiContractNetResponderBehaviour(this, template, share_on));
		this.addBehaviour(new TaxiServiceBehaviour(this));
		System.out.println("Created agent: " + this.getName());
	}
	
	protected void takeDown(){
		try{
			DFService.deregister(this);
		}catch(FIPAException e){
			e.printStackTrace();
		}
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	public void addService(TaxiService service){
		this.services.add(service);
	}
	
	public void removeService(TaxiService service){
		this.services.remove(service);
	}
	
	public TaxiService getService(int index){
		return this.services.get(index);
	}
	
	public ArrayList<TaxiService> getServices(){
		return this.services;
	}
}
