package edu.memphis.ccrg.lida.framework.serialization;

import java.io.File;
import java.util.List;
import java.util.Scanner;
import java.io.FileWriter;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import com.thoughtworks.xstream.XStream;

public class Serializer{
	
	XStream xstream = new XStream();//XStream XML serializer
	
	public static void main(String[] args){
		//List of two objects to be serialized
		DummyClass d1 = new DummyClass(77);
		DummyClass d2 = new DummyClass(33);
		List<Object> objs = new ArrayList<Object>();
		objs.add(d1);
		objs.add(d2);
		
		//Serialize, then deserialize
		Serializer test = new Serializer();
		String outputFileName = "./inputFiles/Dummy.txt";
		test.serialize(objs, "dummy_", DummyClass.class, outputFileName);
		List<Object> res = test.deserialize(outputFileName);
		
		//Print results
		for(Object o: res)
			System.out.println(o.toString());
	}//method
	
	/**
	 * @param objectList - Objects to be serialized to xml
	 * @param objectNamePrefix - Prefix for objects' xml tag e.g. "node"
	 * @param objectClass - Class of the objects
	 * @param outputFileName - File path for the output
	 * 
	 * Note: XStream is picky about the objectNamePrefix, 
	 * rather, try strings of this form: "string1" and not obj.toString()
	 * 
	 * Note: Only 1 object type at a time, all object in 'objectList' 
	 * must be of type 'objectClass'
	 */
	@SuppressWarnings("unchecked")
	private void serialize(List<Object> objectList, String objectNamePrefix, Class objectClass, String outputFileName){
		try{
			BufferedWriter out = new BufferedWriter(new FileWriter(outputFileName));
			int numObjects = objectList.size();
			for(int i = 0; i < numObjects; i++){
				xstream.alias(objectNamePrefix + i, objectClass);
				String s = xstream.toXML(objectList.get(i));
				out.write(s);
		        out.write("\n*\n");
			}//for
	        out.close();
	    }catch(Exception e){}
	}//method

	/**
	 * Deserializes an xml file and returns the objects in a list. 
	 * 
	 * @param inputFileName
	 * @return list of objects
	 */
	private List<Object> deserialize(String inputFileName) {
		List<Object> result = new ArrayList<Object>();
		Scanner sc;
		try{
			sc = new Scanner(new File(inputFileName));
			String objectString = new String();
			while(sc.hasNext()){//while the file has tokens
				String token = sc.next();
				while(!token.equals("*")){//until the next delimiter "*"
					objectString += token;
					if(sc.hasNext())
						token = sc.next();
					else
						break;
				}//while				
			    result.add(xstream.fromXML(objectString));
				objectString = new String();
			}//while
		}catch (FileNotFoundException e){
			e.printStackTrace();
		}
		return result;
	}//method

}//class