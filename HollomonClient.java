import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.io.*;

class ComparisionCard implements Comparator<Card> {
    public int compare(Card card1, Card card2){
        int result = card1.rank.compareTo(card2.rank);
        if (result == 0){
            return card1.name.compareTo(card2.name);
        }
        else{
            return result;
        }
    }
}

public class HollomonClient {
    private String server; 
    private int port;
    public Socket Socket; 
    public String username; 
    public String password;
    public InputStream CardInputStreams;
    public OutputStream CardOutputStreams;

    public HollomonClient(String server, int port) throws IOException, UnknownHostException {
        this.server = server;
        this.port = port;
        this.Socket= new Socket(server, port);
        this.CardInputStreams = Socket.getInputStream(); //data input stream
        this.CardOutputStreams = Socket.getOutputStream();
    }

    public List<Card> login(String username, String password) throws IOException, UnknownHostException { //If server not found
        this.username = username;
        this.password = password;
        try {
            Socket socket = new Socket(server, port);
            BufferedReader input = new BufferedReader(new InputStreamReader(this.CardInputStreams));
            PrintStream output = new PrintStream(this.CardOutputStreams);


            output.println(username); //Send username 
            output.println(password); //Send password 

            String value = input.readLine();
            List<Card> lst = new ArrayList<Card>();
            
            if (value.contains("successfully.")) { 
                boolean statusRegister = true; 
                CardInputStream cardItem = new CardInputStream(CardInputStreams); //Create an input stream


                while (statusRegister) {
                    
                    lst.add(cardItem.readCard());
                    lst = CardInputStream.tempLst; //Add it to the final list
                    
                    if (cardItem.readResponse().equals("OK")) { //last item
                        statusRegister = false; 
                    } else {
                        lst.add(cardItem.readCard());
                    }
                }
                Collections.sort(lst, new ComparisionCard());
                socket.close();
                return lst;
            }

            socket.close();
            return null;
        } catch (UnknownHostException e) {
            return null;
        }
    }

    public void close() { 
        try {
            this.Socket.close();
        } catch (IOException e) {
        }
    }

    public long getCredits() throws IOException, UnknownHostException {
        try{
            Socket socket = new Socket("netsrv.cim.rhul.ac.uk", 1812); //Logon script
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output.println(getUsername());
            output.println(getPasword());
            String value = input.readLine(); //Reconnect to server
            value = input.readLine();
            output.println("CREDITS");
            while (!value.equals("OK")) { //Ignore everything server sends back
                value = input.readLine();
            }
            return Long.parseLong(input.readLine()); //Take the last line sent from server
        }
        catch (UnknownHostException e) {
            System.out.println("Error has occured");
            return -1;
        }
    }

    public List<Card> getCards() throws IOException, UnknownHostException {
        try{
            Socket socket = new Socket("netsrv.cim.rhul.ac.uk", 1812); //Logon script
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output.println(getUsername()); 
            output.println(getPasword());

            String value = input.readLine();
            input.readLine();
            output.println("CARDS"); //Send CARDS to server
            List<Card> lst = new ArrayList<Card>(); 
            long id = 0L;
            String name = null;
            Rank rank = null;
            long price = 0L;
            while (!value.equals("OK")) { 
                if (value != null) {
                    value = input.readLine();
                }
                if (value != null) {
                    id = Long.parseLong(value);
                }
                value = input.readLine();
                if (value != null) {
                    name = value;
                }
                value = input.readLine();
                if (value != null) {
                    rank = Rank.valueOf(value);
                }
                value = input.readLine();
                if (value != null) {
                    price = Long.parseLong(value);
                }
                value = input.readLine();
                lst.add(new Card(id, name, rank));
            }
            Collections.sort(lst, new ComparisionCard());
            return lst;
        }
        catch (UnknownHostException e) {
            System.out.println("There was an error");
            return null;
        }
    }

    public List<Card> getOffers() throws IOException, UnknownHostException {
        try{
            Socket socket = new Socket("netsrv.cim.rhul.ac.uk", 1812); //Create a new socket for this method
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true); 
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output.println(getUsername()); 
            output.println(getPasword());
            String value = input.readLine();
            value = input.readLine();
            List<Card> lst; //To store offers avalible
            lst = new ArrayList<Card>(); 
            while (!value.equals("OK")) { //Skip to end of transmission
                value = input.readLine();
            }
            output.println("OFFERS"); //OFFERS command to sever
            long id = 0L; 
            String name = null;
            Rank rank = null;
            long price = 0L;
            value = input.readLine(); 
            while (!value.equals("OK")) { //get values for card
                if (value != null) { 
                    value = input.readLine();
                }
                if (value != null) {
                    id = Long.parseLong(value);
                }
                value = input.readLine();
                if (value != null) {
                    name = value;
                }
                value = input.readLine();
                if (value != null) {
                    rank = Rank.valueOf(value);
                }
                value = input.readLine();
                if (value != null) {
                    price = Long.parseLong(value);
                }
                value = input.readLine();
                lst.add(new Card(id, name, rank)); //Create new card object and directly add it to list
            }
            socket.close();
            Collections.sort(lst, new ComparisionCard());
            return lst;}
        catch (UnknownHostException e) {
            System.out.println("There was an error");
            return null;
        }
    }

    public boolean buyCard(Card card) throws IOException, UnknownHostException { //Return it is possible to by card
        try{

            Socket socket = new Socket("netsrv.cim.rhul.ac.uk", 1812); //Create socket
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output.println(getUsername()); //Create stream and login
            output.println(getPasword());
            String inputvalue = input.readLine();

            for (String i = input.readLine(); !inputvalue.equals("OK"); inputvalue = input.readLine()) {
            } //Quickly skip to end of transmission
            output.println("BUY " + card.ID); //Tell the server details of card to buy

            return !input.readLine().equals("ERROR"); //Return boolean
        }       
        catch (UnknownHostException e) {
            System.out.println("There was an error");
            return false;
        }
    }


    public boolean sellCard(Card card, long price) throws IOException, UnknownHostException {
        try{
            Socket socket = new Socket("netsrv.cim.rhul.ac.uk", 1812); //Create new socket
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output.println(getUsername()); //Login and make data input stream
            output.println(getPasword());
            String value = input.readLine();
            for (String i = input.readLine(); !value.equals("OK"); value = input.readLine()) {
            } //Jump to end of the list
            output.println("SELL " + card.ID + " " + card.price);
            return !input.readLine().equals("ERROR"); //Just return the boolean directly
        }
        catch (UnknownHostException e) {
            System.out.println("There was an error");
            return false;
        }
    }

    
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getUsername() {
        return this.username;
    }

    public String getPasword() {
        return this.password;
    }
}
