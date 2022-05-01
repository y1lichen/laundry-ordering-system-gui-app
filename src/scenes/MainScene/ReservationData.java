package scenes.MainScene;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ReservationData {
	private SimpleIntegerProperty id;
	private SimpleStringProperty machine;
	private SimpleStringProperty time;
	
	public ReservationData(int id, String machine, String time) {
		this.id = new SimpleIntegerProperty(id);
		this.machine = new SimpleStringProperty(machine);
		this.time = new SimpleStringProperty(time);
	}
	
	public int getId() {
		return id.get();
	}
	
	public void setId(int id) {
		this.id.set(id);
	}
	
	public String getMachine() {
		return machine.get();
	}
	
	public void setMachine(String machine) {
		this.machine.set(machine);
	}
	
	public String getTime() {
		return time.get();
	}
	
	public void setTime(String time) {
		this.time.set(time);
	}
}
