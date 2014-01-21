package upc.edu.eetac.dsa.dsaq31314g3.futbol.auth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

/**
 * Servlet implementation class ServletRegister
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

/**
 * Servlet implementation class ServletRegister
 */
@SuppressWarnings("deprecation")
public class ServletRegister extends HttpServlet {

        DataSource ds = null;

        protected void doGet(HttpServletRequest request,
                        HttpServletResponse response) throws ServletException, IOException {

        }

        protected void doPost(HttpServletRequest request,
                        HttpServletResponse response) throws ServletException, IOException {
                String username = request.getParameter("usernamer");
                String password = request.getParameter("passwordr");
                String name = request.getParameter("name");
                String email = request.getParameter("email");
                String rol = "registered";
                System.out.println(username);
                System.out.println(password);
                System.out.println(name);
                System.out.println(email);

                Connection conn = null;
                try {
                        conn = ds.getConnection();
                } catch (SQLException e) {
                        e.printStackTrace();
                }
                Statement stmt = null;
                try {
                        stmt = conn.createStatement();
                } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
                String update = "insert into usuarios values(null,'" + username + "','" + email + 
                		"','" + name  + "', MD5('"+ password + "'), '" + rol + "');";
                		
                		  
                try {
                        stmt.executeUpdate(update);
                } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
              
                try {
                        stmt.close();
                        conn.close();
                } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }

                HttpHost targetHost = new HttpHost("localhost", 8080, "http");
                CredentialsProvider credsProvider = new BasicCredentialsProvider();
                credsProvider.setCredentials(new AuthScope(targetHost.getHostName(),
                                targetHost.getPort()), new UsernamePasswordCredentials("admin",
                                "admin"));

                // Create AuthCache instance
                AuthCache authCache = new BasicAuthCache();
                // Generate BASIC scheme object and add it to the local auth cache
                BasicScheme basicAuth = new BasicScheme();
                authCache.put(targetHost, basicAuth);

                // Add AuthCache to the execution context
                HttpClientContext context = HttpClientContext.create();
                context.setCredentialsProvider(credsProvider);

                HttpPost httpPost = new HttpPost(
                                "http://localhost:8080/futbol-api/users");
                httpPost.addHeader("Content-Type",
                                "application/vnd.futbol.api.user+json");
                httpPost.addHeader("Accept", "application/vnd.futbol.api.user+json");

                String user2 = "{\"usernamer\": \"" + username + "\", \"passwordr\": \""
                                + password + "\", \"name\": \"" + name + "\", \"email\": \""
                                + email + "\" }";
                httpPost.setEntity(new StringEntity(user2));
                CloseableHttpClient closeableHttpClient = HttpClients.createDefault();

                CloseableHttpResponse httpResponse = closeableHttpClient.execute(
                                targetHost, httpPost, context);
                HttpEntity entity = httpResponse.getEntity();

                BufferedReader reader = new BufferedReader(new InputStreamReader(
                                entity.getContent()));
                String line = null;
                while ((line = reader.readLine()) != null)
                        System.out.println(line);

                httpResponse.close();

//                String url = "/Registrok.html";
//                ServletContext sc = getServletContext();
//                RequestDispatcher rd = sc.getRequestDispatcher(url);
//                rd.forward(request, response);
                
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.write("success");
        }

        @Override
        public void init() throws ServletException {
                // TODO Auto-generated method stub
                super.init();
                ds = DataSourceSPA.getInstance().getDataSource();

        }

}
