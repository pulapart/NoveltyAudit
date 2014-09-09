package com.Utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class saveImg {

	public static boolean saveImage(String imageUrl, String destinationFile) throws IOException{

		try {
		
		URL url = new URL(imageUrl);
		 HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
		 if (httpcon == null ) return false;
		 httpcon.addRequestProperty("User-Agent", "Mozilla/4.0");
		 InputStream in = httpcon.getInputStream();
		 OutputStream out = new BufferedOutputStream(new FileOutputStream(destinationFile));
		 for ( int i; (i = in.read()) != -1; ) {
		    out.write(i);
		 }
		 in.close();
		 out.close();
		 return true;
		}
		
		catch (Exception e) {
			
			File dir = new File(".");
			String path =  dir.getCanonicalPath();
			
			InputStream inStream = null;
			OutputStream outStream = null;
			
			File afile =new File(path+File.separator+"noimg.jpg");
		    File bfile =new File(destinationFile);

		    inStream = new FileInputStream(afile);
		    outStream = new FileOutputStream(bfile);

		    byte[] buffer = new byte[1024];

		    int length;
		    //copy the file content in bytes 
		    while ((length = inStream.read(buffer)) > 0){

		    	outStream.write(buffer, 0, length);

		    }

		    inStream.close();
		    outStream.close();
		    return false;
		}
	 
	} 

	
	
	
	
	
	
	
}
