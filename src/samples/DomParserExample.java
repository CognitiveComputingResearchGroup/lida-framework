package samples;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.ModuleName;

public class DomParserExample {

	// No generics
	List<LidaModule> lidaModules = new ArrayList<LidaModule>();
	Document dom;

	public DomParserExample() {
		// create a list to hold the employee objects
	}

	public void runExample() {

		// parse the xml file and get the dom object
		parseXmlFile();

		// get each employee element and create a Employee object
		parseDocument();

		// Iterate through the list and print the data
//		printData();

	}

	private void parseXmlFile() {
		// get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try {

			// Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			// parse using builder to get DOM representation of the XML file
			dom = db.parse("configs/lida.xml");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (SAXException se) {
			se.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	private void parseDocument() {
		// get the root element
		Element docEle = dom.getDocumentElement();

		
		for(LidaModule lm:getModules(docEle)){
			lidaModules.add(lm);
		}
		List<LidaModule> modules = lidaModules;
		
	}

	/**
	 * @param element
	 */
	private List<LidaModule> getModules(Element element) {
		List<LidaModule>modules =new ArrayList<LidaModule>();
		NodeList nl = element.getElementsByTagName("submodules");
		if (nl != null && nl.getLength() > 0) {
			// get the employee element
			Element modulesElemet = (Element) nl.item(0);
			nl = modulesElemet.getElementsByTagName("module");
			if (nl != null && nl.getLength() > 0) {
				for (int i = 0; i < nl.getLength(); i++) {
					Element moduleElement = (Element) nl.item(i);
					// get the Employee object
					LidaModule module = getModule(moduleElement);

					// add it to list
					modules.add(module);
				}
			}
		}
		return modules;
	}

	private LidaModule getModule(Element moduleElement) {
		LidaModule module = null;
		String className = getTextValue(moduleElement, "class");
		String name = getTextValue(moduleElement, "name");
		ModuleName moduleName = ModuleName.NoModule;
		try{
		 moduleName=Enum.valueOf(ModuleName.class, name);
		}catch (Exception e){
			e.printStackTrace();
			
		}
		try {
			module = (LidaModule) Class.forName(className).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		module.setModuleName(moduleName);
		Properties params = getParams(moduleElement);
		module.init(params);
		for(LidaModule lm:getModules(moduleElement)){
			module.addModule(lm);
		}		
		return module;
				
	}

	private static Properties getParams(Element moduleElement) {
		Properties prop = new Properties();
		NodeList nl = moduleElement.getElementsByTagName("param");
		if (nl != null && nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++) {
				Element param = (Element) nl.item(i);
				String name = getTextValue(param, "name");
				String value = getTextValue(param, "value");
				prop.setProperty(name, value);
			}
		}
		return prop;
	}

	/**
	 * I take an employee element and read the values in, create an Employee
	 * object and return it
	 * 
	 * @param empEl
	 * @return
	 */
//	private Employee getEmployee(Element empEl) {
//
//		// for each <employee> element get text or int values of
//		// name ,id, age and name
//		String name = getTextValue(empEl, "Name");
//		int id = getIntValue(empEl, "Id");
//		int age = getIntValue(empEl, "Age");
//
//		String type = empEl.getAttribute("type");
//
//		// Create a new Employee with the value read from the xml nodes
//		Employee e = new Employee(name, id, age, type);
//
//		return e;
//	}

	/**
	 * I take a xml element and the tag name, look for the tag and get the text
	 * content i.e for <employee><name>John</name></employee> xml snippet if the
	 * Element points to employee node and tagName is name I will return John
	 * 
	 * @param ele
	 * @param tagName
	 * @return
	 */
	public static String getTextValue(Element ele, String tagName) {
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if (nl != null && nl.getLength() > 0) {
			Element el = (Element) nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}

		return textVal;
	}

	/**
	 * Calls getTextValue and returns a int value
	 * 
	 * @param ele
	 * @param tagName
	 * @return
	 */
	public static int getIntValue(Element ele, String tagName) {
		// in production application you would catch the exception
		return Integer.parseInt(getTextValue(ele, tagName));
	}

//	/**
//	 * Iterate through the list and print the content to console
//	 */
//	private void printData() {
//
//		System.out.println("No of Employees '" + myEmpls.size() + "'.");
//
//		Iterator it = myEmpls.iterator();
//		while (it.hasNext()) {
//			System.out.println(it.next().toString());
//		}
//	}

	public static void main(String[] args) {
		// create an instance
		DomParserExample dpe = new DomParserExample();

		// call run example
		dpe.runExample();
	}

}
