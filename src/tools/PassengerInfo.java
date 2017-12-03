package tools;

public class PassengerInfo {
	private int initial_x;
	private int initial_y;
	private int final_x;
	private int final_y;
	private String name;
	
	public PassengerInfo(int initial_x, int initial_y, int final_x, int final_y, String name) {
		super();
		this.initial_x = initial_x;
		this.initial_y = initial_y;
		this.final_x = final_x;
		this.final_y = final_y;
		this.name = name;
	}
	
	public PassengerInfo(String info){
		String[] info_parts = info.split("-");
		this.initial_x = Integer.parseInt(info_parts[1]);
		this.initial_y = Integer.parseInt(info_parts[2]);
		this.final_x = Integer.parseInt(info_parts[3]);
		this.final_y = Integer.parseInt(info_parts[4]);
		this.name = info_parts[0];
	}
	
	@Override
	public String toString() {
		return this.name + "-" + this.initial_x + "-" + this.initial_y + "-" + this.final_x + "-" + this.final_y;
	}

	public int getInitial_x() {
		return initial_x;
	}

	public void setInitial_x(int initial_x) {
		this.initial_x = initial_x;
	}

	public int getInitial_y() {
		return initial_y;
	}

	public void setInitial_y(int initial_y) {
		this.initial_y = initial_y;
	}

	public int getFinal_x() {
		return final_x;
	}

	public void setFinal_x(int final_x) {
		this.final_x = final_x;
	}

	public int getFinal_y() {
		return final_y;
	}

	public void setFinal_y(int final_y) {
		this.final_y = final_y;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
