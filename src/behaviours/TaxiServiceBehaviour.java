package behaviours;

import java.util.ArrayList;

import Boot.Boot;
import agents.Taxi;
import gui.Gui;
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
	private int counter = 0;
	private static boolean run = false;
	
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
		if(run)
			if(this.taxi.getService(0).getShareService() == null)
				moveTaxi();
			else
				moveShareTaxi();
	}
	
	public static void changeState(){
		if(run)
			run = false;
		else
			run = true;
	}
	
	public void moveShareTaxi(){
		if((System.currentTimeMillis() - this.time) < wait_time)
			return;
		
		this.time = System.currentTimeMillis();
		
		TaxiService curr_ser = this.taxi.getService(0);
		TaxiService share_ser = curr_ser.getShareService();
		int[] new_pos = new int[]{this.taxi.getX(), this.taxi.getY()};
		int best_move = Integer.MAX_VALUE;
		
		if((!curr_ser.isInCar() && !curr_ser.isCompleted()) || (!share_ser.isInCar() && !share_ser.isCompleted())){
			if(!curr_ser.isInCar()){
				int distance = calculateDistanceTwoPoints(this.taxi.getX(), this.taxi.getY(), curr_ser.getInfo().getInitial_x(), curr_ser.getInfo().getInitial_y()); 
				if(distance < best_move){
					new_pos = nextMove(this.taxi.getX(), this.taxi.getY(), curr_ser.getInfo().getInitial_x(), curr_ser.getInfo().getInitial_y());
					best_move = distance;
				}
			}
			if(!share_ser.isInCar()){
				int distance = calculateDistanceTwoPoints(this.taxi.getX(), this.taxi.getY(), share_ser.getInfo().getInitial_x(), share_ser.getInfo().getInitial_y()); 
				if(distance < best_move){
					new_pos = nextMove(this.taxi.getX(), this.taxi.getY(), share_ser.getInfo().getInitial_x(), share_ser.getInfo().getInitial_y());
					best_move = distance;
				}
			}
		}else{
			if(!curr_ser.isCompleted()){
				int distance = calculateDistanceTwoPoints(this.taxi.getX(), this.taxi.getY(), curr_ser.getInfo().getFinal_x(), curr_ser.getInfo().getFinal_y());
				if(distance < best_move){
					new_pos = nextMove(this.taxi.getX(), this.taxi.getY(), curr_ser.getInfo().getFinal_x(), curr_ser.getInfo().getFinal_y());
					best_move = distance;
				}
			}
			if(!share_ser.isCompleted()){
				int distance = calculateDistanceTwoPoints(this.taxi.getX(), this.taxi.getY(), share_ser.getInfo().getFinal_x(), share_ser.getInfo().getFinal_y());
				if(distance < best_move){
					new_pos = nextMove(this.taxi.getX(), this.taxi.getY(), share_ser.getInfo().getFinal_x(), share_ser.getInfo().getFinal_y());
					best_move = distance;
				}
			}
		}
		
		int old_x = this.taxi.getX();
		int old_y = this.taxi.getY();
		
		this.taxi.setX(new_pos[0]);
		this.taxi.setY(new_pos[1]);
		
		ArrayList<String> names = informPassengers();
		
		if(!curr_ser.isInCar() && this.taxi.getX() == curr_ser.getInfo().getInitial_x() && this.taxi.getY() == curr_ser.getInfo().getInitial_y()){
			getIn(curr_ser.getPassenger(), curr_ser);
			Gui.resetCell(curr_ser.getInfo().getInitial_y(), curr_ser.getInfo().getInitial_x());
		}
		
		if(!share_ser.isInCar() && this.taxi.getX() == share_ser.getInfo().getInitial_x() && this.taxi.getY() == share_ser.getInfo().getInitial_y()){
			getIn(share_ser.getPassenger(), share_ser);
			Gui.resetCell(share_ser.getInfo().getInitial_y(), share_ser.getInfo().getInitial_x());
		}
		
		Gui.moveTaxi(old_y, old_x, this.taxi.getY(), this.taxi.getX(), this.taxi.getLocalName(), names.get(0), names.get(1));
		
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
		
		int old_x = this.taxi.getX();
		int old_y = this.taxi.getY();
		
		this.taxi.setX(new_pos[0]);
		this.taxi.setY(new_pos[1]);
		
		String out = String.format("Res x: %d\nRes y: %d\n\n", this.taxi.getX(), this.taxi.getY());
		//System.out.println(out);
		
		ArrayList<String> names = informPassengers();
		
		if(!curr_ser.isInCar() && this.taxi.getX() == curr_ser.getInfo().getInitial_x() && this.taxi.getY() == curr_ser.getInfo().getInitial_y()){
			getIn(curr_ser.getPassenger(), curr_ser);
			Gui.resetCell(curr_ser.getInfo().getInitial_y(), curr_ser.getInfo().getInitial_x());
		}
		
		Gui.moveTaxi(old_y, old_x, this.taxi.getY(), this.taxi.getX(), this.taxi.getLocalName(), names.get(0), names.get(1));
		
	}
	
	public ArrayList<String> informPassengers(){
		ArrayList<String> passenger_names = new ArrayList<>();
		ArrayList<TaxiService> to_be_removed = new ArrayList<>();
		
		for(TaxiService service : this.taxi.getServices()){
			if(service.isInCar() && !service.isCompleted()){
				passenger_names.add(service.getPassenger().getLocalName());
				
				String cost = null;
				if(service.getShareService() != null && service.getShareService().isInCar())
					cost = "0.5";
				else
					cost = "1.0";
				
				ACLMessage query = new ACLMessage(ACLMessage.QUERY_IF);
				query.setProtocol("TaxiPassengerProtocol");
				query.setContent(this.taxi.getX() + "-" + this.taxi.getY() + "-" + cost);
				query.addReceiver(service.getPassenger());
				this.taxi.send(query);
				
				ACLMessage reply = this.taxi.blockingReceive(this.template_reply);
				if(reply.getPerformative() == ACLMessage.AGREE){
					if(service.getShareService() != null && service.getShareService().isCompleted())
						to_be_removed.add(service);
					else
						service.setCompleted(true);
					if(service.getShareService() == null)
						to_be_removed.add(service);
					Gui.resetCell(service.getInfo().getFinal_y(), service.getInfo().getFinal_x());
				}
			}
			if(service.getShareService() != null && !service.getShareService().isCompleted()){
				TaxiService share_ser = service.getShareService();
				if(share_ser.isInCar()){
					passenger_names.add(share_ser.getPassenger().getLocalName());
					
					String cost = null;
					if(service.isInCar())
						cost = "0.5";
					else
						cost = "1.0";
					
					ACLMessage query = new ACLMessage(ACLMessage.QUERY_IF);
					query.setProtocol("TaxiPassengerProtocol");
					query.setContent(this.taxi.getX() + "-" + this.taxi.getY() + "-" + cost);
					query.addReceiver(share_ser.getPassenger());
					this.taxi.send(query);
					
					ACLMessage reply = this.taxi.blockingReceive(this.template_reply);
					if(reply.getPerformative() == ACLMessage.AGREE){
						if(service.isCompleted())
							to_be_removed.add(service);
						else
							share_ser.setCompleted(true);
						Gui.resetCell(share_ser.getInfo().getFinal_y(), share_ser.getInfo().getFinal_x());
					}
				}
			}
		}
			
		
		for(TaxiService service : to_be_removed)
			this.taxi.removeService(service);
		
		if(passenger_names.size() == 0){
			passenger_names.add("");
			passenger_names.add("");
		}else if(passenger_names.size() == 1)
			passenger_names.add("");
		
		return passenger_names;
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
	
	private int calculateDistanceTwoPoints(int x1, int y1, int x2, int y2){
		return (int) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}

}
