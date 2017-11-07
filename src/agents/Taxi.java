package agents;
import agents.Passenger.PassengerBehaviour;
import jade.core.*;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.DFService;
import jade.domain.FIPAException;

public class Taxi extends Agent{
	
	class TaxiBehaviour extends SimpleBehaviour {
		
		public TaxiBehaviour(Agent a){
			super(a);
		}

		@Override
		public void action() {
			ACLMessage msg = receive();
			if(msg.getPerformative() == ACLMessage.INFORM) {
				if(msg.getContent().equals("return location")){
					ACLMessage reply = msg.createReply();
					reply.setContent(Integer.toString(7) + ";" +  Integer.toString(4));
					send(reply);
				}
			}
			
		}

		@Override
		public boolean done() {
			// TODO Auto-generated method stub
			return false;
		}
		
	}
	
	protected void setup() {
		//Read arguments. If arguments are != 0 fails
		Object[] args = getArguments();
		if(args != null && args.length != 0) {
			System.out.println("Agent Passenger requieres one argument");
			return;
		}
        
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setName(getName());
		sd.setType("Taxi");
		dfd.addServices(sd);
		try{
			DFService.register(this, dfd);
		} catch(FIPAException e) {
			e.printStackTrace();
		}
		
        
        TaxiBehaviour b = new TaxiBehaviour(this);
		addBehaviour(b);
		
	}
	
}
