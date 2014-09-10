package com.getDetails;
import java.io.IOException;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.Utils.sendHttpGet;



public class getAttributes {
	
	
public static void main(String[] args) {
	

	String merchid = "A1Z14AUB2ZPJ4R";		
	String mpid = "JP" , titlekeywords = "", genkeywords ="";
	int size = 40;
		
	try {
		String[] sample = fetchSampleasins(merchid, mpid, titlekeywords, genkeywords,  size);
	
		if (sample == null) System.out.println("is null");
		
		for (int i = 0; i < sample.length; i++) {
			
			System.out.println(sample[i]);
					}
		
		String[][] urls =  fetchUrls(sample, mpid);
		
		for (int i = 0; i < (urls.length); i++) {
			System.out.println(i +" ---->>> "+urls[i][0]);
		}
			
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
}	
	
	
public static String[] fetchSampleasins(String merchid, String mpid, String titlekeywords, String genkeywords ,int size) throws Exception {
		
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
	    int x = randInt(0, 10);
	    
	    
	    String sort = String.valueOf(x) ;
	        
	    String getasinquery = "http://"+site+"/gp/internal/search/rsas.html?api=basic&query-searchAlias=&query-node=&query-backbone-node=&query-keywords="+genkeywords+"&query-merchant-id="+merchid+
			"&release-ids=&query-marketplace-id="+qmpid+"&query-marketplace-id-override=&query-store=&page=&size="+size+"&sort="+sort+"&product-flavor=&language="+lang+"&eliminate-variation="+
			"unspecified&products_check=1&query-field-1=search-alias&query-value-1=merchant-items&query-field-2=field-title&query-value-2="+titlekeywords+"&query-field-3=&query-value-3=&query-field-4="+
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
 //	String[] imgurls = new String[asins.length];
		try {
			String physids = sendHttpGet.sendGet(query);
			
			Document imgdoc = Jsoup.parse(physids);
			
			Elements title = imgdoc.getElementsByTag("pre");
		
		//	String[] titles = new String[asins.length];
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
						//	System.out.println(tempstr.replace("_x{", "&#x").replace("}", ";"));
							tempstr = tempstr.replace("_x{", "&#x").replace("}", ";").replace("?", "");
							
						}
						
						if (tempstr.startsWith("\\")) tempstr = tempstr.substring(1) ;  
					} catch (Exception e) {
						tempstr = "couldn't fetch title";
					}
							
				//	titles[j] = temp2[0];
					
					attribs[j][0] = tempstr;
					
					j++;
				}
			
			}
			
			Elements img = imgdoc.getElementsByTag("img");
		
			int i = 0 ;
			for (Element el : img) {
			String src = el.absUrl("src");
			if (src != null) {
			//	imgurls[i] = src ;
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
