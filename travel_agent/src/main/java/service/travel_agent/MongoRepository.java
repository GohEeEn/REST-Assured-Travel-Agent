package service.travel_agent;

import com.google.gson.Gson;
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

    public boolean createBooking(Booking booking) {
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

    // private Coffin createCoffinFromMongoDocument(@NonNull final Document document) {
    //     return Coffin.builder()
    //             .id(document.get(MongoConstants.ID).toString())
    //             .userEmail(document.getString(MongoConstants.EMAIL))
    //             .name(document.getString(MongoConstants.NAME))
    //             .image(document.getString(MongoConstants.IMAGE))
    //             .description(document.getString(MongoConstants.DESCRIPTION))
    //             .price(document.getInteger(MongoConstants.PRICE))
    //             .timeStamp(document.getLong(MongoConstants.TIMESTAMP))

    //             .build();
    // }

    // public ArrayList<Coffin> getCoffins() {
    //     final ArrayList<Coffin> coffinList = new ArrayList<>();

    //     for (final Document coffinDoc : collection.find()) {
    //         coffinList.add(createCoffinFromMongoDocument(coffinDoc));
    //     }
    //     return coffinList;
    // }

    // TODO search string analysis
    // public ArrayList<Coffin> getCoffins(@NonNull final String search) {
    //     final ArrayList<Coffin> coffinList = new ArrayList<>();

    //     for (final Document coffinDoc : collection.find(Filters.text(search))) {
    //         coffinList.add(createCoffinFromMongoDocument(coffinDoc));
    //     }
    //     return coffinList;
    // }

//     public boolean removeCoffins(@NonNull final long time) {
//         try {
// //            final BsonArray toRemove = new BsonArray();
// //            for (final Document document: collection.find(Filters.lte(MongoConstants.TIMESTAMP, time))){
// //                toRemove.add()
// //            }
//             collection.deleteMany(Filters.lte(MongoConstants.TIMESTAMP, time));
//             return true;
//         } catch (final Exception e) {
//             return false;
//         }
//     }
}