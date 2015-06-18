package whitespell.peakapi.endpoints.content.categories;

import com.google.gson.Gson;
import whitespell.logic.EndpointInterface;
import whitespell.logic.RequestContext;
import whitespell.logic.logging.Logging;
import whitespell.logic.sql.ExecutionBlock;
import whitespell.logic.sql.StatementExecutor;
import whitespell.model.CategoryObject;
import whitespell.model.ContentTypeObject;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Pim de Witte(wwadewitte), Whitespell LLC
 *         1/20/15
 *         whitespell.model
 */
public class RequestCategories implements EndpointInterface {


    private static final String GET_CATEGORIES = "SELECT * FROM `categories`";

    @Override
    public void call(final RequestContext context) throws IOException {
        /**
         * Get the categories
         */
        try {
            StatementExecutor executor = new StatementExecutor(GET_CATEGORIES);
            executor.execute(new ExecutionBlock() {
                @Override
                public void process(PreparedStatement ps) throws SQLException {

                    final ResultSet results = ps.executeQuery();
                    ArrayList<CategoryObject> categoryObjects = new ArrayList<>();
                    while (results.next()) {

                        CategoryObject d = new CategoryObject(results.getInt("category_id"), results.getString("category_name"), results.getString("category_thumbnail"), results.getInt("category_followers"), results.getInt("category_publishers"));

                        categoryObjects.add(d);
                    }

                    // put the array list into a JSON array and write it as a response

                    Gson g = new Gson();
                    String response = g.toJson(categoryObjects);
                    context.getResponse().setStatus(200);
                    try {
                        context.getResponse().getWriter().write(response);
                    }catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (SQLException e) {
            Logging.log("High", e);
        }
    }
}
