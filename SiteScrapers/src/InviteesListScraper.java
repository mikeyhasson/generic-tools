import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;


public class InviteesListScraper {
	final static String PREF ="https://private.b144.co.il/PrivateResults.aspx?&p_name=";
	
	public static void main (String[] args) throws IOException, URISyntaxException, InterruptedException {
		Scanner s = new Scanner(System.in);
		System.out.println("Please insert path to invitees list");
		FileReader f_reader = new FileReader(s.nextLine());
		s.close();
		
		BufferedReader buff = new BufferedReader(f_reader);
		String line = buff.readLine();
		
		
		while (line != null) {
			System.out.println(getPersonAddress(line));
			line = buff.readLine();
		}
		
			}

	public static String getPersonAddress (String name) throws IOException {
		return getPersonAddress(name,false);
	}
	public static String getPersonAddress (String name, boolean mark) throws IOException {
		
		name=name.replaceAll("\'","");//apostrophe not supported, hence removed.
		String link=PREF+name.replaceAll(" ", "%20");//formatting name to URL search.
        // Make a URL to the web page
        URL url = new URL(link);

        // Get the input stream through URL Connection
        URLConnection con = url.openConnection();
        InputStream is =con.getInputStream();
       
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line = null;
        String output = "";

        // read each line and write to System.out
        while ((line = br.readLine()) != null) {
        	if(line.contains("busAddress")) {
        		line=line.substring(21,line.length()-2).trim();
        		if (!output.contains(line))//avoiding duplicate addresses
        			output+=line+", ";
        	}
        }
        if (output.equals("")) {//if there is no match
        	if (mark || !name.replaceFirst(" ", "").contains(" "))//removing one of the first names.
        		return "";
        	else
        		return getPersonAddress(name.substring(0, name.lastIndexOf(" ")),true);
        }
        output=output.substring(0, output.length()-2);//removing last " ,"
        output=output.replace("&quot;", "\"").replace("&#39;", "\'");//formating quotes and apostrophe.
        return output;
    }
}
