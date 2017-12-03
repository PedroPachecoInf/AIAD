package behaviours;

import java.util.ArrayList;

import agents.Taxi;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import tools.PassengerInfo;
import tools.TaxiService;

public class TaxiServiceBehaviour extends CyclicBehaviour {
	private int wait_time = 100;
	private long time;
	
	private Taxi taxi;
	MessageTemplate template_reply = MessageTemplate.or(MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.AGREE), MessageTemplate.MatchProtocol("TaxiPassengerProtocol")),
														MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REFUSE), MessageTemplate.MatchProtocol("TaxiPassengerProtocol")));
	
	public TaxiServiceBehaviour(Taxi taxi){
		this.time = System.currentTimeMillis();
		this.taxi = taxi;
	}
	
	public void action() {
		if (this.taxi.getServices().isEmpty()){
			this.time = System.currentTimeMillis();
			return;
		}
			
		moveTaxi();
	}

	public void moveTaxi() {
		if((System.currentTimeMillis() - this.time) < wait_time)
			return;
		
		this.time = System.currentTimeMillis();
		
		TaxiService curr_ser = this.taxi.getService(0);
		
		int[] new_pos;
		if(curr_ser.isInCar()){
			new_pos = nextMove(this.taxi.getX(), this.taxi.getY(), curr_ser.getInfo().getFinal_x(), curr_ser.getInfo().getFinal_y());
			String out = String.format("Taxi x: %d\nTaxi y: %d\nDest x: %d\nDest y: %d", this.taxi.getX(), this.taxi.getY(), curr_ser.getInfo().getFinal_x(), curr_ser.getInfo().getFinal_y());
			//System.out.println(out);
		}
		else{
			new_pos = nextMove(this.taxi.getX(), this.taxi.getY(), curr_ser.getInfo().getInitial_x(), curr_ser.getInfo().getInitial_y());
			String out = String.format("Taxi x: %d\nTaxi y: %d\nDest x: %d\nDest y: %d", this.taxi.getX(), this.taxi.getY(), curr_ser.getInfo().getInitial_x(), curr_ser.getInfo().getInitial_y());
			//System.out.println(out);
		}
		
		this.taxi.setX(new_pos[0]);
		this.taxi.setY(new_pos[1]);
		
		String out = String.format("Res x: %d\nRes y: %d\n\n", this.taxi.getX(), this.taxi.getY());
		//System.out.println(out);
		
		informPassengers();
		
		if(!curr_ser.isInCar() && this.taxi.getX() == curr_ser.getInfo().getInitial_x() && this.taxi.getY() == curr_ser.getInfo().getInitial_y())
			getIn(curr_ser.getPassenger(), curr_ser);
		
	}
	
	public void informPassengers(){
		ArrayList<TaxiService> to_be_removed = new ArrayList<>();
		
		for(TaxiService service : this.taxi.getServices())
			if(service.isInCar()){
				ACLMessage query = new ACLMessage(ACLMessage.QUERY_IF);
				query.setProtocol("TaxiPassengerProtocol");
				query.setContent(this.taxi.getX() + "-" + this.taxi.getY());
				query.addReceiver(service.getPassenger());
				this.taxi.send(query);
				
				ACLMessage reply = this.taxi.blockingReceive(this.template_reply);
				if(reply.getPerformative() == ACLMessage.AGREE)
					to_be_removed.add(service);
			}
		
		for(TaxiService service : to_be_removed)
			this.taxi.removeService(service);
	}
	
	public void getIn(AID aid, TaxiService ser){
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.setProtocol("TaxiPassengerProtocol");
		msg.setContent(this.taxi.getX() + "-" + this.taxi.getY() + "-" + this.taxi.getLocalName());
		msg.addReceiver(ser.getPassenger());
		this.taxi.send(msg);
		
		ACLMessage reply = this.taxi.blockingReceive(this.template_reply);
		if(reply.getPerformative() == ACLMessage.AGREE)
			ser.setIsInCar(true);
		else
			this.taxi.getService(0).setInfo(new PassengerInfo(reply.getContent()));
	}
	
	public static int[] nextMove(int x1, int y1, int x2, int y2) {
		int nx = 0, ny = 0;
		
		int dx = Math.abs(x2 - x1);
		int dy = Math.abs(y2 - y1);

		int sx = (x1 < x2) ? 1 : -1;
		int sy = (y1 < y2) ? 1 : -1;

		int err = dx - dy;

		int e2 = 2 * err;

		if (e2 > -dy) {
			err = err - dy;
			nx = x1 + sx;
		}else
			nx = x1;

		if (e2 < dx) {
			err = err + dx;
			ny = y1 + sy;
		}else
			ny = y1;
		
		return new int[]{nx, ny};
	}

}
