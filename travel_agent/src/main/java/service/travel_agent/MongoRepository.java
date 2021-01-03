package service.travel_agent;

import com.google.gson.Gson;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.ArrayList;

public class MongoRepository {
    protected MongoCollection<Document> collection;

    public void connectToCollection() {
        final MongoClient mongoClient = MongoClients.create("mongodb+srv://tanmayjoshi:tanmaypass@cluster-quaranteam.sr96d.mongodb.net/bookings?retryWrites=true&w=majority");
        final MongoDatabase database = mongoClient.getDatabase("bookings");
        this.collection = database.getCollection("booking");
    }

    public boolean insertBooking(Booking booking) {
        try {
            final String json = new Gson().toJson(booking);
            final Document doc = Document.parse(json);
            collection.insertOne(doc);
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Booking createBookingFromMongoDocument(final Document document) {
        String uni_id = document.get("_id").toString();
        String flight = document.get("flight").toString();
        String hotel = document.get("hotel").toString(); 
        String activities = document.get("activities").toString(); 
        return new Booking(flight, hotel, activities);
    }

    public Booking getBookingFromMongo(final String search) throws NoSuchFieldException{
        try {
            final Document document = collection.find(eq("id", search)).first();
            return createBookingFromMongoDocument(document);
        } catch (final Exception e) {
            throw new NoSuchFieldException(
            String.format("The given ref id: <{%s}> does not correspond to a booking.", search));
        }
    }

}