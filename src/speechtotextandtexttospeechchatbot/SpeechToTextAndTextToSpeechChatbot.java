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

/**
 *
 * @author Thabo Setsubi st10445734@vcconnnect.edu.za ST10445734
 */
public class SpeechToTextAndTextToSpeechChatbot {

    public static void main(String[] args) {
        
        
        fetchPokemon();
//        Scanner scanner = new Scanner(System.in);
//        boolean running = true;
//
//        while (running) {
//            try {
//                System.out.println("Choose an option: ");
//                System.out.println("1. Text to Speech");
//                System.out.println("2. Chatbot");
//                System.out.println("3. All available voices");
//                System.out.println("4. Exit");
//
//                int choice = scanner.nextInt();
//                scanner.nextLine();  // Consume newline
//
//                switch (choice) {
//                    case 1:
//                        System.out.println("Enter text to convert to speech:");
//                        String text = scanner.nextLine();
//                        textToSpeech(text);
//                        break;
//                    case 2:
//                        chatbot();
//                        break;
//                    case 3:
//                        Voices();
//                        break;
//                    case 4:
//                        running = false;  // Exit the loop
//                        System.out.println("Exiting...");
//                        break;
//                    default:
//                        System.out.println("Invalid choice. Please enter 1, 2, or 3.");
//                }
//            } catch (InputMismatchException e) {
//                System.out.println("Invalid input. Please enter a number.");
//                scanner.nextLine(); // Clear the buffer
//            }
//        }
//        scanner.close();
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
            String urlString = "https://pokeapi.co/api/v2/pokemon/" + randomId;
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

                System.out.println("Name: " + name);
                System.out.println("ID: " + id);
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
    //-------------------------00ooo0oo End of file oo0ooo00------------------//
}