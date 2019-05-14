

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class Convert {



	/**
	 * @param args
	 */
	 public Document parse(String path) throws DocumentException {
		 	//parsing xml 
		    SAXReader reader = new SAXReader();
	        File file = new File(path);
	        Document document = reader.read(file);
	        System.out.println("Parsing ...."+file);
	        System.out.println("---------------------------");
	        return document;
	    }
	 @SuppressWarnings("null")
	public void bar(Document document, String path) throws DocumentException, IOException {
		 	
		    
		    // External files upload : blank words, punctuation
		    InputStream ismv = this.getClass().getResourceAsStream("ressources/mots_vides.txt");
		    InputStream isp = this.getClass().getResourceAsStream("ressources/ponctuation.txt");

		    BufferedReader br = new BufferedReader(new InputStreamReader(ismv));
			BufferedReader br1 = new BufferedReader(new InputStreamReader(isp));
			
	        ArrayList<String> motsvide = new ArrayList<String>();
	        ArrayList<String> ponct = new ArrayList<String>();
			String lin;

	        while ((lin = br.readLine()) != null) {
	        	motsvide.add(lin);
	        }

	        br.close();
			while ((lin = br1.readLine()) != null) {
				ponct.add(lin);
			}
			br1.close();
			
			String filename = document.getName();
		 	System.out.println(path);
		 	String[] nom = filename.split("/");
		    Element root = document.getRootElement();
		    
		    // iterate through child elements of root with element name "page"		
	        List<Element> list =  root.elements("page");
	        ArrayList<String []> data = new ArrayList<String[]>();
	        for (Element element : list) {
		        String page = element.attributeValue("number");
	            List<Element> sublist = element.elements("text");
	            for (Element element2 : sublist) {
	            	// saving lines attributes
            		String[] ligne = new String[7];
					ligne[0] = element2.attributeValue("top");
            		ligne[1] = element2.attributeValue("left");
            		ligne[2] = element2.attributeValue("width");
            		ligne[3] = element2.attributeValue("height");
            		ligne[4] = element2.attributeValue("font");
            		ligne[5] = page;
	            	
	            		ligne[6] = element2.getStringValue();
	            		// save lines content into data if not blank
	            		if( !ligne[6].equals("") && !ligne[6].equals("  ") && !ligne[6].equals(" ") && !ligne[6].equals("      "))
	    	        		
		        		{
	            	
	            	data.add(ligne);
		        		}
	            }
		    }
	        
	        
    		String[] line = new String[6];

    		//creating an empty bloc 
	        ArrayList<String []> block = new ArrayList<String[]>();
	        ArrayList< ArrayList<String []>> result= new ArrayList< ArrayList<String []>>();
	       
	        //reading data
	        for(int i=0; i<data.size(); i++)
	        {	
	        		line = data.get(i);	  
	        			//if the bloc is empty, add next line to bloc
	        			if(block.isEmpty())        		        		
	        			{	
	        				block.add(line);
	        			}
	        			//if the bloc contains one line
	        			else if (block.size() == 1)
	        			{
	        				
	        				String[] temp = new String[6];
	        				String[] temps = line[6].split(" ");
	        				//fist word of the line
	        				temp = block.get(0);
	        				// the gap between current line and previous line
	        				int gap = Math.abs(Integer.valueOf(line[0]) - Integer.valueOf(temp[0]));
	        				//verifies if the current line is not equal or substring to previous line 
	        				if(!temp[6].equals(line[6]) && !temp[6].startsWith(line[6]) && !temp[6].endsWith(line[6]) && !line[6].startsWith(temp[6]) && !line[6].endsWith(temp[6]) && temps.length > 0)
	        				  {
		        				//if heights (line[3]) or fonts (line[4]) on current and previous lines are different or a large gap
	        					//then add bloc to results, and start a new one with the current line
	        					if( (!temp[3].equals(line[3]) && temp[6].length()>1) || (!temp[4].equals(line[4]) && temp[6].length()>1) || gap > 35)
	        					{
	        						result.add(new ArrayList<String[]>(block));
	        						block.clear();
	        						block.add(line);
	        	        		}
	        					//else add line to bloc
	        					else
	        					{
	        						block.add(line);
	        					}
	        				}
	        		
	        			}
	        			//if the bloc contains more than two lines
	        			else if(block.size() > 1)
	        			{
	        				String[] temp1 = new String[6];
	        				String[] temp2 = new String[6];
	        				//System.out.println(block.size());

	        				temp1 = block.get(block.size()-2);
	        				temp2 = block.get(block.size()-1);
	        				
	        				String[] temps = line[6].split(" ");
	        				String[] temps1 = temp2[6].split(" ");
	        				//calculating gap between last two existing lines in the bloc
	        				int currentgap = Math.abs(Integer.valueOf(temp2[0]) - Integer.valueOf(temp1[0]));
	        				//calculating gap between current line and the last line of the bloc
	        				int newgap = Math.abs(Integer.valueOf(line[0]) - Integer.valueOf(temp2[0]));
	        				
	        				//verifies if the current line is not equal or substring to previous line 
	        				if(!temp2[6].equals(line[6]) && !temp2[6].startsWith(line[6]) && !temp2[6].endsWith(line[6]) && !line[6].startsWith(temp2[6]) && !line[6].endsWith(temp2[6]) && temps.length > 0 && temps[0].length() > 0 )
	        				  {	
	        					//if (heights (line[3]) or fonts (line[4]) on current and previous lines are equal) and (having equal left (line[1]) or gap=0 (in the same line))
	        					if( ( (temp2[3].equals(line[3]) || temp2[4].equals(line[4])) && (temp2[1].equals(line[1]) || currentgap == 0 || (!temp2[1].equals(line[1]) && temp2[0].equals(line[0])))) || line[6].equals(".") || line[6].equals("ème") || temp2[6].equals("ème") || temp1[6].equals("ème") || motsvide.contains(temps[0]) || motsvide.contains(temps1[temps1.length-1]) || ponct.contains(Character.toString(line[6].charAt(0))) )
	        					{
	        						// if currentgap=newgap +- 2 or currentgap=0 or newgap=0 or the string ("ème") or a blank word/punctuation in the last token of the bloc 
	        						if( currentgap == newgap || Math.abs(currentgap - newgap) == 1 || Math.abs(currentgap - newgap) == 2 || currentgap == 0 || newgap == 0 || (newgap == 1 && line[6].equals(".")) || line[6].equals("ème") || temp2[6].equals("ème") || temp1[6].equals("ème") || motsvide.contains(temps[0]) || motsvide.contains(temps1[temps1.length-1])|| ponct.contains(Character.toString(line[6].charAt(0))))
	        						{
	        							//add line to bloc
	        							block.add(line);

	        						}
	        						//else if newgap>currentgap and being in the same page(line[5])
	        						else if(newgap > 1 && currentgap > newgap && temp2[5].equals(line[5]) )
	        						{
	        							//remove last line of the bloc
	        							block.remove(block.size()-1);
	        							//add bloc to results
	        							result.add(new ArrayList<String[]>(block));

	        							// clear bloc, add last removed line, add the newline
	        							block.clear();
	        							block.add(temp2);
	        							block.add(line);
	        						}
	        						// else : newgap<currentgap 
	        						else
	        						{
	        							//add bloc to results
	        							result.add(new ArrayList<String[]>(block));
	        							// clear bloc, add the newline
	        							block.clear();
	        							block.add(line);
	        							//System.out.println(block.get(0)[6]);
	        						}
	        		
	        					}
	        					// else :(heights (line[3]) or fonts (line[4]) on current and previous lines are different) and/or (having different left (line[1]) or gap is not null)

	        					else 	{

        							//add bloc to results
	        						result.add(new ArrayList<String[]>(block));
        							// clear bloc, add the newline
	        						block.clear();
	        						block.add(line);

	        			
	        					}
	        				  }
	        			}

	        		
	        }
	       

	        //creating output xml model
	        Document newdocument = DocumentHelper.createDocument();
	        Element newroot = newdocument.addElement( "pdf2xml" ); 
	        newroot.addAttribute("producer", "Tayeb Ghorfi");
			for(int i=0; i<result.size(); i++)
	        {   
				Element bloc = newroot.addElement("bloc");
				Integer sommeheight = 0;
				Integer maxwidth = 0;
				Integer mintop = 10000000;
				Integer minleft = 10000000;
				Integer j=0;
				 for(j=0; j<result.get(i).size(); j++)
			        {
					 // add attributes
					  Element txt = bloc.addElement("text");
					  txt.addAttribute("top", result.get(i).get(j)[0]);
					  txt.addAttribute("left", result.get(i).get(j)[1]);
					  txt.addAttribute("width", result.get(i).get(j)[2]);
					  txt.addAttribute("height", result.get(i).get(j)[3]);
					  txt.addAttribute("font", result.get(i).get(j)[4]);
					  txt.addText(result.get(i).get(j)[6]);
					// getting attribute values
					  sommeheight = sommeheight + Integer.valueOf(result.get(i).get(j)[3]);
					  if(Integer.valueOf(result.get(i).get(j)[2]) > maxwidth )
						  maxwidth = Integer.valueOf(result.get(i).get(j)[2]);
					  
					  if(Integer.valueOf(result.get(i).get(j)[0]) < mintop )
						  mintop = Integer.valueOf(result.get(i).get(j)[0]);
					  
					  if(Integer.valueOf(result.get(i).get(j)[1]) < minleft )
						  minleft = Integer.valueOf(result.get(i).get(j)[1]);

			        }
				 bloc.addAttribute("page", result.get(i).get(0)[5]);
				 bloc.addAttribute("top", mintop.toString());
				 bloc.addAttribute("left", minleft.toString());
				 bloc.addAttribute("height", result.get(i).get(0)[3]);
				 bloc.addAttribute("width", maxwidth.toString());
				 bloc.addAttribute("font", result.get(i).get(0)[4]);
				 bloc.addAttribute("lignes", j.toString());


	        }
			// writing output file
			OutputFormat format = OutputFormat.createPrettyPrint();
	         XMLWriter writer;
	         if(!new File(path+"/out/").exists())
	         {
	             new File(path+"/out/").mkdirs();
	         }
	         FileOutputStream fos = new FileOutputStream(path+"/out/"+nom[nom.length-1]);
	         //writer = new XMLWriter( System.out, format );
	         writer = new XMLWriter( fos, format );

	         writer.write( newdocument );
	        			
	}
	 //fixe errors on pdftohtml output
	 public String fixml (String dossier) throws IOException
	 {
		 //creating output folder for fixed xml
		 if(!new File(dossier+"/fixed/").exists())
         {
             new File(dossier+"/fixed/").mkdirs();
         }
		 String dossier2 = dossier+"/fixed";

		 File repertoire = new File(dossier);
	     String liste[] = repertoire.list();      
	     	
	     	//iterating through file lines and replace errors
	        if (liste != null) {         
	            for (int i = 0; i < liste.length; i++) {
	            	if(liste[i].endsWith(".xml"))
	            	
	            	{
	            	BufferedReader in=new BufferedReader(new FileReader(dossier+"/"+liste[i]));	
	                BufferedWriter out = new BufferedWriter(new FileWriter(dossier2+"/"+liste[i]));

	    			String ligne;
	    			
						while ((ligne=in.readLine())!=null){
							
							ligne=ligne.replace("", " ");
							ligne=ligne.replace("", " ");
							ligne=ligne.replace("", " ");
							ligne=ligne.replace("", " ");
							ligne=ligne.replace("’", "'");
							ligne=ligne.replace("r</i> </a>", ">r</a></i> ");

							ligne=ligne.replace("", " ");
							ligne=ligne.replace("", " ");
							ligne=ligne.replace("<b>Charançons du bourgeon terminal<i> </b>", "<b>Charançons du bourgeon terminal</b><i> ");
							ligne=ligne.replace("<b>REPRODUCTION INTEGRALE DE CE BULLETIN AUTORISÉE<a href=\"http://www.limousin.synagri.com/\"> - </b>", "<b>REPRODUCTION INTEGRALE DE CE BULLETIN AUTORISÉE - </b><a href=\"http://www.limousin.synagri.com/\">");
							ligne=ligne.replace("<b>REPRODUCTION INTEGRALE DE CE BULLETIN AUTORISÉE <a href=\"http://www.limousin.synagri.com/\">- </b>", "<b>REPRODUCTION INTEGRALE DE CE BULLETIN AUTORISÉE - </b><a href=\"http://www.limousin.synagri.com/\">");
							ligne=ligne.replace("<a href=\"mailto:Philippe.penichou@safran87.fr\">SÉE - </b><i>Reproduction partielle aut</i></a>", "<a href=\"mailto:Philippe.penichou@safran87.fr\">SÉE - </a></b><i>Reproduction partielle aut</i>");
							ligne=ligne.replace("<a href=\"mailto:Philippe.penichou@safran87.fr\">SÉE - </b><i>Reproduction partielle autori</i></a>", "<a href=\"mailto:Philippe.penichou@safran87.fr\">SÉE - </a></b><i>Reproduction partielle autori</i>");

							ligne=ligne.replace("<b>Ascochytose (= anthracnose) de la féverole <i>(</b>", "<b>Ascochytose (= anthracnose) de la féverole </b><i>(");
							ligne=ligne.replace("<b>la féverole <i>(</b>", "<b>la féverole </b><i>(");
							ligne=ligne.replace("<i>Sitobion avenae</b> </i>", "<i>Sitobion avenae</i></b> ");
							ligne=ligne.replace("<i>Rhopalosiphum padi</b> </i>", "<i>Rhopalosiphum padi</i></b> ");
							ligne=ligne.replace("<b>cicadelle <i>Psamotettix alienus</b> </i>", "<b>cicadelle <i>Psamotettix alienus</i></b> ");
							ligne=ligne.replace("<b>cicadelle  <i>Psamotettix  alienus</b>  </i>", "<b>cicadelle  <i>Psamotettix  alienus</i></b>  ");

							ligne=ligne.replace("<b> Ascochytose (= anthracnose) de la féverole <i>(</b>Aschochyta fabae)</i>", "<b> Ascochytose (= anthracnose) de la féverole </b><i>(Aschochyta fabae)</i>");
							ligne=ligne.replace("<b>Mineuse, <i>Lyriomyza cepae</b> :</i>", "<b>Mineuse, <i>Lyriomyza cepae</i></b> :");
							ligne=ligne.replace("<b>COLZA<i> </b>page 5</i>", "<b>COLZA</b><i> page 5</i>");
							ligne=ligne.replace("<b>Punaise verte<i> </b>", "<b>Punaise verte </b><i>");
							ligne=ligne.replace("<i>www.bsv-reunion.fr</i></b></a>", "<i>www.bsv-reunion.fr</i></a></b>");
							
							// write output file
			                out.write(ligne);
			                out.write("\n");							
						}
					
	    			in.close();
	                out.close();
	            }
	            }
	            System.out.println("end");
	            
	        }
	        return dossier2;
	}

	 
	public static void main(String[] args) throws DocumentException, IOException {
		// TODO Auto-generated method stub
		String dossier = args[0];
		
		// reading input folder
		File repertoire = new File(dossier);
        String liste[] = repertoire.list();      
        // converting files to xml with pdftohtml
        if (liste != null) {         
            for (int i = 0; i < liste.length; i++) {
                System.out.println(liste[i]);
                String[] filename = liste[i].split(".pdf");
                String command = "pdftohtml -xml -i " + dossier + "/" + liste[i] ;
                try {
					Process proc = Runtime.getRuntime().exec(command);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        } else {
            System.err.println("Nom de repertoire invalide");
        }
        
		Convert test=new Convert();
		// reparing xml files
		String dossier2=test.fixml(dossier);
		
		File repertoireout = new File(dossier2);
        String listeout[] = repertoireout.list();      
        
        // reading xml files
        if (listeout != null) {         
            for (int i = 0; i < listeout.length; i++) {
            		if(listeout[i].endsWith(".xml"))
            		{
            			System.out.println(i);
            			// treating xml files
            			Document doc = test.parse(dossier2+"/"+listeout[i]);
            			test.bar(doc, dossier2);
            		}
            }
            		
       }
	}

}
