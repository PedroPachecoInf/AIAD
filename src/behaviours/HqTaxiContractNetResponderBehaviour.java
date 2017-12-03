package behaviours;

import agents.Taxi;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;
import tools.PassengerInfo;
import tools.TaxiService;

public class HqTaxiContractNetResponderBehaviour extends ContractNetResponder{
	private Taxi taxi;
	
	public HqTaxiContractNetResponderBehaviour(Agent a, MessageTemplate mt) {
		super(a, mt);
		this.taxi = (Taxi) a;
	}
	
	@Override
	protected ACLMessage handleCfp(ACLMessage cfp) throws NotUnderstoodException, RefuseException {
		int proposal = calculateProposal(new PassengerInfo(cfp.getContent()));
		ACLMessage propose = cfp.createReply();
		propose.setPerformative(ACLMessage.PROPOSE);
		propose.setContent(String.valueOf(proposal));
		return propose;
	}
	
	@Override
	protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose,ACLMessage accept) throws FailureException {
		System.out.println(this.getAgent().getLocalName() + ": accepted");
		
		PassengerInfo info = new PassengerInfo(cfp.getContent());
		TaxiService service = new TaxiService(info, new AID(accept.getContent(), AID.ISLOCALNAME));
		this.taxi.addService(service);
		
		ACLMessage inform = accept.createReply();
		inform.setPerformative(ACLMessage.INFORM);
		inform.setContent(this.getAgent().getLocalName());
		return inform;
	}
	
	protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose, ACLMessage reject) {
		System.out.println(this.getAgent().getLocalName() + ": rejected");
	}
	
	private int calculateProposal(PassengerInfo info){
		int x1 = this.taxi.getX(), y1 = this.taxi.getY(), total = 0;
		
		for(TaxiService service : this.taxi.getServices()){
			if(!service.isInCar()){
				total = total + calculateDistanceTwoPoints(x1, y1, service.getInfo().getInitial_x(), service.getInfo().getInitial_y());
				x1 = service.getInfo().getInitial_x();
				y1 = service.getInfo().getInitial_y();
			}
			
			total = total + calculateDistanceTwoPoints(x1, y1, service.getInfo().getFinal_x(), service.getInfo().getFinal_y());
			x1 = service.getInfo().getFinal_x();
			y1 = service.getInfo().getFinal_y();
		}
		
		total = total + calculateDistanceTwoPoints(x1, y1, info.getInitial_x(), info.getInitial_y());
		
		return total;
	}
	
	private int calculateDistanceTwoPoints(int x1, int y1, int x2, int y2){
		return (int) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}
	
	/*
	private int calculateDistance(PassengerInfo info){
		return (int) Math.sqrt(Math.pow((taxi.getX() - info.getInitial_x()), 2) + Math.pow((taxi.getY() - info.getInitial_y()), 2));
	}
	*/
	
}
