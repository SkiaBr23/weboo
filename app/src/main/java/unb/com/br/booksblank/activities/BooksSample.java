package unb.com.br.booksblank.activities;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.books.Books;
import com.google.api.services.books.BooksRequestInitializer;
import com.google.api.services.books.model.Volume;
import com.google.api.services.books.model.Volumes;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.NumberFormat;

import static com.google.api.client.googleapis.javanet.GoogleNetHttpTransport.*;

/**
 * Created by rafa_ on 29/04/2017.
 */

public class BooksSample {

    /**
     * Be sure to specify the name of your application. If the application name is {@code null} or
     * blank, the application will log a warning. Suggested format is "MyCompany-ProductName/1.0".
     */
    private static final String APPLICATION_NAME = "Weboo";

    private static final NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance();
    private static final NumberFormat PERCENT_FORMATTER = NumberFormat.getPercentInstance();


    /* Main para testes */
 /*   public static void main(String [ ] args){
        JsonFactory jsonFactory = new JacksonFactory();

        try {
            Volume book = getBookById(jsonFactory,"s1gVAAAAYAAJ");
         //   Volumes books2 = queryBooksBySubject(jsonFactory,"adventure");
            // Success!
            return;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }*/
    public Volume getBookById(JsonFactory jsonFactory, String volumeId) throws Exception {
        ClientCredentials.errorIfNotSpecified();

        // Set up Books client.
        final Books books = new Books.Builder(new com.google.api.client.http.javanet.NetHttpTransport(), jsonFactory, null)
                .setApplicationName(APPLICATION_NAME)
                .setGoogleClientRequestInitializer(new BooksRequestInitializer(ClientCredentials.API_KEY))
                .build();
        // Set query string and filter only Google eBooks.
        //System.out.println("Query: [" + query + "]");

        Books.Volumes.Get volumesList = books.volumes().get(volumeId);
        // Execute the query.
        Volume volume = volumesList.execute();
        if (volume == null) {
            System.out.println("No matches found.");
            return null;
        } else {
            return volume;
        }
    }

    public Volumes queryBooksBySubject(JsonFactory jsonFactory, String subject) throws Exception {
        ClientCredentials.errorIfNotSpecified();

        // Set up Books client.
        final Books books = new Books.Builder(new com.google.api.client.http.javanet.NetHttpTransport(), jsonFactory, null)
                .setApplicationName(APPLICATION_NAME)
                .setGoogleClientRequestInitializer(new BooksRequestInitializer(ClientCredentials.API_KEY))
                .build();
        // Set query string and filter only Google eBooks.
        //System.out.println("Query: [" + query + "]");

        Books.Volumes.List volumesList = books.volumes().list("subject:" + subject);
        // Execute the query.
        Volumes volumes = volumesList.execute();
        if (volumes.getTotalItems() == 0 || volumes.getItems() == null) {
            System.out.println("No matches found.");
            return null;
        } else {
            return volumes;
        }
    }

    public Volumes queryGoogleBooks(JsonFactory jsonFactory, String query) throws Exception {
        ClientCredentials.errorIfNotSpecified();

        // Set up Books client.
        final Books books = new Books.Builder(new com.google.api.client.http.javanet.NetHttpTransport(), jsonFactory, null)
                .setApplicationName(APPLICATION_NAME)
                .setGoogleClientRequestInitializer(new BooksRequestInitializer(ClientCredentials.API_KEY))
                .build();
        // Set query string and filter only Google eBooks.
        //System.out.println("Query: [" + query + "]");
       // volumesList.setFilter("ebooks");

        Books.Volumes.List volumesList = books.volumes().list(query);
        // Execute the query.
        Volumes volumes = volumesList.execute();
        if (volumes.getTotalItems() == 0 || volumes.getItems() == null) {
            System.out.println("No matches found.");
            return null;
        } else {
            return volumes;
        }

        // Output results.
        /*for (Volume volume : volumes.getItems()) {
            Volume.VolumeInfo volumeInfo = volume.getVolumeInfo();
            Volume.SaleInfo saleInfo = volume.getSaleInfo();
            System.out.println("==========");
            // Title.
            System.out.println("Title: " + volumeInfo.getTitle());
            // Author(s).
            java.util.List<String> authors = volumeInfo.getAuthors();
            if (authors != null && !authors.isEmpty()) {
                System.out.print("Author(s): ");
                for (int i = 0; i < authors.size(); ++i) {
                    System.out.print(authors.get(i));
                    if (i < authors.size() - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.println();
            }
            // Description (if any).
            if (volumeInfo.getDescription() != null && volumeInfo.getDescription().length() > 0) {
                System.out.println("Description: " + volumeInfo.getDescription());
            }
            // Ratings (if any).
            if (volumeInfo.getRatingsCount() != null && volumeInfo.getRatingsCount() > 0) {
                int fullRating = (int) Math.round(volumeInfo.getAverageRating().doubleValue());
                System.out.print("User Rating: ");
                for (int i = 0; i < fullRating; ++i) {
                    System.out.print("*");
                }
                System.out.println(" (" + volumeInfo.getRatingsCount() + " rating(s))");
            }
            // Price (if any).
            if (saleInfo != null && "FOR_SALE".equals(saleInfo.getSaleability())) {
                double save = saleInfo.getListPrice().getAmount() - saleInfo.getRetailPrice().getAmount();
                if (save > 0.0) {
                    System.out.print("List: " + CURRENCY_FORMATTER.format(saleInfo.getListPrice().getAmount())
                            + "  ");
                }
                System.out.print("Google eBooks Price: "
                        + CURRENCY_FORMATTER.format(saleInfo.getRetailPrice().getAmount()));
                if (save > 0.0) {
                    System.out.print("  You Save: " + CURRENCY_FORMATTER.format(save) + " ("
                            + PERCENT_FORMATTER.format(save / saleInfo.getListPrice().getAmount()) + ")");
                }
                System.out.println();
            }
            // Access status.
            String accessViewStatus = volume.getAccessInfo().getAccessViewStatus();
            String message = "Additional information about this book is available from Google eBooks at:";
            if ("FULL_PUBLIC_DOMAIN".equals(accessViewStatus)) {
                message = "This public domain book is available for free from Google eBooks at:";
            } else if ("SAMPLE".equals(accessViewStatus)) {
                message = "A preview of this book is available from Google eBooks at:";
            }
            System.out.println(message);
            // Link to Google eBooks.
            System.out.println(volumeInfo.getInfoLink());
        }
        System.out.println("==========");
        System.out.println(
                volumes.getTotalItems() + " total results at http://books.google.com/ebooks?q="
                        + URLEncoder.encode(query, "UTF-8"));*/
    }
}