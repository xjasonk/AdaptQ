/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adaptability.resources;

import java.io.File;
import java.text.DecimalFormat;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

/**
 *
 * @author kbotange
 */
public class XMLReader {
     
public static void main(String args[]) {

try {
 
File file = new File("H:\\Eclipse workspace\\flight booking","booking.xml" );

DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
Document doc = dBuilder.parse(file);
doc.getDocumentElement().normalize();
Element root = doc.getDocumentElement(); // gets the root element
bpelxml_traverse_DOM(root);
} catch (Exception ex) {
ex.printStackTrace();
}
}
 
private static void bpelxml_traverse_DOM(Node node)
{
double adaptVal = 0;
double vp;
int ref;
int n = 0;
   
Element e = (Element)node;
ref = Integer.parseInt(e.getAttribute("refval"));
NodeList nodeList = node.getChildNodes();
    for (int i = 0, len = nodeList.getLength(); i < len; i++) {
        if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
            System.out.println(nodeList.item(i).getNodeName());       
            if(nodeList.item(i).getNodeName().equals("sequence")){
                vp = sequence(nodeList.item(i), ref);
                System.out.println(vp);   
                n++;
            }else if(nodeList.item(i).getNodeName().equals("switch")){
                vp = sw(nodeList.item(i), ref);
                System.out.println(vp);
                n++;
            }else{
                Element el = (Element)nodeList.item(i);
                if(el.getAttribute("vp").isEmpty()){
                    vp = 0;
                }else{
                    vp = Double.parseDouble(el.getAttribute("vp")) / ref;
                    n++;
                }                
            System.out.println(vp);
            }
        adaptVal = adaptVal + vp;
        }
    }
    adaptVal = adaptVal/n;
    DecimalFormat f = new DecimalFormat("0.00");
    System.out.println("Adaptability degree: " + f.format(adaptVal));    
}

private static double sequence(Node node, int r){
double adaptVal = 0;
double vp;
int n = 0;
NodeList nodeList = node.getChildNodes();
    for (int i = 0, len = nodeList.getLength(); i < len; i++) {
        if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
            System.out.println(nodeList.item(i).getNodeName());       
            if(nodeList.item(i).getNodeName().equals("sequence")){
                vp = sequence(nodeList.item(i), r);
                System.out.println(vp);   
                n++;
            }else if(nodeList.item(i).getNodeName().equals("switch")){
                vp = sw(nodeList.item(i), r);
                System.out.println(vp);
                n++;                
            }else{
                Element el = (Element)nodeList.item(i);
                if(el.getAttribute("vp").isEmpty()){
                    vp = 0;
                }else{
                    vp = Double.parseDouble(el.getAttribute("vp")) / r;
                    n++;
                }                
            }
        adaptVal = adaptVal + vp;
        }
    }
    adaptVal = adaptVal/n;
return adaptVal; 
}

private static double sw(Node node, int r){
double adaptVal = 0;
double vp;
int n = 0;
NodeList nodeList = node.getChildNodes();
    for (int i = 0, len = nodeList.getLength(); i < len; i++) {
        if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
        n++;
        }    
    }
    for (int i = 0, len = nodeList.getLength(); i < len; i++) {
        if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
            System.out.println(nodeList.item(i).getNodeName());       
            if(nodeList.item(i).getNodeName().equals("sequence")){
                vp = sequence(nodeList.item(i),r) * ((double)1/n);
                System.out.println(vp);                
            }else if(nodeList.item(i).getNodeName().equals("switch")){
                vp = sw(nodeList.item(i),r) * ((double)1/n);
                System.out.println(vp);
            }else{
                Element el = (Element)nodeList.item(i);
                if(el.getAttribute("vp").isEmpty()){
                    vp = 0;
                }else{
                    vp = (Double.parseDouble(el.getAttribute("vp")) / r) * ((double)1/n);
                }                
            }
            adaptVal = adaptVal + vp;
        }
    }
return adaptVal; 
}

}