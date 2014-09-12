package com.getDetails;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.Utils.sendHttpGet;



public class getAttributes {
	
	
public static void main(String[] args) {
	
//A10XUP92X2DR79
	String merchid = "AH0MUUSMNZ1IF";		
	String mpid = "UK" ;// titlekeywords = "", genkeywords ="";
	//int size = 600;
		
	try {		
		String[] top10 = fetchSampleasins(merchid, mpid, "", "", "", "1", 200);
		
		String[][] tp = fetchUrls(top10, "UK");
		
		for (int i = 0; i < tp.length; i++) {
			System.out.println(tp[i][1]);
		}
					
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
}	
	


public static String[] topNBrands(String merchid, String mpid , int n) throws Exception {
	
	
	try {
		String qmpid = "", lang="", site = ""; 
		
	    if (mpid.equals("US")) { lang = "en_US"; site = "www.amazon.com"; qmpid = "ATVPDKIKX0DER"; } 
	    else if (mpid.equals("UK")) { lang = "en_GB" ; site = "www.amazon.co.uk" ; qmpid = "A1F83G8C2ARO7P" ;}
	    else if (mpid.equals("CA")) { lang = "en_CA" ; site = "www.amazon.ca" ;  qmpid = "A2EUQ1WTGCTBG2";}
	    else if (mpid.equals("DE")) { lang = "de_DE" ; site = "www.amazon.de" ;  qmpid = "A1PA6795UKMFR9";}
	    else if (mpid.equals("FR")) { lang = "fr_FR" ; site = "www.amazon.fr";  qmpid = "A13V1IB3VIYZZH";}
	    else if (mpid.equals("ES")) { lang = "es_ES" ; site = "www.amazon.es" ;  qmpid = "A1RKKUPIHCS9HS";}
	    else if (mpid.equals("JP")) { lang = "ja_JP" ; site = "www.amazon.co.jp" ;  qmpid = "A1VC38T7YXB528";}
	    else if (mpid.equals("IT")) { lang = "it_IT" ; site = "www.amazon.it" ;  qmpid = "APJ6JRA9NG5V4";}
	        
	    String getasinquery = "http://"+site+"/gp/internal/search/rsas.html?api=refinementpage&query-searchAlias=&query-node=&query-backbone-node=&query-keywords=&query-merchant-id="
	    		+ merchid + "&release-ids=&query-marketplace-id="+qmpid+"&query-marketplace-id-override=&refinement=brandtextbin&refinementValuesStartWith=&size=&language="+lang+ "&products_check=1"
	    				+ "&query-field-1=&query-value-1=&query-field-2=&query-value-2=&query-field-3=&query-value-3=&query-field-4=&query-value-4=&weblab-overrides=&search=Search%21";
	    
	    StringBuilder brandstr = new StringBuilder();
	 	brandstr.append("");
	 	StringBuilder countstr = new StringBuilder();
	 	countstr.append("");
	 	String brands = sendHttpGet.sendGet(getasinquery);
	 	Document brandsdoc = Jsoup.parse(brands);
	 	Elements table = brandsdoc.select("table"); 	
	 	Element tb = table.get(6);
	 	Element tb2 = tb.select("table").get(2);
	 	Elements tb3 = tb2.select("tr");
	 	Elements row = tb3.select("tr");
	 	
	 	TreeMap<Integer, String> map = new TreeMap<Integer, String>();
	 	

	 	
	 	Integer count = 0 ;
	 	String origcnt = ""; String fulbrand = ""; String temp = "";
	 	for (int i = 1; i < (row.size()-1); i++) {
	 		count = Integer.parseInt(row.get(i).select("td").get(2).text());
	 		
	 		origcnt = row.get(i).select("td").get(2).text();
	 		fulbrand = row.get(i).select("td").get(1).text() + "\t" + origcnt ;
	 		
	 		if (map.containsKey(count)) {
	 			
	 			temp = map.get(count) + "\t\t" + fulbrand ; 
	 			
	 			map.remove(count);
	 			map.put(count, temp);			
	 		
	 		} else 	map.put(count,fulbrand);
	 	
	 	}
	 
	 	//Sorts brands descending order
	 	TreeMap<Integer, String> revmap = new TreeMap<Integer, String>(Collections.reverseOrder());
	 	revmap.putAll(map);
	 	String[] topbrands = new String[n];
	 	int c = 0 ;
	 	for (Map.Entry<Integer, String> entry : revmap.entrySet()) {
			
	 		if (c == n) break ;
	 		
	 		topbrands[c] = (entry.getValue()); 
	 
	 		c++;		
		}
	 	
	 	String[] finalbrands = new String[n];
	 	
	 	int fincnt = 0 ;
	 	
	 OUT:	for (int i = 0; i < topbrands.length; i++) {
	 		if (topbrands[i] == null) break OUT;
	 		
	 		String[] t1 = topbrands[i].split("\t\t");	
	 		for (int j = 0; j < t1.length; j++) { 			
	 		if (fincnt == n) break OUT;	
	 			finalbrands[fincnt] = t1[j];
	 			fincnt++;
			} 		
		}	 	 	
 		return finalbrands;	  	
	}
	catch (Exception e) {
		
		e.printStackTrace();

		return null;	
	}
	
}

public static String[] fetchSampleasins(String merchid, String mpid, String titlekeywords, String genkeywords, String brand, String page , int size) throws Exception {
	
	
	System.out.println("Merchant ID input : => " + merchid  + " the marketplace input : " + mpid);
	
		
	if (size > 600 ) size = 600 ;
		
	try {
		String qmpid = "", lang="", site = ""; 
		
	    if (mpid.equals("US")) { lang = "en_US"; site = "www.amazon.com"; qmpid = "ATVPDKIKX0DER"; } 
	    else if (mpid.equals("UK")) { lang = "en_GB" ; site = "www.amazon.co.uk" ; qmpid = "A1F83G8C2ARO7P" ;}
	    else if (mpid.equals("CA")) { lang = "en_CA" ; site = "www.amazon.ca" ;  qmpid = "A2EUQ1WTGCTBG2";}
	    else if (mpid.equals("DE")) { lang = "de_DE" ; site = "www.amazon.de" ;  qmpid = "A1PA6795UKMFR9";}
	    else if (mpid.equals("FR")) { lang = "fr_FR" ; site = "www.amazon.fr";  qmpid = "A13V1IB3VIYZZH";}
	    else if (mpid.equals("ES")) { lang = "es_ES" ; site = "www.amazon.es" ;  qmpid = "A1RKKUPIHCS9HS";}
	    else if (mpid.equals("JP")) { lang = "ja_JP" ; site = "www.amazon.co.jp" ;  qmpid = "A1VC38T7YXB528";}
	    else if (mpid.equals("IT")) { lang = "it_IT" ; site = "www.amazon.it" ;  qmpid = "APJ6JRA9NG5V4";}
	    
	    //int x = randInt(0, 10);
	    //popularity-rank
	    String sort = "" ;
	        
	    String getasinquery = "http://"+site+"/gp/internal/search/rsas.html?api=basic&query-searchAlias=&query-node=&query-backbone-node=&query-keywords="+genkeywords+"&query-merchant-id="+merchid+
			"&release-ids=&query-marketplace-id="+qmpid+"&query-marketplace-id-override=&query-store=&page="+page+"&size="+size+"&sort="+sort+"&product-flavor=&language="+lang+"&eliminate-variation="+
			"unspecified&products_check=1&query-field-1=search-alias&query-value-1=merchant-items&query-field-2=field-title&query-value-2="+titlekeywords+"&query-field-3=brandtextbin&query-value-3="+brand+"&query-field-4="+
			"&query-value-4=&metadataNames=&weblab-overrides=&explicitRefinements=&flag-field-1=&flag-value-1=&flag-field-2=&flag-value-2=&flag-field-3=&flag-value-3=&flag-field-4="+
			"&flag-value-4=&search=Search!";
	 
	 	String asins = sendHttpGet.sendGet(getasinquery);
	 	Document asindoc = Jsoup.parse(asins);
	 	String listofasin = "";
	 	Element table = asindoc.select("table").get(7);
	 	for( Element element : table.children() )
	 	{
	 	    listofasin = element.text().replace("ASIN Title Image All Product Data Metadata ", "");
	 	}
	 	 
	 	listofasin = listofasin.replace("ASIN Title Image All Product Data Metadata","");
	 	
		if ( listofasin.equals("")) return null;
	 	
	 	String[] sampleasins = listofasin.split(" ");
	 	
	  	
 		return sampleasins;	  	
	}
	catch (Exception e) {
		return null;	
	}
	
}	
	


public static String[][] fetchUrls(String[] asins, String mplace) throws Exception {
	
	StringBuilder asin = new StringBuilder();
	
	for (int i = 0; i < asins.length; i++) {
		asin.append(asins[i]);
		asin.append(",");		
	}
	
	
	String site = "";
	
	if (mplace.equals("US")) { site = "www.amazon.com";  } 
    else if (mplace.equals("UK")) { site = "www.amazon.co.uk" ; }
    else if (mplace.equals("CA")) { site = "www.amazon.ca" ;  }
    else if (mplace.equals("DE")) { site = "www.amazon.de" ; }
    else if (mplace.equals("FR")) { site = "www.amazon.fr"; }
    else if (mplace.equals("ES")) { site = "www.amazon.es" ; }
    else if (mplace.equals("JP")) { site = "www.amazon.co.jp" ; }
    else if (mplace.equals("IT")) { site = "www.amazon.it" ;}

	
	String key = asin.toString();
	
	key = key.substring(0, key.length() - 1);
	
	String query = "http://"+site+"/gp/internal/aggregators/pas-test.html?ASINs="+key+"&facets=Description:Low,Media:Low&requestID=&username=&fakePassword=&selectors=";
	String[][] attribs = new String[asins.length][2];
	
		try {
			String physids = sendHttpGet.sendGet(query);
			Document imgdoc = Jsoup.parse(physids);			
			Elements title = imgdoc.getElementsByTag("pre");
			String[] temp , temp2;
			String titleion = "";
			int j = 0;
			for (Element ttl : title) {
			
				if (ttl.text().contains("title")) {						
					titleion = ttl.text();
					temp =  titleion.split("'title' => ");
					String tempstr = "";
					try {
						temp2 = temp[1].split("',");
						tempstr = temp2[0];
						tempstr = tempstr.substring(1);
						
						if (tempstr.contains("\",          '")) {
							String[] t2 = tempstr.split("\",          '");
							tempstr = t2[0].toString();
							tempstr = tempstr.replace("_x{", "&#x").replace("}", ";").replace("?", "");
							
						}
						
						if (tempstr.startsWith("\\")) tempstr = tempstr.substring(1) ;  
					} catch (Exception e) {
						tempstr = "couldn't fetch title";
					}
					attribs[j][0] = tempstr;	
					j++;
				}
			
			}
			
			Elements img = imgdoc.getElementsByTag("img");
		
			int i = 0 ;
			for (Element el : img) {
			String src = el.absUrl("src");
			if (src != null) {
				attribs[i][1] = src;
				}
			i++;
			}
					
		} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		return attribs;
}


public static int randInt(int min, int max) {

    // Usually this can be a field rather than a method variable
    Random rand = new Random();

    // nextInt is normally exclusive of the top value,
    // so add 1 to make it inclusive
    int randomNum = rand.nextInt((max - min) + 1) + min;

    return randomNum;
}





}
