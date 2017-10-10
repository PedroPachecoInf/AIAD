package agents;
import jade.core.*;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.DFService;
import jade.domain.FIPAException;


public class Passenger extends Agent{
	
	private boolean inTaxi;
	int xi,yi,xf,yf;
	
	public Passenger(int xi,int yi,int xf,int yf){
		this.xi=xi;
		this.yi=yi;
		this.xf=xf;
		this.yf=yf;
		this.inTaxi=false;
		
	}
	
	
	
	public boolean getInTaxi (){
		 
		return this.inTaxi;
	}
	
	public void setInTaxi(boolean inTaxi){
		
		this.inTaxi=inTaxi;
	}
	
	
	
	
}




