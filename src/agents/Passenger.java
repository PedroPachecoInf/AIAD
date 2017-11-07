package agents;
import jade.core.*;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.DFService;
import jade.domain.FIPAException;


public class Passenger extends Agent{
	
	class PassengerBehaviour extends SimpleBehaviour{
		private Boolean done = false;
		
		public PassengerBehaviour(Agent a){
			super(a);
		}

		@Override
		public void action() {
			ACLMessage msg = blockingReceive();
			if(msg.getPerformative() == ACLMessage.INFORM) {
				System.out.println("From: " + msg.getSender().getName() + "\nTo: " + getLocalName() + "\nMessage: " + msg.getContent());
			}
		}

		@Override
		public boolean done() {
			return done;
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
		sd.setType("Passenger");
		dfd.addServices(sd);
		try{
			DFService.register(this, dfd);
		} catch(FIPAException e) {
			e.printStackTrace();
		}
		
		DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd1 = new ServiceDescription();
        sd1.setType("HQ");
        template.addServices(sd1);
        
        try {
            DFAgentDescription[] result = DFService.search(this, template);
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            for(int i=0; i<result.length; ++i)
               msg.addReceiver(result[i].getName());
            msg.setContent(Integer.toString(1) + ";" + Integer.toString(2) + ";" +
            				Integer.toString(3) + ";" + Integer.toString(4));
            send(msg);
         } catch(FIPAException e) { e.printStackTrace(); }
        
        PassengerBehaviour b = new PassengerBehaviour(this);
		addBehaviour(b);
		
	}
	
	protected void takeDown() {
		try {
            DFService.deregister(this);
        } catch(FIPAException e) {
            e.printStackTrace();
        }
	}
	
}




