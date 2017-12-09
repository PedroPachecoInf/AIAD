package tools;

public class Proposal {
	private int proposal;
	private String passenger;
	private boolean share;

	public Proposal(int proposal, String passenger, boolean share) {
		super();
		this.proposal = proposal;
		this.passenger = passenger;
		this.share = share;
	}
	
	public Proposal(String proposal){
		String[] parts = proposal.split("-");
		if(!parts[1].equals("null")){
			this.passenger = parts[1];
		}
		this.proposal = Integer.parseInt(parts[0]);
		this.share = Boolean.parseBoolean(parts[2]);
	}
	
	public String toString(){
		return String.valueOf(this.proposal) + "-" + this.passenger + "-" + String.valueOf(this.share);
	}

	public int getProposal() {
		return proposal;
	}

	public String getPassenger() {
		return passenger;
	}

	public boolean isShare() {
		return share;
	}
	
}
