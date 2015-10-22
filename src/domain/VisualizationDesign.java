package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import message.CharacterizeVisuMsg;
import message.DeclareDashboardMsg;
import message.PlugDataMsg;
import message.PositionMsg;
import businessobject.*;

public class VisualizationDesign {
	
	public static void declareDashboard(DeclareDashboardMsg msg){
		String name = msg.getDashboardName();
		System.out.println("Dashboard "+name+" :"+"\t\t ("+System.currentTimeMillis()+" )");
	}
	
	public static void characterizeVisu(CharacterizeVisuMsg msg){
		String dashboardName = msg.getDashboardName();
		String visuName = msg.getVisuName();
		System.out.println(visuName+" shows "+ msg.getHowQualifiers()+"\t\t ("+System.currentTimeMillis()+" )");
	}
	
	public static void plugData(PlugDataMsg msg){ //, Map<String, Map<String,Object>> concerns
		String dashboardName = msg.getDashboardName();
		String visuName = msg.getVisuName();
		String dataName = msg.getDataName();
		System.out.println("VisuDesign : data \""+dataName+"\" pluged on visualization \""+visuName+" with concerns "+msg.getConcerns()+
				"\" at "+System.currentTimeMillis());
		Registry.getInstance().getDashboard(dashboardName).getVisu(visuName).addData(dataName);
	}
	
	public static void position(PositionMsg msg){
		String dashboardName = msg.getDashboardName();
		String viewName = msg.getViewName();
		int relativeSize = msg.getRelativeSize();
		Alignment layout = msg.getLayout();
		//String container= (horizontal)?"Line":"Column";
		List<String> visualizations = msg.getVisualizations();
		
		System.out.println("View \""+viewName+"\" is a "+ layout +" sized "+ relativeSize
				+ " containing the visualizations " + visualizations
				+" at "+ System.currentTimeMillis());
		
			
	}
	
	private static class Registry{
		private static Registry instance;
		private List<Dashboard> dashboards;
		private Registry(){ this.dashboards = new ArrayList<VisualizationDesign.Dashboard>();}
		
		public static Registry getInstance(){
			if(instance==null) instance=new Registry();
			return instance;
		}
		public void declareDashboard(Dashboard d){
			this.dashboards.add(d);
		}
		public Dashboard getDashboard(String dashboardName){
			for(Dashboard d : dashboards)
				if(d.getName()==dashboardName)
					return d;
			return null;
		}
		
	}
	
	private static class Dashboard {
		private String name;
		private List<Visualization> visus;
		public Dashboard(String name) {
			this.name = name;
			visus = new ArrayList<Visualization>();
		}
		public Visualization getVisu(String visuName) {
			for(Visualization v : visus)
				if(v.getName()==visuName)
					return v;
			return null;
		}
		public void addVisu(Visualization v){this.visus.add(v);}
		public Boolean hasVisu(Visualization v){ return this.visus.contains(v);}
		public String getName(){return this.name;}
	}
	
	private static class Visualization {
		private String name;
		private List<String> data;
		private List<Concern> concerns;
		
		public Visualization(String name) {
			this.name = name;
			data = new ArrayList<String>();
			concerns = new ArrayList<Concern>();
		}
		public String getName() {
			return name;
		}
		public void addData(String uri){this.data.add(uri);}
		public void addConcern(Concern c){this.concerns.add(c);}
	}
	
	private static class Concern {
		private String name;
		private Map<String,Object> params;
		
		public Concern(String name) {
			this.name = name;
			params = new HashMap<String,Object>();
		}

		public Concern(String name, Map<String,Object> params) {
			this(name);
			this.params.putAll(params);
		}
	}
}
