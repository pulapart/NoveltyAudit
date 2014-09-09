package com.getDetails;
import java.io.IOException;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.Utils.sendHttpGet;
import com.Utils.sendHttpPost;

public class getAttributes {
	
	
public static void main(String[] args) {
	

	String merchid = "A2P0YMXEJRD7XR";		
	String mpid = "US" ;
	int size = 400;
	
	try {
		String[] sample = fetchSampleasins(merchid, mpid, size);
	
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
	
	
public static String[] fetchSampleasins(String merchid, String mpid, int size) throws Exception {
		
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
	        
	    String getasinquery = "http://"+site+"/gp/internal/search/rsas.html?api=basic&query-searchAlias=&query-node=&query-backbone-node=&query-keywords=&query-merchant-id="+merchid+
			"&release-ids=&query-marketplace-id="+qmpid+"&query-marketplace-id-override=&query-store=&page=&size="+size+"&sort="+sort+"&product-flavor=&language="+lang+"&eliminate-variation="+
			"unspecified&products_check=1&query-field-1=search-alias&query-value-1=merchant-items&query-field-2=&query-value-2=&query-field-3=&query-value-3=&query-field-4="+
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
					temp =  titleion.split("'title' => '");
					String tempstr = "";
					try {
						temp2 = temp[1].split("',");
						tempstr = temp2[0];
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




public static String[] fetchTitles(String[] asins, String mpid) throws Exception {
	
	if (asins == null) return null;

    String mplace = "";

    if (mpid.equals("US")) { mplace = "1" ; } 
    else if (mpid.equals("UK")) { mplace = "3";}
    else if (mpid.equals("CA")) { mplace = "7";}
    else if (mpid.equals("DE")) { mplace = "4";}
    else if (mpid.equals("FR")) { mplace = "5";}
    else if (mpid.equals("IT")) { mplace = "35691";}
    else if (mpid.equals("ES")) { mplace = "44551"; }
    else if (mpid.equals("JP")) { mplace = "6"; }
	

	StringBuilder asin = new StringBuilder();
	
	//asins.member.1.asin=B0018EUDHW&asins.member.1.marketplace=1&asins.member.2.asin=B00IMAGING&asins.member.2.marketplace=1&asins.member.3.asin=B000IMAGES&asins.member.3.marketplace=1
	
	for (int i = 0; i < asins.length; i++) {
		asin.append("&asins.member."+(i+1)+".asin=");
		asin.append(asins[i]);
		asin.append("&asins.member."+(i+1)+".marketplace="+mplace);		
	}
	
	String key = asin.toString();
	
	key = key.substring(1, key.length());
	
	String query = "http://imaging-ext-data-agg-prod.amazon.com/?"+key+"&Operation=getASINInfos&ContentType=JSON";
		
 	String[] titles = new String[asins.length];
		try {
			String titledoc = sendHttpGet.sendGet(query);

			String[] temp = titledoc.split("\"title\":\"");
			
			for (int i = 1; i < temp.length; i++) {
			
				//"}
				String tempres = temp[i];
				
				String[] t2 = tempres.split("\"}");
				
				titles[i-1] = t2[0];	
			}
			
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		return titles;
	
}













public static String getImgid(String asin) throws Exception {
		
		String getimg = "http://media-bridge.amazon.com/MediaBridge/action/GurupaMediaServiceHTTP?method=Query&minimalData=false&maxTransactionDepth=10&type=ProductMediaOU&asin="+asin;
	 	String imgid = "";
			try {
			
				String physids = sendHttpGet.sendGet(getimg);
				Document imgdoc = Jsoup.parse(physids);
				Element img = imgdoc.select("physicalID").get(0);
				imgid = img.text();
			 				
				} catch (IOException e) {
					e.printStackTrace();
				}
			return imgid;
	}



public static String fetchImgUrl (String asin) throws Exception {

	if (asin == null) return null;
	
	 		String imgid = getAttributes.getImgid(asin);
 			String imgurl = "http://ecx.images-amazon.com/images/I/"+imgid+".jpg";
  			return imgurl;	
}




public static String getTitle(String asin, String mplace) {
	
	String json = "{\"allAttrs\": false, "+
			 "\"asins\": \""+ asin +"\", "+
			 "\"returnAttrs\": "+
			 "[\"BIW.title\"], "+
			 "\"user\": \"pulapart\"}";
	
	
	String vip = "" ;
	 if (mplace.equals("US")) { vip ="http://if-us.amazon.com/searchByAsins" ;  } 
	    else if (mplace.equals("UK")) { vip = "http://if-gb.amazon.com/searchByAsins" ; }
	    else if (mplace.equals("CA")) { vip = "http://if-ca.amazon.com/searchByAsins" ; }
	    else if (mplace.equals("DE")) { vip = "http://if-de.amazon.com/searchByAsins" ; }
	    else if (mplace.equals("FR")) { vip = "http://if-fr.amazon.com/searchByAsins" ; }
	     else if (mplace.equals("JP")) { vip = "http://if-jp.amazon.com/searchByAsins" ; }
	 
	String result_str = sendHttpPost.executePost(vip, json);
	
	return result_str;

}


public static String getcsiTitle(String asin, String mplace) {
	
	int mid = 0 ;
	 if (mplace.equals("US")) { mid = 1;  } 
	    else if (mplace.equals("UK")) { mid = 3 ; }
	    else if (mplace.equals("CA")) { mid = 7 ; }
	    else if (mplace.equals("DE")) { mid = 4 ; }
	    else if (mplace.equals("FR")) { mid = 5 ; }
	    else if (mplace.equals("ES")) { mid = 44551 ; }
	    else if (mplace.equals("JP")) { mid = 6 ; }
	    else if (mplace.equals("IT")) { mid = 35691 ; }
	
	
	
	String getimg = "http://csi-api.amazon.com/view?view=simple_product_data_view&item_id=" + asin + 
			 "&marketplace_id="+ mid +"&customer_id=&merchant_id=&sku=&fn_sku=&gcid=&fulfillment_channel_code=&listing_type=purchasable"
			+ "&submission_id=&order_id=&external_id=&search_string=&realm=USAmazon&stage=prod&domain_id=33333&keyword=&submit=Show";
	
 	String title = "";
 	String html = "";
		try {
		
			html = sendHttpGet.sendGet(getimg);
			
			if (html.contains("Invalid ASIN specified:")) throw new Exception("invalid ASIN") ;
				
			Document imgdoc = Jsoup.parse(html);
			Element ttl = imgdoc.select("table").get(17);
			
			imgdoc = Jsoup.parse(ttl.text());
			title = ttl.text();	
			title = title.substring(6);
			return title;
			
		} catch (Exception se) {
				if (se.getMessage().equals("invalid ASIN")) return null;
				return null;
		} 
		
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
