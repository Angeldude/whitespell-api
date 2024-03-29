package main.com.whitespell.peak.logic.endpoints.content;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import jdk.nashorn.tools.Shell;
import main.com.whitespell.peak.Server;
import main.com.whitespell.peak.StaticRules;
import main.com.whitespell.peak.logic.Authentication;
import main.com.whitespell.peak.logic.EndpointHandler;
import main.com.whitespell.peak.logic.RequestObject;
import main.com.whitespell.peak.logic.config.Config;
import main.com.whitespell.peak.logic.exec.ShellExecution;
import main.com.whitespell.peak.logic.logging.Logging;
import main.com.whitespell.peak.logic.notifications.impl.ContentUploadedNotification;
import main.com.whitespell.peak.logic.sql.StatementExecutor;
import main.com.whitespell.peak.model.ContentObject;
import org.eclipse.jetty.http.HttpStatus;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * @author Pim de Witte & Cory McAn(cmcan), Whitespell Inc.
 *         5/4/2015
 */
public class AddNewContent extends EndpointHandler {

    private static final String INSERT_CONTENT_QUERY = "INSERT INTO `content`(`user_id`, `category_id`, `content_type`, `content_url`, `content_title`, `content_description`, `thumbnail_url`, `content_price`, `processed`,`timestamp`,`content_url_1080p`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
    private static final String UPDATE_USER_AS_PUBLISHER_QUERY = "UPDATE `user` SET `publisher` = ? WHERE `user_id` = ?";
    private static final String CHECK_DUPLICATE_CONTENT_QUERY = "SELECT `content_id` FROM `content` WHERE `user_id` = ? AND (`content_url` = ? OR `content_title` = ?)";

    private static final String GET_AVAILABLE_PROCESSING_INSTANCES = "SELECT 1 FROM `avcpvm_monitoring` WHERE `queue_size` < 3 AND `shutdown_reported` = 0 AND (`last_ping` IS NULL AND `creation_time` > ? OR `last_ping` > ?)";
    
    private static final String GET_CONTENT_ID_QUERY = "SELECT LAST_INSERT_ID()";


    private static final String DELETE_FROM_CURATION = "DELETE FROM `content_curation` WHERE `content_url` = ?";

    private static final String PAYLOAD_CATEGORY_ID = "categoryId";
    private static final String PAYLOAD_CONTENT_TYPE_ID = "contentType";
    private static final String PAYLOAD_CONTENT_TITLE = "contentTitle";
    private static final String PAYLOAD_CONTENT_URL = "contentUrl";
    private static final String PAYLOAD_CONTENT_URL_1080p = "contentUrl1080p";
    private static final String PAYLOAD_CONTENT_DESCRIPTION = "contentDescription";
    private static final String PAYLOAD_CONTENT_THUMBNAIL = "thumbnailUrl";
    private static final String PAYLOAD_CONTENT_PRICE = "contentPrice";

    private static final String URL_USER_ID_KEY = "userId";

    @Override
    protected void setUserInputs() {
        urlInput.put(URL_USER_ID_KEY, StaticRules.InputTypes.REG_INT_REQUIRED);
        payloadInput.put(PAYLOAD_CATEGORY_ID, StaticRules.InputTypes.REG_INT_REQUIRED);
        payloadInput.put(PAYLOAD_CONTENT_TYPE_ID, StaticRules.InputTypes.REG_INT_REQUIRED);
        payloadInput.put(PAYLOAD_CONTENT_TITLE, StaticRules.InputTypes.REG_STRING_REQUIRED);
        payloadInput.put(PAYLOAD_CONTENT_URL, StaticRules.InputTypes.REG_STRING_REQUIRED);
        payloadInput.put(PAYLOAD_CONTENT_URL_1080p, StaticRules.InputTypes.REG_STRING_OPTIONAL);
        payloadInput.put(PAYLOAD_CONTENT_DESCRIPTION, StaticRules.InputTypes.REG_STRING_REQUIRED);
        payloadInput.put(PAYLOAD_CONTENT_THUMBNAIL, StaticRules.InputTypes.REG_STRING_REQUIRED);
        payloadInput.put(PAYLOAD_CONTENT_PRICE, StaticRules.InputTypes.REG_DOUBLE_OPTIONAL);
    }

