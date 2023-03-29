
public class Card  implements Comparable<Card>{
    long ID;
    String name;
    Rank rank;
    long price;

    public Card(long id, String name, Rank rank){
        this.ID = id;
        this.name = name;
        this.rank = rank;
        this.price = 0;
    }

    @Override
    public String toString(){
        return this.ID +" "+ this.name + " "+ this.rank +" "+ this.price;
    }
/*
    public boolean equals(Card card)
    {
        if (this.ID !=card.ID){
            return false;
        }
        else if (!this.name.equals(card.name)){
            return false;
        }
        else if (!this.rank.equals(card.rank)){
            return false;
        }
        return true;
    }*/

    @Override
    public boolean equals(Object cardObj) {
        if (cardObj == this) return true;
        
        if (!(cardObj instanceof Card)) {
             return false;
        }
        Card card = (Card) cardObj;
  
        return (this.ID == card.ID) && card.name.equals(this.name) && card.rank.equals(this.rank); 
    }

    @Override
    public int hashCode() {
        return (int) ((ID >> 32) ^ ID);
    }

/*
    public int compareTo(Card card) {
        return Comparator.comparing(Card::getRank, Comparator.reverseOrder()).thenComparing(Card::getName).thenComparing(Card::getID).compare(this, card);
      }*/

    public int compareTo(Card card){
        int i = this.rank.compareTo(card.rank);
        if (i == 0){

            int iname = this.name.compareTo(card.name);
            if (iname == 0){

                Long thisID = this.ID;
                Long cardID = card.ID;
                return thisID.compareTo(cardID);
            }
            return iname;
        }
        return i;
    }
}
