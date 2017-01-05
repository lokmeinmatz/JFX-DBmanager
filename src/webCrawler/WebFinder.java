package webCrawler;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class WebFinder {
	
	
	//not working function

	
	
	public static Image findImage(String title, String maindir){
		title = replaceUmlaut(title);
		System.out.println("Searching for "+title);
		String searchstring = title.replaceAll(" ", "+");
		
		File thumbnaildir = new File(maindir+"/res/thumbnails/");
		File[] thumbnails = thumbnaildir.listFiles();
		String filename = title.toLowerCase().replaceAll(" ", "");
		System.out.println("Searching for "+filename);
		for(File f:thumbnails){
			String fname = f.getName().replace(".png", "");
			System.out.println(f.getAbsoluteFile().getAbsolutePath());
			if(fname.equals(filename)){
				return new Image("file:"+maindir+"/res/thumbnails/"+f.getName());
			}
		}
		try {
			Document doc = Jsoup.connect("https://www.themoviedb.org/search?language=de-DE&query="+searchstring).get();
			Elements imagelinks = doc.getElementsByClass("poster lazyload");
			System.out.println("found "+imagelinks.size());
			if(imagelinks.isEmpty()){
				System.out.println("Not movies found");
				return null;
			}
			for (Element e:imagelinks){
				String imgline = e.attr("data-src");
				System.out.println(imgline);
				Image img = new Image(imgline);
				File outputfile = new File(maindir+"/res/thumbnails/"+filename+".png");
				BufferedImage outimage = SwingFXUtils.fromFXImage(img, null);
				ImageIO.write(outimage, "png", outputfile);
				return img;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return null;
	}
	
	private static String replaceUmlaut(String input) {

	     //replace all lower Umlauts
	     String output = input.replace("�", "ue")
	                          .replace("�", "oe")
	                          .replace("�", "ae")
	                          .replace("�", "ss");

	     //first replace all capital umlaute in a non-capitalized context (e.g. �bung)
	     output = output.replace("�(?=[a-z���� ])", "Ue")
	                    .replace("�(?=[a-z���� ])", "Oe")
	                    .replace("�(?=[a-z���� ])", "Ae");

	     //now replace all the other capital umlaute
	     output = output.replace("�", "UE")
	                    .replace("�", "OE")
	                    .replace("�", "AE");

	     return output;
	 }

}
