package message;

import java.util.List;

import transfer.Message;
import businessobject.Alignment;

public class PositionMsg extends Message {
	private String dashboardName;
	private String viewName;
	private int relativeSize;
	private Alignment layout;
	private List<String> visualizations;

	public PositionMsg(String dashboardName, String viewName, int relativeSize, boolean horizontal,	List<String> visualizations) {
		super();
		this.dashboardName = dashboardName;
		this.viewName = viewName;
		this.relativeSize = relativeSize;
		this.visualizations = visualizations;
	}
	public String getDashboardName() {
		return dashboardName;
	}
	public void setDashboardName(String dashboardName) {
		this.dashboardName = dashboardName;
	}
	public String getViewName() {
		return viewName;
	}
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
	public int getRelativeSize() {
		return relativeSize;
	}
	public void setRelativeSize(int relativeSize) {
		this.relativeSize = relativeSize;
	}
	public List<String> getVisualizations() {
		return visualizations;
	}
	public void setVisualizations(List<String> visualizations) {
		this.visualizations = visualizations;
	}
	public Alignment getLayout() {
		return layout;
	}
	public void setLayout(Alignment layout) {
		this.layout = layout;
	}
	


}
