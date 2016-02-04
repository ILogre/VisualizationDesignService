package message;

import transfer.Message;

public class CharacterizeVisuMsg extends Message {
	private String dashboardName;
	private String visuName;
	private String[] whatQualifiers;

	public CharacterizeVisuMsg(String dashboardName, String visuName, String ... whatQualifier) {
		super();
		this.dashboardName = dashboardName;
		this.visuName = visuName;
		this.whatQualifiers = whatQualifier;
	}
	public String getDashboardName() {
		return dashboardName;
	}
	public String getVisuName() {
		return visuName;
	}
	public String[] getWhatQualifiers() {
		return whatQualifiers;
	}
	
}
