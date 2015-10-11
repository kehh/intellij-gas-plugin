package com.michaelsnowden.gas_plugin;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.Oauth2Scopes;
import com.google.api.services.oauth2.model.Userinfoplus;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.codehaus.jettison.json.JSONException;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

/**
 * @author michael.snowden
 */

public class GasDeployer extends com.intellij.openapi.actionSystem.AnAction {

    private static GoogleClientSecrets CLIENT_SECRET;

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {

    }

    // Path to client_secret.json which should contain a JSON document such as:
    //   {
    //     "web": {
    //       "client_id": "[[YOUR_CLIENT_ID]]",
    //       "client_secret": "[[YOUR_CLIENT_SECRET]]",
    //       "auth_uri": "https://accounts.google.com/o/oauth2/auth",
    //       "token_uri": "https://accounts.google.com/o/oauth2/token"
    //     }
    //   }
    private static final String CLIENT_SECRET_LOCATION = "/client_secret.json";
    private static final String APPLICATION_NAME = "My Project";
    private static final String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";
    private static final List<String> SCOPES = Arrays.asList(
            DriveScopes.DRIVE_SCRIPTS, DriveScopes.DRIVE, Oauth2Scopes.USERINFO_PROFILE, DriveScopes.DRIVE_APPDATA,
            DriveScopes.DRIVE_APPS_READONLY, DriveScopes.DRIVE_READONLY, DriveScopes.DRIVE_FILE);

    private static GoogleAuthorizationCodeFlow flow = null;
    private static final JacksonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    /**
     * Exception thrown when an error occurred while retrieving credentials.
     */
    public static class GetCredentialsException extends Exception {

        protected String authorizationUrl;

        /**
         * Construct a GetCredentialsException.
         *
         * @param authorizationUrl The authorization URL to redirect the user to.
         */
        public GetCredentialsException(String authorizationUrl) {
            this.authorizationUrl = authorizationUrl;
        }

        /**
         * Set the authorization URL.
         */
        public void setAuthorizationUrl(String authorizationUrl) {
            this.authorizationUrl = authorizationUrl;
        }

        /**
         * @return the authorizationUrl
         */
        public String getAuthorizationUrl() {
            return authorizationUrl;
        }
    }

    /**
     * Exception thrown when a code exchange has failed.
     */
    public static class CodeExchangeException extends GetCredentialsException {
        /**
         * Construct a CodeExchangeException.
         *
         * @param authorizationUrl The authorization URL to redirect the user to.
         */
        public CodeExchangeException(String authorizationUrl) {
            super(authorizationUrl);
        }
    }

    /**
     * Exception thrown when no refresh token has been found.
     */
    public static class NoRefreshTokenException extends GetCredentialsException {
        /**
         * Construct a NoRefreshTokenException.
         *
         * @param authorizationUrl The authorization URL to redirect the user to.
         */
        public NoRefreshTokenException(String authorizationUrl) {
            super(authorizationUrl);
        }
    }

    /**
     * Exception thrown when no user ID could be retrieved.
     */
    private static class NoUserIdException extends Exception {
    }

    /**
     * Retrieved stored credentials for the provided user ID.
     *
     * @param userId User's ID.
     * @return Stored Credential if found, {@code null} otherwise.
     */
    static Credential getStoredCredentials(String userId) {
        // TODO: Implement this method to work with your database. Instantiate a new
        // Credential instance with stored accessToken and refreshToken.
        throw new UnsupportedOperationException();
    }

    /**
     * Store OAuth 2.0 credentials in the application's database.
     *
     * @param userId      User's ID.
     * @param credentials The OAuth 2.0 credentials to store.
     */
    static void storeCredentials(String userId, Credential credentials) {
        // TODO: Implement this method to work with your database.
        // Store the credentials.getAccessToken() and credentials.getRefreshToken()
        // string values in your database.
        throw new UnsupportedOperationException();
    }

    /**
     * Build an authorization flow and store it as a static class attribute.
     *
     * @return GoogleAuthorizationCodeFlow instance.
     * @throws IOException Unable to load client_secret.json.
     */
    static GoogleAuthorizationCodeFlow getFlow() throws IOException {
        if (flow == null) {
            InputStream in =
                    GasDeployer.class.getResourceAsStream(CLIENT_SECRET_LOCATION);
            CLIENT_SECRET = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
            flow = new GoogleAuthorizationCodeFlow.Builder(
                    HTTP_TRANSPORT, JSON_FACTORY, CLIENT_SECRET, SCOPES)
                    .setAccessType("online")
                    .setApprovalPrompt("auto")
                    .build();
        }
        return flow;
    }

    /**
     * Exchange an authorization code for OAuth 2.0 credentials.
     *
     * @param authorizationCode Authorization code to exchange for OAuth 2.0
     *                          credentials.
     * @return OAuth 2.0 credentials.
     * @throws CodeExchangeException An error occurred.
     */
    static Credential exchangeCode(String authorizationCode)
            throws CodeExchangeException {
        try {
            GoogleAuthorizationCodeFlow flow = getFlow();
            GoogleTokenResponse response = flow
                    .newTokenRequest(authorizationCode)
                    .setRedirectUri(REDIRECT_URI)
                    .execute();
            return flow.createAndStoreCredential(response, null);
        } catch (IOException e) {
            System.err.println("An error occurred: " + e);
            throw new CodeExchangeException(null);
        }
    }

