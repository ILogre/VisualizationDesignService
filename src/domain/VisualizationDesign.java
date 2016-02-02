package domain;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import message.CharacterizeVisuMsg;
import message.DeclareDashboardMsg;
import message.IsValidatedDashboardAsw;
import message.IsValidatedDashboardMsg;
import message.PlugDataMsg;
import message.PositionMsg;
import message.ValidateAndPersistDashboardMsg;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;

import transfer.Service;
import visualizationDesignLanguage.Cell;
import visualizationDesignLanguage.Colorization;
import visualizationDesignLanguage.Container;
import visualizationDesignLanguage.Dashboard;
import visualizationDesignLanguage.DataProperty;
import visualizationDesignLanguage.Icon;
import visualizationDesignLanguage.Property;
import visualizationDesignLanguage.Source;
import visualizationDesignLanguage.Taxonomy;
import visualizationDesignLanguage.Threshold;
import visualizationDesignLanguage.Visualization;
import visualizationDesignLanguage.VisualizationDesignLanguageFactory;
import visualizationDesignLanguage.WhatQualifier;
import visualizationDesignLanguage.Window;
import businessobject.Alignment;
import errors.UnknownDashboardException;

/*
 * This class represent the domain knowledge of the Visualization Design domain
 * It implements the exposed operations with EMF stack
 */

public class VisualizationDesign extends Service {

	static private Map<String,Dashboard> currents = new HashMap<String,Dashboard>();
	static private boolean validated = false;
	
	static private Dashboard getDashboard(String name) throws UnknownDashboardException{
		if(!currents.containsKey(name)){
			Dashboard preexisting = loadModel(name);
			if (preexisting==null){
				throw new UnknownDashboardException("[ERROR] : Dashboard " + name + " does not exist"+ "\t\t (" + System.currentTimeMillis() + " )");
			}else{
				currents.put(name, preexisting); 
				return preexisting;
			}
		}else
			return currents.get(name);
	}
	
	static private void updateDashboard(String name, Dashboard c){
		if(!currents.containsKey(name))
			currents.put(name, c);
		else
			currents.replace(name, currents.get(name), c);
	}
	
	public static void declareDashboard(DeclareDashboardMsg msg) {
		String name = msg.getDashboardName();
		try{
			getDashboard(name);
			System.out.println("--> [Warning] : Dashboard " + name + " already exists" + "\t\t (" + System.currentTimeMillis() + " )");
		}
		catch(UnknownDashboardException e){
			Dashboard d = VisualizationDesignLanguageFactory.eINSTANCE.createDashboard();
			d.setName(name);
			updateDashboard(name, d);;
			System.out.println("Dashboard " + name + " created" + "\t\t (" + System.currentTimeMillis() + " )");
		}
		validated = false;
	}

	public static void characterizeVisu(CharacterizeVisuMsg msg) throws UnknownDashboardException {
		String dashboardName = msg.getDashboardName();
		String visuName = msg.getVisuName();
		String[] concerns = msg.getWhatQualifiers();

		Dashboard preexisting = getDashboard(dashboardName);
		if (preexisting != null) {
			Visualization v = VisualizationDesignLanguageFactory.eINSTANCE
					.createVisualization();
			v.setName(visuName);
			for (String s : concerns) {
				WhatQualifier c = VisualizationDesignLanguageFactory.eINSTANCE
						.createWhatQualifier();
				c.setConcern(Taxonomy.valueOf(s.toUpperCase()));
				v.getConcerns().add(c);
			}
			preexisting.getVisualizations().add(v);

			StringBuilder qualif = new StringBuilder();
			for(String s : msg.getWhatQualifiers())
				qualif.append(s+" ");
			System.out.println("Dashboard "+ dashboardName + " contains "+visuName + " which shows " + qualif.toString() + "\t\t (" + System.currentTimeMillis() + " )");
			
			validated = false;
		} else {
			System.out.println("--> [ERROR] : Dashboard " + dashboardName + " does not exist"
					+ "\t\t (" + System.currentTimeMillis()	+ " )");
		}
	}

