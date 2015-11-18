package message;

import transfer.Message;

public class CharacterizeVisuMsg extends Message {
	private String dashboardName;
	private String visuName;
	private String[] whatQualifier;

	public CharacterizeVisuMsg(String dashboardName, String visuName, String ... whatQualifier) {
		super();
		this.dashboardName = dashboardName;
		this.visuName = visuName;
		this.whatQualifier = whatQualifier;
	}
	public String getDashboardName() {
		return dashboardName;
	}
	public String getVisuName() {
		return visuName;
	}
	public String[] getWhatQualifiers() {
		return whatQualifier;
	}
	
}
