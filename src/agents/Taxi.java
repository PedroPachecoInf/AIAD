package agents;
import jade.core.*;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.DFService;
import jade.domain.FIPAException;

public class Taxi extends Agent{
	private int capacity,sits,x,y;
	
	public Taxi() {
		if(sits>capacity)
			throw new ArithmeticException("sits can't be higher than capacity");
		
	}
	
	class TaxiBehaviour extends SimpleBehaviour {

		@Override
		public void action() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean done() {
			// TODO Auto-generated method stub
			return false;
		}
		
	}
	
	protected void setup() {
		
	}
	
	protected void takeDown() {
		
	}
	
	private int getCapacity() {
		return capacity;
	}
	
	private int getSits() {
		return sits;
	}
	
	private int getX() {
		return x;
	}
	
	private int getY() {
		return y;
	}
	
	private void addPassengers(int number) {
		sits-=number;
	}
	
	private void removePassengers(int number) {
		sits+=number;
	}
	
	private void setX(int newX) {
		x=newX;
	}
	
	private void setY(int newY) {
		y=newY;
	}
	

	
}