	public static void plugData(PlugDataMsg msg) throws UnknownDashboardException {
		String dashboardName = msg.getDashboardName();
		String visuName = msg.getVisuName();
		String dataName = msg.getDataName();
		String dataLocation = msg.getDataLocation();
		String dataType = msg.getDataType();
		
		Map<String, Map<String, Object>> concerns = msg.getConcerns();
		
		Dashboard preexisting = getDashboard(dashboardName);
		if (preexisting != null) {
			Source s = VisualizationDesignLanguageFactory.eINSTANCE.createSource();
			s.setName(dataName);
			s.setLocation(dataLocation);
			s.setType(dataType);
			for (String c : concerns.keySet()) {
				switch (c){
					case "Icon":
						Icon icon = VisualizationDesignLanguageFactory.eINSTANCE.createIcon();
						s.getConcerns().add(icon);
						break;
					case "Colorization":
						Colorization color = VisualizationDesignLanguageFactory.eINSTANCE.createColorization();
						color.setColor((String) concerns.get(c).get("color"));
						s.getConcerns().add(color);
						break;
					case "Threshold":
						Threshold t = VisualizationDesignLanguageFactory.eINSTANCE.createThreshold();
						t.setMin((int) concerns.get(c).get("min"));
						t.setMax((int) concerns.get(c).get("max"));
						s.getConcerns().add(t);
						break;
					case "Property" :
						Property p = VisualizationDesignLanguageFactory.eINSTANCE.createProperty();
						p.setValue(DataProperty.valueOf((String) concerns.get(c).get("value")));
						s.getConcerns().add(p);
						break;
				}
			}
			Visualization visu = null;
			for(Visualization v : preexisting.getVisualizations()){
				if(v.getName().equalsIgnoreCase(visuName)){
					visu=v;
					break;
				}
			}
			visu.getDisplays().add(s);

			System.out.println("Data \"" + dataName	+ "\" pluged on visualization \"" + visuName + " with concerns "
					+ concerns + " in dashboard " + dashboardName + "\t\t (" + System.currentTimeMillis() + " )");
			
			validated = false;
		} else {System.out.println("--> [ERROR] : Dashboard " + dashboardName + " does not exist" + "\t\t (" + System.currentTimeMillis() + " )");}
		
	}

	public static void position(PositionMsg msg) throws UnknownDashboardException {
		String dashboardName = msg.getDashboardName();
		String viewName = msg.getViewName();
		int relativeSize = msg.getRelativeSize();
		Alignment layout = msg.getLayout();
		// String container= (horizontal)?"Line":"Column";
		List<String> visualizations = msg.getVisualizations();
		
		Dashboard preexisting = getDashboard(dashboardName);
		if (preexisting != null) {
			Window wind = null;
			Boolean newview = true;
			for(Window w : preexisting.getWindows())
				if(w.getName().equalsIgnoreCase(viewName)){
					wind=w;
					newview=false;
					break;
				}
			if(newview){
				wind = VisualizationDesignLanguageFactory.eINSTANCE.createWindow();
				wind.setName(viewName);
			}
			Container c = null;
			switch(layout){
			case Column:
				c = VisualizationDesignLanguageFactory.eINSTANCE.createColumn();
				break;
			case Line:
				c = VisualizationDesignLanguageFactory.eINSTANCE.createLine();
				break;
			}
			c.setPonderation(relativeSize);
			
			for(String v : visualizations){
				Cell wrapper = VisualizationDesignLanguageFactory.eINSTANCE.createCell();
				Visualization visu = locateVisualization(v,preexisting);
				wrapper.setWraps(visu);
				c.getContains().add(wrapper);
			}
			
			wind.getOrganizes().add(c);
			if(newview)
				preexisting.getWindows().add(wind);

			validated = false;
			
			System.out.println("View \"" + viewName + "\" is a " + layout + " sized " + relativeSize
					+ " containing the visualizations "	+ visualizations + " in dashboard " + dashboardName
					+ "\t\t (" + System.currentTimeMillis() + " )");
			
		} else {System.out.println("--> [ERROR] : Dashboard " + dashboardName + " does not exist" + "\t\t (" + System.currentTimeMillis() + " )");}
	}
	
	private static Visualization locateVisualization(String name, Dashboard d){
		for(Visualization v:d.getVisualizations())
			if(v.getName().equalsIgnoreCase(name))
				return v;
		return null;
	}

	static { // register the language
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> packageRegistry = reg.getExtensionToFactoryMap();
		packageRegistry.put(visualizationDesignLanguage.VisualizationDesignLanguagePackage.eNS_URI,
						visualizationDesignLanguage.VisualizationDesignLanguagePackage.eINSTANCE);

	}

	public static Dashboard loadModel(String name) {
		// load the xmi file
		XMIResource resource = new XMIResourceImpl(
				URI.createURI("resources/" + name + ".xmi"));
		try {
			resource.load(null);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return null;
		}

		// get the root of the model
		Dashboard dash = (Dashboard) resource.getContents().get(0);

		return dash;
	}
	
	public static void validateAndPersist(ValidateAndPersistDashboardMsg msg) throws IOException {
		String fileName = "resources/" + msg.getModelName() + ".xmi";
		File file = new File(fileName);
		Files.deleteIfExists(file.toPath());

		ResourceSet resSet = new ResourceSetImpl();
		resSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
		Resource res = resSet.createResource(URI.createFileURI(fileName));
		res.getContents().add(currents.get(msg.getModelName()));

		try {
			res.save(Collections.EMPTY_MAP);
		} catch (Exception e) {
			System.err.println("ERREUR sauvegarde du modèle : " + e);
			e.printStackTrace();
		}
		validated = true;

	}
	
	public static IsValidatedDashboardAsw isValidated(IsValidatedDashboardMsg msg){
		return new IsValidatedDashboardAsw(validated);
	}
}