    @Override
    public void safeCall(RequestObject context) throws IOException {

        System.out.println("Received content call");
        JsonObject payload = context.getPayload().getAsJsonObject();

        final int user_id = Integer.parseInt(context.getUrlVariables().get(URL_USER_ID_KEY));
        final int category_id = payload.get(PAYLOAD_CATEGORY_ID).getAsInt();
        final int content_type = payload.get(PAYLOAD_CONTENT_TYPE_ID).getAsInt();
        final String content_url = payload.get(PAYLOAD_CONTENT_URL).getAsString();
        final String content_title = payload.get(PAYLOAD_CONTENT_TITLE).getAsString();
        final String content_description = payload.get(PAYLOAD_CONTENT_DESCRIPTION).getAsString();
        final String thumbnail_url = payload.get(PAYLOAD_CONTENT_THUMBNAIL).getAsString();
        final double[] content_price ={0};
        final String[] content_url1080p = {null};

        if(payload.get(PAYLOAD_CONTENT_URL_1080p) != null){
            content_url1080p[0] = payload.get(PAYLOAD_CONTENT_URL_1080p).getAsString();
        }

        if(payload.get(PAYLOAD_CONTENT_PRICE) != null) {
            content_price[0] = payload.get(PAYLOAD_CONTENT_PRICE).getAsDouble();
        }
        else{
            content_price[0] = 0.0;
        }


        final Timestamp now = new Timestamp(Server.getMilliTime());

        int[] contentId = {0};
        int ADMIN_UID = -1;
        String ADMIN_KEY = StaticRules.MASTER_KEY;
        //todo(pim) last_comment

        /**
         * Ensure that the user is authenticated properly
         */

        final Authentication a = new Authentication(context.getRequest().getHeader("X-Authentication"));

        boolean isMe = (a.getUserId() == user_id);

        if (!a.isAuthenticated() || !isMe) {
            context.throwHttpError(this.getClass().getSimpleName(), StaticRules.ErrorCodes.NOT_AUTHENTICATED);
            return;
        }

        /**
         * Ensure content details are of appropriate length
         */
        if(content_title.length() > StaticRules.MAX_CONTENT_TITLE_LENGTH){
            context.throwHttpError(this.getClass().getSimpleName(), StaticRules.ErrorCodes.CONTENT_TITLE_TOO_LONG);
            return;
        }else if(content_url.length() > StaticRules.MAX_CONTENT_URL_LENGTH){
            context.throwHttpError(this.getClass().getSimpleName(), StaticRules.ErrorCodes.CONTENT_URL_TOO_LONG);
            return;
        }else if(content_description.length() > StaticRules.MAX_CONTENT_DESCRIPTION_LENGTH){
            context.throwHttpError(this.getClass().getSimpleName(), StaticRules.ErrorCodes.CONTENT_DESCRIPTION_TOO_LONG);
            return;
        }else if(thumbnail_url.length() > StaticRules.MAX_THUMBNAIL_URL_LENGTH){
            context.throwHttpError(this.getClass().getSimpleName(), StaticRules.ErrorCodes.THUMBNAIL_URL_TOO_LONG);
            return;
        }else if(String.valueOf(content_type).length() > StaticRules.MAX_CONTENT_TYPE_ID_LENGTH){
            context.throwHttpError(this.getClass().getSimpleName(), StaticRules.ErrorCodes.CONTENT_TYPE_ID_TOO_LONG);
            return;
        }

        /**
         * Ensure this content hasn't been uploaded already by this user to avoid duplicates (only check non-bundles)
         */
        int[] duplicate = {0};
        if(content_type != (StaticRules.BUNDLE_CONTENT_TYPE)) {
            try {
                StatementExecutor executor = new StatementExecutor(CHECK_DUPLICATE_CONTENT_QUERY);
                executor.execute(ps -> {
                    ps.setInt(1, user_id);
                    ps.setString(2, content_url);
                    ps.setString(3, content_title);

                    ResultSet results = ps.executeQuery();
                    if (results.next()) {
                        duplicate[0] = 1;
                    }
                });
            } catch (SQLException e) {
                Logging.log("High", e);
                context.throwHttpError(this.getClass().getSimpleName(), StaticRules.ErrorCodes.UNKNOWN_SERVER_ISSUE);
                return;
            }
        }

        /**
         * Prevent upload of duplicate content
         */
        if(duplicate[0] == 1){
            context.throwHttpError(this.getClass().getSimpleName(), StaticRules.ErrorCodes.CONTENT_ALREADY_EXISTS);
            return;
        }

        /**
         * Insert the content into the database
         */

        int[] success = {0};
        try {
            StatementExecutor executor = new StatementExecutor(INSERT_CONTENT_QUERY);
            executor.execute(ps -> {
                ps.setString(1, String.valueOf(user_id));
                ps.setInt(2, category_id);
                ps.setInt(3, content_type);
                ps.setString(4, content_url);
                ps.setString(5, content_title);
                ps.setString(6, content_description);
                ps.setString(7, thumbnail_url);
                ps.setDouble(8, content_price[0]);
                // whether the video processed is true or not, true in all cases but when it's a video uploaded through peak
                ps.setInt(9, content_type == StaticRules.PLATFORM_UPLOAD_CONTENT_TYPE ? 0 : 1);
                ps.setTimestamp(10, now);
                ps.setString(11, content_url1080p[0]);


                int rows = ps.executeUpdate();
                if (rows <= 0){
                    success[0] = 0;
                }else{
                    success[0] = 1;
                }
            });
        } catch (SQLException e) {
            Logging.log("High", e);
            if (e.getMessage().contains("FK_user_content_content_type")) {
                context.throwHttpError(this.getClass().getSimpleName(), StaticRules.ErrorCodes.NO_SUCH_CATEGORY);
            }
            return;
        }

        if(success[0] == 0){
            context.throwHttpError(this.getClass().getSimpleName(), StaticRules.ErrorCodes.CONTENT_NOT_FOUND);
            return;
        }

        /**
         * Get the contentId so that the content can be added to a bundle
         */

        try {
            StatementExecutor executor = new StatementExecutor(GET_CONTENT_ID_QUERY);
            executor.execute(ps -> {

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                
                ResultSet r = ps.executeQuery();
                if (r.next()){
                    contentId[0] = r.getInt("LAST_INSERT_ID()");
                }
            });
        } catch (SQLException e) {
            Logging.log("High", e);
            context.throwHttpError(this.getClass().getSimpleName(), StaticRules.ErrorCodes.UNKNOWN_SERVER_ISSUE);
            return;
        }

        try {
            StatementExecutor executor = new StatementExecutor(GET_AVAILABLE_PROCESSING_INSTANCES);
            final Timestamp min_15_ago = new Timestamp(Server.getMilliTime() - (60 * 1000 * 15)); // 15 mins max
            executor.execute(ps -> {

                ps.setTimestamp(1,min_15_ago);
                ps.setTimestamp(2,min_15_ago);
                ResultSet r = ps.executeQuery();
                if (!r.next() && !Config.TESTING){
                    Logging.log("INFO", "not enough video nodes, inserting one");
                   // ShellExecution.createAndInsertVideoConverter();

                } else {
                    Logging.log("INFO", "we have enough video nodes");
                }
            });
        } catch (SQLException e) {
            Logging.log("High", e);
            context.throwHttpError(this.getClass().getSimpleName(), StaticRules.ErrorCodes.UNKNOWN_SERVER_ISSUE);
            return;
        }

        System.out.println("Removing from content curated");
        /**
         * Delete from content_curated table if exists: used for contentCuration.
         * The contentCurated endpoint adds content to the content_curation table
         * and the content endpoint deletes it from the curation table in order to "accept" it into the content table.
        */
        try {
            StatementExecutor executor = new StatementExecutor(DELETE_FROM_CURATION);
            executor.execute(ps -> {
                ps.setString(1, content_url);

                int rows = ps.executeUpdate();
                if (rows <= 0) {
                    /**
                     * Continue execution if no matching content in curation table to allow content to be added normally.
                     */
                    System.out.println("Failed to delete from curation table");
                }
            });
        } catch (SQLException e) {
            Logging.log("High", e);
            context.throwHttpError(this.getClass().getSimpleName(), StaticRules.ErrorCodes.UNKNOWN_SERVER_ISSUE);
        }

        /**
         * Update user as publisher in database
         */

        success[0] = -1;
        try {
            StatementExecutor executor = new StatementExecutor(UPDATE_USER_AS_PUBLISHER_QUERY);
            executor.execute(ps -> {
                ps.setInt(1, 1);
                ps.setInt(2, user_id);

                int rows = ps.executeUpdate();
                if (rows <= 0) {
                    success[0] = 0;
                }else{
                    success[0] = 1;
                }
            });
        } catch (SQLException e) {
            Logging.log("High", e);
            context.throwHttpError(this.getClass().getSimpleName(), StaticRules.ErrorCodes.UNKNOWN_SERVER_ISSUE);
            return;
        }

        if(success[0] == 0){
            context.throwHttpError(this.getClass().getSimpleName(), StaticRules.ErrorCodes.NOT_PUBLISHING_CATEGORY);
            return;
        }

        System.out.println("adding category publishing");
        Gson g = new Gson();
        try{

            /**
             * Update category publishing based on content uploaded
             */
            HttpResponse<String> stringResponse = null;

            stringResponse = Unirest.post("http://localhost:" + Config.API_PORT + "/users/" + user_id + "/publishing")
                    .header("accept", "application/json")
                    .body("{\n" +
                            "\"categoryId\": \"" + category_id + "\",\n" +
                            "\"action\": \"publish\"\n" +
                            "}")
                    .asString();

            if(!(stringResponse.getBody().contains("published") || stringResponse.getBody().contains("already publishing"))){
                context.throwHttpError(this.getClass().getSimpleName(), StaticRules.ErrorCodes.NOT_PUBLISHING_CATEGORY);
                return;
            }
        } catch (Exception e) {
            Logging.log("High", e);
            context.throwHttpError(this.getClass().getSimpleName(), StaticRules.ErrorCodes.UNKNOWN_SERVER_ISSUE);
            return;
        }

        /**
         * Add the description as the first comment of the video.
         */
        try {
            HttpResponse<String> stringResponse = Unirest.post("http://localhost:" + Config.API_PORT + "/content/" + contentId[0] + "/comments")
                    .header("accept", "application/json")
                    .header("X-Authentication", "" + a.getUserId() + "," + a.getKey() + "")
                    .body("{\n" +
                            "\"userId\": \"" + a.getUserId() + "\",\n" +
                            "\"comment\": \"" + content_description + "\"\n" +
                            "}")
                    .asString();
        }catch(Exception e){
            Logging.log("High", e);
            context.throwHttpError(this.getClass().getSimpleName(), StaticRules.ErrorCodes.UNKNOWN_SERVER_ISSUE);
            //do not throw error on client side
        }

        /**
         * Instead of sending notification when content is added, send notification when content has been processed.
         */

        context.getResponse().setStatus(HttpStatus.OK_200);
        AddContentObject object = new AddContentObject();
        object.setContentId(contentId[0]);
        object.setContentAdded(true);
        String json = g.toJson(object);
        context.getResponse().getWriter().write(json);
    }

    public class AddContentObject {

        private boolean contentAdded;
        private int contentId;

        public int getContentId() {
            return contentId;
        }

        public void setContentId(int contentId) {
            this.contentId = contentId;
        }

        public boolean isContentAdded() {
            return contentAdded;
        }

        public void setContentAdded(boolean contentAdded) {
            this.contentAdded = contentAdded;
        }

    }
}
