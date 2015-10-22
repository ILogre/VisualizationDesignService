package message;

import transfer.Message;

public class CharacterizeVisuMsg extends Message {
	private String dashboardName;
	private String visuName;
	private String[] howQualifier;

	public CharacterizeVisuMsg(String dashboardName, String visuName, String ... howQualifier) {
		super();
		this.dashboardName = dashboardName;
		this.visuName = visuName;
		this.howQualifier = howQualifier;
	}
	public String getDashboardName() {
		return dashboardName;
	}
	public void setDashboardName(String dashboardName) {
		this.dashboardName = dashboardName;
	}
	public String getVisuName() {
		return visuName;
	}
	public void setVisuName(String visuName) {
		this.visuName = visuName;
	}
	public String[] getHowQualifiers() {
		return howQualifier;
	}
	
}
