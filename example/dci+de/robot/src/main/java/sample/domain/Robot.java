package sample.domain;

import com.jdon.annotation.Model;

@Model
public class Robot {

	private String id;
	private String name;

	private CPU cpu;
	private Motor motoe;
	private Sensor sensor;
	private Sonar sonar;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CPU getCpu() {
		return cpu;
	}

	public void setCpu(CPU cpu) {
		this.cpu = cpu;
	}

	public Motor getMotoe() {
		return motoe;
	}

	public void setMotoe(Motor motoe) {
		this.motoe = motoe;
	}

	public Sensor getSensor() {
		return sensor;
	}

	public void setSensor(Sensor sensor) {
		this.sensor = sensor;
	}

	public Sonar getSonar() {
		return sonar;
	}

	public void setSonar(Sonar sonar) {
		this.sonar = sonar;
	}

}
