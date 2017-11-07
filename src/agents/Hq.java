package agents;
import jade.core.*;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.DFService;
import jade.domain.FIPAException;


public class Hq extends Agent{
	
	
	class TaxiComBehaviour extends SimpleBehaviour{
		
		private Agent agent;
		public TaxiComBehaviour(Agent a){
			super(a);
			agent = a;
		}

		//TODO
		//receber mensagem de um passageiro com localizaçao atual e destino
		//comunica com todos os taxis pedindo o seu estado atual
		//escolhe o taxi mais apto para a tarefa e comunica lhe a order 
		@Override
		public void action() {
			//receber mensagem de Passenger com informação
			ACLMessage msg = blockingReceive();
			if(msg.getPerformative() == ACLMessage.INFORM) {
				System.out.println("From: " + msg.getSender().getName() + "\nTo: " + getLocalName() + "\nMessage: " + msg.getContent());
			}
			
			DFAgentDescription template = new DFAgentDescription();
	        ServiceDescription sd1 = new ServiceDescription();
	        sd1.setType("Taxi");
	        template.addServices(sd1);
	        
	        try {
	            DFAgentDescription[] result = DFService.search(agent, template);
	            ACLMessage msg_taxis = new ACLMessage(ACLMessage.INFORM);
	            for(int i=0; i<result.length; ++i)
	               msg.addReceiver(result[i].getName());
	            msg.setContent("return location");
	            send(msg);
	         } catch(FIPAException e) { e.printStackTrace(); }
			
		}

		@Override
		public boolean done() {
			return false;
		}
		
	}
	
	protected void setup() {
		//Read arguments. If arguments are != 0 fails
		Object[] args = getArguments();
		if(args != null && args.length != 0) {
			System.out.println("Agent HQ does not require arguments");
			return;
		}
	    
		//Asks DF if HQ agent exists
		DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd1 = new ServiceDescription();
        sd1.setType("HQ");
        template.addServices(sd1);
        
        //fail if HQ agent already exists
        try{
        	DFAgentDescription[] result = DFService.search(this, template);
        	if (result.length != 0){
        		System.out.println("HQ Agent already exists!");
        		takeDown();
        		return;
        	}
        }catch(FIPAException e) { 
        	e.printStackTrace(); 
        }
        
        
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setName(getName());
		sd.setType("HQ");
		dfd.addServices(sd);
		try{
			DFService.register(this, dfd);
		} catch(FIPAException e) {
			e.printStackTrace();
		}
		
		TaxiComBehaviour b = new TaxiComBehaviour(this);
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
