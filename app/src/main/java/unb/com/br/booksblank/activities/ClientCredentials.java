package unb.com.br.booksblank.activities;

/**
 * Created by rafa_ on 29/04/2017.
 */

public class ClientCredentials {

    /** Value of the "API key" shown under "Simple API Access". */
    static final String API_KEY =
            "AIzaSyBtRTy5vOKDxdSZM4x-BeGK92U8ZG58fIQ";

    static void errorIfNotSpecified() {
        if (API_KEY.startsWith("Enter ")) {
            System.err.println(API_KEY);
            System.exit(1);
        }
    }
}