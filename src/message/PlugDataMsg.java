package message;

import java.util.Map;

import transfer.Message;

public class PlugDataMsg extends Message {
	private String dashboardName;
	private String visuName;
	private String dataName;
	private String dataLocation;
	private String dataType;
	private Map<String, Map<String,Object>> concerns;

	public PlugDataMsg(String dashboardName, String visuName, String dataName,
			String dataLocation, String dataType,
			Map<String, Map<String, Object>> concerns) {
		super();
		this.dashboardName = dashboardName;
		this.visuName = visuName;
		this.dataName = dataName;
		this.dataLocation = dataLocation;
		this.dataType = dataType;
		this.concerns = concerns;
	}
	public String getDashboardName() {
		return dashboardName;
	}
	public String getVisuName() {
		return visuName;
	}
	public String getDataName() {
		return dataName;
	}
	public Map<String, Map<String, Object>> getConcerns() {
		return concerns;
	}
	public String getDataLocation() {
		return dataLocation;
	}
	public String getDataType() {
		return dataType;
	}
	
	
}
