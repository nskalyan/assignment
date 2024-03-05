import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/submitData")
public class DataSubmissionServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Database credentials and URL
        String url = "jdbc:mysql://localhost:3305/LAB";
        String user = "root";
        String password = "kalyan";

        try {
            // Load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Create a connection to the database
            Connection conn = DriverManager.getConnection(url, user, password);

            // SQL query to insert data
            String sql = "INSERT INTO test (field1, field2, field3, field4, field5) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, request.getParameter("field1"));
            statement.setString(2, request.getParameter("field2"));
            statement.setString(3, request.getParameter("field3"));
            statement.setString(4, request.getParameter("field4"));
            statement.setString(5, request.getParameter("field5"));

            // Execute the insert command
            statement.executeUpdate();

            // Close the connection
            statement.close();
            conn.close();

            // Redirect to another JSP page to view the submitted data
            response.sendRedirect("viewData.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Data insertion failed.");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve data from the database and set it as a request attribute
        Map<String, String> dataMap = fetchDataFromDatabase();
        request.setAttribute("dataMap", dataMap);

        // Forward the request to the viewData.jsp page
        getServletContext().getRequestDispatcher("/viewData.jsp").forward(request, response);
    }

    private Map<String, String> fetchDataFromDatabase() {
        Map<String, String> dataMap = new HashMap<>();

        // Database credentials and URL
        String url = "jdbc:mysql://localhost:3305/LAB";
        String user = "root";
        String password = "kalyan";

        try {
            // Load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Create a connection to the database
            Connection conn = DriverManager.getConnection(url, user, password);

            // SQL query to fetch data
            String sql = "SELECT * FROM test";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            // Process the result set and populate the dataMap
            if (resultSet.next()) {
                dataMap.put("field1", resultSet.getString("field1"));
                dataMap.put("field2", resultSet.getString("field2"));
                dataMap.put("field3", resultSet.getString("field3"));
                dataMap.put("field4", resultSet.getString("field4"));
                dataMap.put("field5", resultSet.getString("field5"));
            }

            // Close the connection
            resultSet.close();
            statement.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataMap;
    }
}
