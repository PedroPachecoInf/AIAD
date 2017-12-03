package agents;
import behaviours.HqReceivePassengerBehaviour;
import jade.core.*;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.DFService;
import jade.domain.FIPAException;


public class Hq extends Agent{
	
	protected void setup(){
		Object[] args = getArguments();
		if(args.length != 0){
			System.out.println("Hq nao necessita de argumentos");
			return;
		}
		
		// Asks DF if HQ agent exists
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd1 = new ServiceDescription();
		sd1.setType("HQ");
		template.addServices(sd1);

		// fail if HQ agent already exists
		try {
			DFAgentDescription[] result = DFService.search(this, template);
			if (result.length != 0) {
				System.out.println("HQ Agent already exists!");
				return;
			}
		} catch (FIPAException e) {
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
	    
	    System.out.println("Created agent: " + this.getName());
	    
	    this.addBehaviour(new HqReceivePassengerBehaviour(this));
	}
	
	protected void takeDown(){
		try {
            DFService.deregister(this);
        } catch(FIPAException e) {
            e.printStackTrace();
        }
	}
	
}