    /**
     * Send a request to the UserInfo API to retrieve the user's information.
     *
     * @param credentials OAuth 2.0 credentials to authorize the request.
     * @return User's information.
     * @throws NoUserIdException An error occurred.
     */
    static Userinfoplus getUserInfo(Credential credentials)
            throws NoUserIdException {
        Oauth2 userInfoService = new Oauth2.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credentials)
                .setApplicationName(APPLICATION_NAME)
                .build();
        Userinfoplus userInfo = null;
        try {
            userInfo = userInfoService.userinfo().get().execute();
        } catch (IOException e) {
            System.err.println("An error occurred: " + e);
        }
        if (userInfo != null && userInfo.getId() != null) {
            return userInfo;
        } else {
            throw new NoUserIdException();
        }
    }

    /**
     * Retrieve the authorization URL.
     *
     * @param emailAddress User's e-mail address.
     * @param state        State for the authorization URL.
     * @return Authorization URL to redirect the user to.
     * @throws IOException Unable to load client_secret.json.
     */
    public static String getAuthorizationUrl(String emailAddress, String state)
            throws IOException {
        GoogleAuthorizationCodeRequestUrl urlBuilder = getFlow()
                .newAuthorizationUrl()
                .setRedirectUri(REDIRECT_URI)
                .setState(state);
        urlBuilder.set("user_id", emailAddress);
        return urlBuilder.build();
    }

    /**
     * Retrieve credentials using the provided authorization code.
     * <p/>
     * This function exchanges the authorization code for an access token and
     * queries the UserInfo API to retrieve the user's e-mail address. If a
     * refresh token has been retrieved along with an access token, it is stored
     * in the application database using the user's e-mail address as key. If no
     * refresh token has been retrieved, the function checks in the application
     * database for one and returns it if found or throws a NoRefreshTokenException
     * with the authorization URL to redirect the user to.
     *
     * @param authorizationCode Authorization code to use to retrieve an access
     *                          token.
     * @param state             State to set to the authorization URL in case of error.
     * @return OAuth 2.0 credentials instance containing an access and refresh
     * token.
     * @throws NoRefreshTokenException No refresh token could be retrieved from
     *                                 the available sources.
     * @throws IOException             Unable to load client_secret.json.
     */
    public static Credential getCredentials(String authorizationCode, String state)
            throws CodeExchangeException, NoRefreshTokenException, IOException {
        String emailAddress = "";
        try {
            Credential credentials = exchangeCode(authorizationCode);
            Userinfoplus userInfo = getUserInfo(credentials);
            String userId = userInfo.getId();
            emailAddress = userInfo.getEmail();
            if (credentials.getRefreshToken() != null) {
                storeCredentials(userId, credentials);
                return credentials;
            } else {
                credentials = getStoredCredentials(userId);
                if (credentials != null && credentials.getRefreshToken() != null) {
                    return credentials;
                }
            }
        } catch (CodeExchangeException e) {
            e.printStackTrace();
            // Drive apps should try to retrieve the user and credentials for the
            // current session.
            // If none is available, redirect the user to the authorization URL.
            e.setAuthorizationUrl(getAuthorizationUrl(emailAddress, state));
            throw e;
        } catch (NoUserIdException e) {
            e.printStackTrace();
        }
        // No refresh token has been retrieved.
        String authorizationUrl = getAuthorizationUrl(emailAddress, state);
        throw new NoRefreshTokenException(authorizationUrl);
    }

    // ...

    /**
     * Build a Drive service object.
     *
     * @param credentials OAuth 2.0 credentials.
     * @return Drive service object.
     */
    static Drive buildService(Credential credentials) {
        return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credentials)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /**
     * Print a file's metadata.
     *
     * @param service Drive API service instance.
     * @param fileId  ID of the file to print metadata for.
     */
    static void printFile(Drive service, String fileId) {
        try {
            File file = service.files().get(fileId).execute();

            System.out.println("Title: " + file.getTitle());
            System.out.println("Description: " + file.getDescription());
            System.out.println("MIME type: " + file.getMimeType());
        } catch (org.apache.http.client.HttpResponseException e) {
            if (e.getStatusCode() == 401) {
                // Credentials have been revoked.
                // TODO: Redirect the user to the authorization URL.
                throw new UnsupportedOperationException();
            }
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
        }
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public static void main(String[] args) throws IOException, URISyntaxException, JSONException, NoUserIdException {
        GoogleAuthorizationCodeFlow flow = getFlow();
        String url = flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();
        System.out.println("Enter authorization code:");
        Desktop.getDesktop().browse(new URI(url));
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String code = br.readLine();

        GoogleTokenResponse response = flow.newTokenRequest(code).setRedirectUri(REDIRECT_URI).execute();
        GoogleCredential credential = new GoogleCredential().setFromTokenResponse(response);
        Userinfoplus userInfo = getUserInfo(credential);
        String userId = userInfo.getId();

        //Create a new authorized API client
        Drive service = buildService(credential);
        File file = service.files().get("1fMF1Z0OCt1rnvJDuljOaIfevnQhAohjR_7yI2S1B5RIhayUZgdOUWXsv?alt=media").execute()
                .setAppDataContents(true);
        System.out.println("");


        if (!file.content.equals("")) {
            file = service.files().insert(file,
                    ByteArrayContent.fromString(clientFile.mimeType, clientFile.content))
                    .execute();
        } else {
            file = service.files().insert(file).execute();
        }

        resp.setContentType(JSON_MIMETYPE);
        resp.getWriter().print(new Gson().toJson(file.getId()).toString());
    }
}