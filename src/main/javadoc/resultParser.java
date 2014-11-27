import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class XmlParser
{
	private static Logger logger = Logger.getLogger(XmlParser.class);
	
	private static final String STATUS = "FAIL";
	
	private static final String filePath = "/dir/isoTestResponse.xml"; // Path to the Response XML
	
	public static void main(String[] args)
    {
	    
        try
        {
        	File xmlFile = new File(filePath);
    	    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    		DocumentBuilder dBuilder;
	        dBuilder = dbFactory.newDocumentBuilder();
	        Document doc = dBuilder.parse(xmlFile);
	        logger.debug("Document path: " + filePath);
	        
	        logger.debug("Root element :" + doc.getDocumentElement().getNodeName());
	        
	        NodeList nodeList = doc.getElementsByTagName("test-method");
	        
	        printFailedTests(nodeList);
	        
        }
        catch (ParserConfigurationException e)
        {
	       logger.error("Unable to parse Configuration at " + filePath, e);
        }
        catch (SAXException e)
        {
        	logger.error("Unable to parse Configuration at " + filePath, e);
        }
        catch (IOException e)
        {
        	logger.error("Unable to Read file at " + filePath, e);
        }
    }
	
	/**
	 * Get only Failed Tests
	 * @param nodeList
	 */
	public static void printFailedTests(NodeList nodeList)
	{
		if(nodeList != null)
		{
			for (int tmpNode = 0; tmpNode < nodeList.getLength(); tmpNode++) {
				 
				Node nNode = nodeList.item(tmpNode);
		 
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		 
					Element nodeElement = (Element) nNode;
					
					if(nodeElement.getAttribute("status") != null && STATUS.equals(nodeElement.getAttribute("status").trim()))
					{
						logger.debug("______________________________");
						logger.debug("Name : " + nodeElement.getAttribute("name")); // Print Failed Test Name
						logger.debug("Description : " + nodeElement.getAttribute("description")); // Print Failed Test Description
						if(nodeElement.hasChildNodes())
						{
							String testMessage = getMessage(nodeElement.getChildNodes());
							logger.debug("Reason for failure : " + testMessage); // Print Reason why test failed
						}
					}
				}
			}
		}
		else
		{
			logger.error("Unable to get Failed Tests as NodeList was null.");;
		}
	}
	
	/**
	 * Get Exception Message
	 * @param nodeList
	 * @return
	 */
	public static String getMessage(NodeList nodeList)
	{
		if(nodeList != null)
		{
			for (int count = 0; count < nodeList.getLength(); count++)
	2014a		{
				Node tempNode = nodeList.item(count);
				
				if (tempNode.getNodeType() == Node.ELEMENT_NODE) 
				{
					// get Exception message
					if("exception".equals(tempNode.getNodeName()))
					{
						if(tempNode.hasChildNodes())
						{
							NodeList list = tempNode.getChildNodes();
							for(int i = 0; i < list.getLength(); i++)
							{
								Node messNode = list.item(i);
								if("message".equals(messNode.getNodeName()))
								{
									return(messNode.getTextContent() != null ? messNode.getTextContent().trim() : messNode.getTextContent());
								}
							}
							
						}
					}
				}
			}
		}
		else
		{
			logger.debug("Cannot find Message as nodeLost was null.");
		}
		return null;
	}
	
}
