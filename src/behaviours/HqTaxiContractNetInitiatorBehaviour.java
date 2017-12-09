package behaviours;

import java.util.Enumeration;
import java.util.Vector;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;
import tools.Proposal;

public class HqTaxiContractNetInitiatorBehaviour extends ContractNetInitiator{
	private ACLMessage cfp_msg;
	private ACLMessage passenger_msg;

	public HqTaxiContractNetInitiatorBehaviour(Agent a, ACLMessage cfp, ACLMessage passenger_msg) {
		super(a, cfp);
		this.cfp_msg = cfp;
		this.passenger_msg = passenger_msg;
	}
	
	@Override
	protected void handleAllResponses(Vector responses, Vector acceptances) {
		int best_proposal = Integer.MAX_VALUE;
		ACLMessage best_proposer = null;
		
		Enumeration e = responses.elements();
		System.out.println("Received Proposals:");
		while (e.hasMoreElements()) {
			ACLMessage msg = (ACLMessage) e.nextElement();
			if (msg.getPerformative() == ACLMessage.PROPOSE){
				System.out.println(msg.getSender().getLocalName() + ": " + msg.getContent());
				ACLMessage reply = msg.createReply();
				reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
				acceptances.addElement(reply);
				int proposal = new Proposal(msg.getContent()).getProposal();
				if(proposal < best_proposal){
					best_proposer = reply;
					best_proposal = proposal;
				}
			}
		}
		
		if(best_proposer != null){
			best_proposer.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
			best_proposer.setContent(this.passenger_msg.getSender().getLocalName());
		}
		
		
	}
	
	@Override
	protected void handleInform(ACLMessage inform) {
		ACLMessage passenger_reply = passenger_msg.createReply();
		passenger_reply.setPerformative(ACLMessage.INFORM);
		passenger_reply.setProtocol("PassengerHqProtocol");
		passenger_reply.setContent(inform.getContent());
		this.getAgent().send(passenger_reply);
	}
	
	@Override
	protected void handleOutOfSequence(ACLMessage msg){
		System.out.println("got a out of sequence message saying: " + msg.getContent());
	}

}
