package org.exoplatform.bookstore.rest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;;


public class DeleteRequest {
  public static void main( String[] args) throws Exception {
    DeleteRequest deleteRequest = new DeleteRequest();
    System.out.println("Sending a Delete Request to delete a book");
    deleteRequest.sendDeleteRequest();
  }
  public void sendDeleteRequest() throws Exception {
    // build a delete request with book id = 35c9bbe57f0001011a420fa2fa277de8
    String url = "http://localhost:8080/portal/rest/bookstore/delete/35bfac977f000101436afc01349f6293";
    URL obj = new URL(url);
    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
    //add reuqest header
    con.setRequestMethod("DELETE");
    con.setRequestProperty("User-Agent", "my PC");
    con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
//    con.setRequestProperty("X-HTTP-Method-Override", "DELETE");
    int responseCode = con.getResponseCode();
    System.out.println("\nSending 'DELETE' request to URL : " + url);
    System.out.println("Response Code : " + responseCode);
 
    BufferedReader in = new BufferedReader(
            new InputStreamReader(con.getInputStream()));
    String inputLine;
    StringBuffer response = new StringBuffer();
 
    while ((inputLine = in.readLine()) != null) {
      response.append(inputLine);
    }
    in.close();
 
    //print result
    System.out.println(response.toString());
  
    }
  
}
