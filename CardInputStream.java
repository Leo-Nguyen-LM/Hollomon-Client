
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CardInputStream extends HollomonClient{
    private long ID; 
    private String name;
    private Rank rank;
    private long price;
    public String response;
    public static List<Card> tempLst;

    public CardInputStream(InputStream Input) throws IOException { //if login fail
        super("netsrv.cim.rhul.ac.uk", 1812); 
        tempLst = new ArrayList<Card>(); 
        BufferedReader item = new BufferedReader(new InputStreamReader(Input)); //Read the entire line with buffer
        String value = item.readLine(); //Read first line of input stream
        while (value.contains("CARD")) { //If a card has been sent from server
            if (value != null) {
                value = item.readLine();}
            if (value != null) {
                this.ID = Long.parseLong(value);}
            value = item.readLine();
            if (value != null) {
                this.name = value;}
            value = item.readLine();
            if (value != null) {
                this.rank = Rank.valueOf(value);}
            value = item.readLine();
            if (value != null) {
                this.price = Long.parseLong(value);}
            value = item.readLine(); //Skip line
            tempLst.add(new Card(this.ID, this.name, this.rank));}
        if (value.equals("OK")) {
            this.response = "OK"; //End of transmission
        }
    }

    @Override
    public void close() { //Close socket
        this.close();
    }

    public List<Card> printList() { //Return entire list
        return tempLst;
    }

    public Card readCard() { //Create card object for one card
        Card card = new Card(this.ID, this.name, this.rank);
        return card;
    }

    public String readResponse() { //Return null if the transmission has reached the end
        return this.response == null ? "OK" : this.response;
    }
}
