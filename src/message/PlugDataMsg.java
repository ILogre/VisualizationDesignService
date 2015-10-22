package message;

import java.util.HashMap;
import java.util.Map;

import transfer.Message;

public class PlugDataMsg extends Message {
	private String dashboardName;
	private String visuName;
	private String dataName;
	private Map<String, Map<String,Object>> concerns;

	public PlugDataMsg(String dashboardName, String visuName, String dataName, String concern, Map<String,Object> params) {
		super();
		this.dashboardName = dashboardName;
		this.visuName = visuName;
		this.dataName = dataName;
		this.concerns= new HashMap<String, Map<String,Object>>();
		concerns.put(concern, params);
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
	public String getDataName() {
		return dataName;
	}
	public void setDataName(String dataName) {
		this.dataName = dataName;
	}
	public Map<String, Map<String, Object>> getConcerns() {
		return concerns;
	}
	public void setConcerns(Map<String, Map<String, Object>> concerns) {
		this.concerns = concerns;
	}
}
