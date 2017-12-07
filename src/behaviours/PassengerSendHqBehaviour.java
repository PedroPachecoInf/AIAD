package behaviours;

import agents.Passenger;
import gui.Gui;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class PassengerSendHqBehaviour extends OneShotBehaviour {
	private Passenger passenger;

	public PassengerSendHqBehaviour(Passenger a) {
		this.passenger = (Passenger) a;
	}

	@Override
	public void action() {
		Gui.newPassenger(this.passenger.getInfo().getInitial_y(), this.passenger.getInfo().getInitial_x(), this.passenger.getLocalName());
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd1 = new ServiceDescription();
		sd1.setType("HQ");
		template.addServices(sd1);

		try {
			DFAgentDescription[] result = DFService.search(this.passenger, template);
			if (result.length == 0) {
				System.out.println("HQ Agent does not exist");
				return;
			}
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			msg.setProtocol("PassengerHqProtocol");
			msg.addReceiver(result[0].getName());
			msg.setContent(this.passenger.getInfo().toString());
			passenger.send(msg);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		
		MessageTemplate reply_template = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM),
											MessageTemplate.MatchProtocol("PassengerHqProtocol"));
		
		System.out.println(this.getAgent().getLocalName() + ": waiting to know my taxi");
		ACLMessage reply = this.getAgent().blockingReceive(reply_template);
		System.out.println(this.getAgent().getLocalName() + ": My taxi's name is " + reply.getContent());
		
		this.passenger.setMyTaxyName(reply.getContent());
		
	}

}
