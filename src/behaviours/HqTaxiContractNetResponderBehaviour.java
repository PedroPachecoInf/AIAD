package behaviours;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import agents.Taxi;
import gui.Gui;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;
import tools.PassengerInfo;
import tools.Proposal;
import tools.TaxiService;

public class HqTaxiContractNetResponderBehaviour extends ContractNetResponder{
	private Taxi taxi;
	private boolean share_on = true;
	
	public HqTaxiContractNetResponderBehaviour(Agent a, MessageTemplate mt, Boolean share_on) {
		super(a, mt);
		this.taxi = (Taxi) a;
		this.share_on = share_on;
	}
	
	@Override
	protected ACLMessage handleCfp(ACLMessage cfp) throws NotUnderstoodException, RefuseException {
		Proposal proposal = calculateProposal(new PassengerInfo(cfp.getContent()));
		ACLMessage propose = cfp.createReply();
		propose.setPerformative(ACLMessage.PROPOSE);
		propose.setContent(proposal.toString());
		return propose;
	}
	
	@Override
	protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose,ACLMessage accept) throws FailureException {
		System.out.println(this.getAgent().getLocalName() + ": accepted");
		Proposal proposal = new Proposal(propose.getContent());
		PassengerInfo info = new PassengerInfo(cfp.getContent());
		TaxiService service = new TaxiService(info, new AID(accept.getContent(), AID.ISLOCALNAME));
		
		if(proposal.isShare() == false){
			this.taxi.addService(service);
		}else{
			ArrayList<TaxiService> services = this.taxi.getServices();
			
			for(TaxiService ser : services){
				if(ser.getPassenger().getLocalName().equals(proposal.getPassenger())){
					ser.setShareService(service);
					System.out.println(accept.getContent() + ": I am going to share a ride with " + ser.getPassenger().getLocalName());
					Gui.addConsole(accept.getContent() + ": I am going with " + ser.getPassenger().getLocalName());
				}
			}
		}
		
		ACLMessage inform = accept.createReply();
		inform.setPerformative(ACLMessage.INFORM);
		inform.setContent(this.getAgent().getLocalName());
		return inform;
	}
	
	protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose, ACLMessage reject) {
		System.out.println(this.getAgent().getLocalName() + ": rejected");
	}
	
	private Proposal calculateProposal(PassengerInfo info){
		int x1 = this.taxi.getX(), y1 = this.taxi.getY(), total = 0;
		boolean share = false;
		String share_passenger = null;
		/*
		if(true){
			total = ThreadLocalRandom.current().nextInt(0, 100 + 1);
			System.out.println(total);
			return new Proposal(total, share_passenger, share);
		}
		*/
		for(TaxiService service : this.taxi.getServices()){
			if(service.getShareService() == null && share_on){
				double dist_start, dist_end;
				int angle;
				
				if(service.isInCar()){
					dist_start = Line2D.ptSegDist(this.taxi.getX(), this.taxi.getY(), service.getInfo().getFinal_x(), service.getInfo().getFinal_y(), info.getInitial_x(), info.getInitial_y());
					dist_end = Line2D.ptSegDist(this.taxi.getX(), this.taxi.getY(), service.getInfo().getFinal_x(), service.getInfo().getFinal_y(), info.getFinal_x(), info.getFinal_y());
					angle = getAngle(service.getInfo().getFinal_x() - this.taxi.getX(), service.getInfo().getFinal_y() - this.taxi.getY(), info.getFinal_x() - info.getInitial_x(), info.getFinal_y() - info.getInitial_y());
				}else{
					dist_start = Line2D.ptSegDist(service.getInfo().getInitial_x(), service.getInfo().getInitial_y(), service.getInfo().getFinal_x(), service.getInfo().getFinal_y(), info.getInitial_x(), info.getInitial_y());
					dist_end = Line2D.ptSegDist(service.getInfo().getInitial_x(), service.getInfo().getInitial_y(), service.getInfo().getFinal_x(), service.getInfo().getFinal_y(), info.getFinal_x(), info.getFinal_y());
					angle = getAngle(service.getInfo().getFinal_x() - service.getInfo().getInitial_x(), service.getInfo().getFinal_y() - service.getInfo().getInitial_x(), info.getFinal_x() - info.getInitial_x(), info.getFinal_y() - info.getInitial_y());
				}
				
				if(dist_start < 4 && dist_end < 4 && angle > -90 && angle < 90 ){
					share = true;
					share_passenger = service.getPassenger().getLocalName();
					return new Proposal(total, share_passenger, share);
				}
			}
			
			
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
		
		return new Proposal(total, share_passenger, share);
	}
	
	private int calculateDistanceTwoPoints(int x1, int y1, int x2, int y2){
		return (int) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}
	
	private boolean canShare(TaxiService service, PassengerInfo info){
		return false;
	}
	
	private int getAngle(int x1, int y1, int x2, int y2){
		return  (int) Math.toDegrees(Math.atan2(y2, x2) - Math.atan2(y1, x1));
	}
	
}
