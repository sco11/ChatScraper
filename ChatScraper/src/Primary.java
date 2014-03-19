import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class Primary {

	public static void main(String[] args) throws UnknownHostException, IOException {

	String server = "irc.twitch.tv";
	int port = 6667;
	String nick = "jugglernaut";
	String login = "oauth:qr3mzlyfzluylaexctjexfvbo5ltb77";
		
	String channel = "#riotgames";
	
	Socket socket = new Socket(server, port);
		
	BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
	BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
	//Log onto server
	writer.write("PASS " + login + "\r\n");
	writer.write("NICK " + nick + "\r\n");

	writer.flush();
	
	String line = null;
	while((line = reader.readLine()) != null)
	{
		System.out.println(line);
		if(line.indexOf("004") >= 0)
		{
			//logged in
			break;
		}
		else if(line.indexOf("433") >= 0)
		{
			//nickname taken
			System.out.println("Nick taken");
			return;
		}
	}
	
	// Join the channel.
    writer.write("JOIN " + channel + "\r\n");
    writer.flush( );
    String MessageSender;
    String Msg;
    int msgNameStart;
    int msgNameEnd;
    String usersName;
    
    

    //PrintWriter printer = new PrintWriter("twitchlog.txt", );
    FileWriter printer = new FileWriter("twitchlog.txt", true);

    int i = 0;
    
    // Keep reading lines from the server.
    while ((line = reader.readLine( )) != null) {

        if (line.toLowerCase().startsWith("ping")) {
            // We must respond to PINGs to avoid being disconnected.
            //writer.write("PONG " + line.substring(5) + "\r\n");
            writer.write("PONG tmi.twitch.tv\r\n");
            //writer.write("PRIVMSG " + channel + " :I got pinged!\r\n");
            writer.flush( );
        }
        else {
            // Print the raw line received by the bot.
            //System.out.println(line);
            if(line.contains("PRIVMSG"))
            {
            	msgNameStart = line.indexOf('#') + 1;
            	if(msgNameStart == -1)
            	{
            		continue;
            	}
            	msgNameEnd = line.indexOf(' ', msgNameStart);
            	MessageSender = line.substring(msgNameStart, msgNameEnd);        	
            	Msg = line.substring(msgNameEnd + 2);

            	try
            	{
            		
            		usersName = line.substring(1, line.indexOf('!', 2));
            		System.out.println(usersName + " -> " + Msg);
            		printer.write(Msg + " ");
            		i++;
            		if(i == 30)
            		{
            			printer.flush();
            			i = 0;
            		}
            	}
            	catch(Exception e)
            	{
            		
            	}
            	
            }
        }
    }
    
    
		
    printer.close();
		
    socket.close();
	}
	
	
}
