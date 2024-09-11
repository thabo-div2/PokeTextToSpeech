package speechtotextandtexttospeechchatbot;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.awt.*;
import javax.swing.*;
import org.json.JSONArray;

/**
 *
 * @author Thabo Setsubi st10445734@vcconnnect.edu.za ST10445734
 */
public class SpeechToTextAndTextToSpeechChatbot {

    public static void main(String[] args) {
        mainWindow();
        
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        
    }
    
    public static void mainWindow() 
    {
        JFrame win = new JFrame("Welcome window");
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        win.setSize(400, 400);
        
        JPanel topPnl = new JPanel();
        JPanel btmPnl = new JPanel();
        
        topPnl.setPreferredSize(new Dimension(400, 100));
        
        JLabel welMsg = new JLabel("Welcome");
        
        topPnl.add(welMsg);
        
        JButton whoPokemonBtn = new JButton("Who is that Pokemon?");
        JButton jokeBtn = new JButton("Want to hear a joke?");
        JButton extBtn = new JButton("Exit");
        
        btmPnl.add(whoPokemonBtn);
        btmPnl.add(jokeBtn);
        btmPnl.add(extBtn);
        
        win.add(topPnl, BorderLayout.NORTH);
        win.add(btmPnl, BorderLayout.SOUTH);
        
        whoPokemonBtn.addActionListener(e -> 
        {
            fetchPokemon();
        });
        
        jokeBtn.addActionListener(e -> 
        {
            getJoke();
        });
        
        extBtn.addActionListener(e -> 
        {
            closeWindow(win);
        });
        
        win.setLayout(new FlowLayout());
        
        win.setVisible(true);
    }
    
    public static void Voices(){
    System.setProperty("freetts.voices",
                "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory," +
                "com.sun.speech.freetts.en.us.cmu_time_awb.AlanVoiceDirectory");

        VoiceManager voiceManager = VoiceManager.getInstance();
        Voice[] voices = voiceManager.getVoices();

        System.out.println("Available voices:");
        for (Voice v : voices) {
            System.out.println(v.getName());
        }
    }
    

    public static void textToSpeech(String text) {
    System.setProperty("freetts.voices",
            "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory," +
            "com.sun.speech.freetts.en.us.cmu_time_awb.AlanVoiceDirectory");
    
    //Fetching the voice to be used from the TTS engine
    VoiceManager voiceManager = VoiceManager.getInstance();
    Voice voice = voiceManager.getVoice("kevin16");

    if (voice != null) {
        voice.allocate();
        
        // Set parameters to approximate TTS
        voice.setRate(120);  // Moderate to fast speech rate
        voice.setPitch(100); // Neutral pitch
        voice.setVolume(2);  // Medium volume
        
        voice.speak(text);
        voice.deallocate();
    } else {
        System.err.println("Voice not found.");
    }
}

    public static void chatbot() {
        List<String[]> responses = new ArrayList<>();
        responses.add(new String[]{"hello", "Hi there!"});
        responses.add(new String[]{"bye", "Goodbye!"});

        Scanner scanner = new Scanner(System.in);
        System.out.println("Start chatting with the bot (type 'exit' to end):");

        while (true) {
            System.out.print("You: ");
            String userInput = scanner.nextLine().toLowerCase();
            if (userInput.equals("exit")) {
                System.out.println("Bot: Goodbye!");
                break;
            }

            String response = "I'm not sure how to respond to that.";
            for (String[] pair : responses) {
                if (pair[0].equals(userInput)) {
                    response = pair[1];
                    break;
                }
            }

            System.out.println("Bot: " + response);
            //Allows Chatbot to provide auditory responses
            textToSpeech(response);
        }
    }
    
    public static void fetchPokemon() 
    {
        StringBuilder content = new StringBuilder();  
        try 
        {
            Random randm = new Random();
            int randomId = randm.nextInt(151)+ 1;
            String urlString = "https://pokeapi.co/api/v2/pokemon/" + 25;
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            
            connection.getRequestMethod();
            
            // wrapping the urlconnection in a bufferedreader  
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));  
            String line;  
            // reading from the urlconnection using the bufferedreader  
            while ((line = bufferedReader.readLine()) != null)  
            {  
              content.append(line).append("\n");  
              
            } 
            System.out.println(content);
            
            
            bufferedReader.close();  
            
            // Parse JSON response
            JSONObject jsonResponse = new JSONObject(content.toString());
            
            if (jsonResponse != null) 
            {
                String name = jsonResponse.getString("name");
                int id = jsonResponse.getInt("id");
                
                // Get the flavour text
                String speciesURL = "https://pokeapi.co/api/v2/pokemon-species/" + 25;
                URL speciesAPI = new URL(speciesURL);
                HttpURLConnection speciesCon = (HttpURLConnection) url.openConnection();
                
                speciesCon.getRequestMethod();
                
                BufferedReader specReader = new BufferedReader(new InputStreamReader(speciesCon.getInputStream()));
                StringBuilder specContent = new StringBuilder();
                
                while((line = specReader.readLine())!= null) 
                {
                    specContent.append(line).append("\n");
                }
                System.out.println("EISH \n" + specContent);
                specReader.close();
                
                JSONObject resp2 = new JSONObject(specContent.toString());
                
                String flavorText = "";
                
                if (resp2.has("flavor_text_entries") ) 
                {
                    JSONArray flavorTextEnts = resp2.optJSONArray("flavor_text_entries");
                    for(int i=0; i < 2; i++)  
                    {
                        JSONObject entry = flavorTextEnts.getJSONObject(i);
                        if (entry.getJSONObject("language").getString("name").equals("en")) 
                        {
                            flavorText = entry.getString("flavor_text").replaceAll("\n", " ");
                            break;
                        }
                    }
                }
                String text = "Name: " + name + " \n" 
                        + "ID: " + id + " \n" 
                        + "Flavor Text: " + flavorText + " \n";
                
                String text1 = name ;
                
                System.out.println(resp2.has("flavor_text_entries"));
                textToSpeech(text1);
                JOptionPane.showMessageDialog(null, text);
                
            } 
            else 
            {
                System.out.println("Failed to fetch Pokémon data.");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public static void getJoke() 
    {
        StringBuilder content = new StringBuilder();
        try 
        {
            Random randm = new Random();
            int randomId = randm.nextInt(151)+ 1;
            String urlString = "https://api.chucknorris.io/jokes/random";
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.getRequestMethod();
            
            // wrapping the urlconnection in a bufferedreader  
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));  
            String line;  
            // reading from the urlconnection using the bufferedreader  
            while ((line = bufferedReader.readLine()) != null)  
            {  
              content.append(line).append("\n");  
              
            } 
            System.out.println(content);
            
            
            bufferedReader.close(); 
            
            // Parse JSON response
            JSONObject jsonResponse = new JSONObject(content.toString());
            
            if (jsonResponse != null) 
            {
                String joke = jsonResponse.getString("value");
                
                System.out.println(jsonResponse.toString());
                textToSpeech(joke);
                JOptionPane.showMessageDialog(null, joke);
                
            } 
            else 
            {
                System.out.println("Failed to fetch Pokémon data.");
            }
        }
        catch(Exception e) 
        {
            e.printStackTrace();
        }
    }
    
    // method to close the windows
    public static void closeWindow(JFrame window)
    {
        textToSpeech("Bye-Bye");
        window.dispose();
    }
    //-------------------------00ooo0oo End of file oo0ooo00------------------//
}