package behaviours;

import java.sql.Date;
import java.util.ArrayList;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class HqReceivePassengerBehaviour extends SimpleBehaviour{
	
	public HqReceivePassengerBehaviour(Agent a){
		super(a);
	}
	
	public void action(){
		MessageTemplate template = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
														MessageTemplate.MatchProtocol("PassengerHqProtocol"));
		ACLMessage passenger_msg = this.getAgent().receive(template);
		if(passenger_msg == null)
			return;
		System.out.println(passenger_msg.getSender().getLocalName() + ": " + passenger_msg.getContent());
		
		Behaviour contract_initiator = new HqTaxiContractNetInitiatorBehaviour(this.getAgent(), buildCFPMessage(passenger_msg.getContent()), passenger_msg);
		this.getAgent().addBehaviour(contract_initiator);
	}
	
	public boolean done(){
		return false;
	}
	
	private ACLMessage buildCFPMessage(String content){
		ACLMessage cfp_msg = new ACLMessage(ACLMessage.CFP);
		addReceivers(cfp_msg, getTaxis());
		cfp_msg.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
		cfp_msg.setReplyByDate(new Date(System.currentTimeMillis() + 5000));
		cfp_msg.setContent(content);
		return cfp_msg;
	}
	
	private void addReceivers(ACLMessage msg, ArrayList<AID> aids){
		for(AID aid : aids){
			msg.addReceiver(aid);
		}
	}
	
	private  ArrayList<AID> getTaxis(){
		DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd1 = new ServiceDescription();
        sd1.setType("TAXI");
        template.addServices(sd1);
        
        ArrayList<AID> aids = new ArrayList<>();
        DFAgentDescription[] results = null;
		try {
			results = DFService.search(this.getAgent(), template);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
        
        for(DFAgentDescription result : results){
        	aids.add(result.getName());
        }
        
        return aids;
	}
}
