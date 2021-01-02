package service.api;



import java.io.File;

import java.io.FileNotFoundException;

import java.io.FileReader;

import java.io.FileWriter;

import java.io.IOException;

import java.net.URI;

import java.net.http.HttpClient;

import java.net.http.HttpRequest;

import java.net.http.HttpResponse;

// import java.text.ParseException;

import java.nio.file.Path;

import java.nio.file.Paths;



import com.mashape.unirest.http.exceptions.UnirestException;



import org.json.simple.JSONArray;

import org.json.simple.JSONObject;

import org.json.simple.parser.JSONParser;

import org.json.simple.parser.ParseException;



import java.io.BufferedReader;

import java.io.IOException;

import java.io.InputStreamReader;

import java.io.InputStream;

import java.util.ArrayList;
import java.util.List;





public class HotelAmadeus2 {

      public static void main(String[] args) throws UnirestException{

            try {

                  HttpRequest request = HttpRequest.newBuilder()

                  .uri(URI.create("https://test.api.amadeus.com/v2/shopping/hotel-offers?cityCode=PAR&roomQuantity=1&adults=2&radius=5&radiusUnit=KM&paymentPolicy=NONE&includeClosed=false&bestRateOnly=true&view=FULL&sort=NONE"))

                  .header("Authorization", "Bearer kpxr45nV2kdgOwpVJ7oFiJdVjpNP")

                  .method("GET", HttpRequest.BodyPublishers.noBody())

                  .build();

                  

                  // HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

			// System.out.println(response.body());



                  // String hotelQuotations = response.body();



                  // String command = "curl \"https://test.api.amadeus.com/v1/security/oauth2/token\" -H \"Content-Type: application/x-www-form-urlencoded\" -d \"grant_type=client_credentials&client_id={u7gTwvqxHbRyEUbKASMPfdTaHfVFPY7k}&client_secret={y1NpUer8LSzWvwNc}\"";

                  // String [] command = {"curl", "\"https://test.api.amadeus.com/v1/security/oauth2/token\"", "-H", "\"Content-Type: application/x-www-form-urlencoded\"", 
                  //                         "-d","\"grant_type=client_credentials&client_id={u7gTwvqxHbRyEUbKASMPfdTaHfVFPY7k}&client_secret={y1NpUer8LSzWvwNc}\""};

                  List<String> tokenCommand = new ArrayList<String>();

                  tokenCommand.add("curl");
                  tokenCommand.add("https://test.api.amadeus.com/v1/security/oauth2/token");
                  tokenCommand.add("-H");
                  tokenCommand.add("Content-Type: application/x-www-form-urlencoded");
                  tokenCommand.add("-d");
                  tokenCommand.add("grant_type=client_credentials&client_id=u7gTwvqxHbRyEUbKASMPfdTaHfVFPY7k&client_secret=y1NpUer8LSzWvwNc");


                  // String command = "curl" + " \"https://test.api.amadeus.com/v1/security/oauth2/token\"" +" -H"+ " \"Content-Type: application/x-www-form-urlencoded\""+ " -d"+" \"grant_type=client_credentials&client_id={u7gTwvqxHbRyEUbKASMPfdTaHfVFPY7k}&client_secret={y1NpUer8LSzWvwNc}\"";

                  
                  System.out.println("Command: \n");
                  for(String cmd : tokenCommand){
                        System.out.println(cmd + "\n");

                  }

                  

                  JSONObject j = new JSONObject();

                  ProcessBuilder builder = new ProcessBuilder(tokenCommand);
                  

                  Process process = builder.start();



                  // Process process = Runtime.getRuntime().exec(tokenCommand.toString());

                  // Firstly, we create an instance of the Process class again, but this time using Runtime.getRuntime(). 

                  // We can get an InputStream as in our previous example by calling the getInputStream() method:



                  // JSONObject accessToken = new JSONObject();

                  // accessToken = process.getInputStream();

                  // When the instance is no longer needed, we should release system resources by calling the destroy() method.

                  final InputStream in = process.getInputStream();

                  final InputStream err = process.getErrorStream();



                  BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in,"UTF-8"));

                  String s = bufferedReader.readLine();

                  System.out.println("Token 1: " + s);



                  while ((s = bufferedReader.readLine()) != null) {

                        System.out.println(s);

                        }



                  // ProcessBuilder   ps =new ProcessBuilder(command);
                  ProcessBuilder   ps =new ProcessBuilder(tokenCommand);




                  //From the DOC:  Initially, this property is false, meaning that the 

                  //standard output and error output of a subprocess are sent to two 

                  //separate streams

                  ps.redirectErrorStream(true);



                  Process pr = ps.start();  



                  BufferedReader in2 = new BufferedReader(new InputStreamReader(pr.getInputStream()));

                  String line;

                  while ((line = in2.readLine()) != null) {

                  System.out.println(line);

                  }





                  // HttpClient client = HttpClients.custom().build();

                  // HttpUriRequest request = RequestBuilder.get()

                  // .setUri(SAMPLE_URL)

                  // .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")

                  // .build();

                  // client.execute(request);



                  

                  HttpRequest requestAccessToken = HttpRequest.newBuilder()

                  .uri(URI.create("https://test.api.amadeus.com/v1/security/oauth2/token/"))

                  .method("POST", HttpRequest.BodyPublishers.noBody())

                  .build();

                  

                  HttpResponse<String> responseAccessToken = HttpClient.newHttpClient().send(requestAccessToken, HttpResponse.BodyHandlers.ofString());

			System.out.println("Token: "+responseAccessToken.body());

                 

                

            } catch(IOException e) {

                  e.printStackTrace();

            }

            catch(InterruptedException e) {

                  e.printStackTrace();

            }

                    

      }



      

}           