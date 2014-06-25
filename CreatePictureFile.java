
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//package xmlbase64decoder;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.*;

import java.io.File;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


/**
 *
 * @author saeliux
 */
public class CreatePictureFile {
   
    public static void main(String[] args) throws Exception
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new File("/cs/home/cse73232/tomcat/webapps/FoodsRUs/WEB-INF/catalog.data/Catalog.xml"));
               
                // normalize text representation
                doc.getDocumentElement ().normalize ();
                System.out.println ("Root element of the doc is " +  doc.getDocumentElement().getNodeName());

		//NodeList listOfPictures = doc.getElementsByTagName("/Categories/Picture/");
		NodeList categoryList = doc.getElementsByTagName("Categories");
	        int totalCategories = categoryList.getLength();
	       	for (int i=0; i<totalCategories;i++){
		    Node category =categoryList.item(i);
		    Element categoryElement = (Element)category;
                    NodeList catIDs = categoryElement.getElementsByTagName("CategoryID");
		    
		    NodeList pics = categoryElement.getElementsByTagName("Picture");
		    String picPath = pics.item(0).getFirstChild().getNodeValue();
		    String cID = catIDs.item(0).getFirstChild().getNodeValue();
		    System.out.println("catID = " + cID + " picPath = " + picPath);
		    decodePic(picPath, "Pictures/"+cID +".jpg");
		}       
               
    }
       
        private static void decodePic(String decodePath, String filename)
        {
                String base64 =decodePath;
                //decode the content
                try {
                    System.out.println("decoding to " + filename + ".jpg");
                    byte decoded[] = new sun.misc.BASE64Decoder().decodeBuffer(base64);
                    FileOutputStream file = new FileOutputStream(filename);
                    for (int i = 78; i < decoded.length; i++)
                    {
                        file.write(decoded[i]);
                    }
                   
                    file.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
       
        }
   

}
