package com.sample;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

/**
 * Servlet implementation class AAA
 */
@WebServlet(
		urlPatterns = { "/AAA" }, 
		initParams = { 
				@WebInitParam(name = "test", value = "12345", description = "Sasa 123")
		})
public class AAA extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AAA() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    try{
        String connURL = getServiceURI();
        MongoClient mongo = new MongoClient(new MongoClientURI(connURL));

        DB db = mongo.getDB("db");

        DBCollection table = db.getCollection("user");


        BasicDBObject document = new BasicDBObject();
        document.put("name", "Tom");
        document.put("age", 30);
        document.put("createdDate", new Date());
        table.insert(document);

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("name", "Tom");

        DBCursor cursor = table.find(searchQuery);

        while (cursor.hasNext()) {
            out.println( "Inserted: " + cursor.next());
        }

        BasicDBObject query = new BasicDBObject();
        query.put("name", "Tom");

        BasicDBObject newDocument = new BasicDBObject();
        newDocument.put("name", "Tina");

        BasicDBObject updateObj = new BasicDBObject();
        updateObj.put("$set", newDocument);

        table.update(query, updateObj);

        BasicDBObject searchQuery2 = new BasicDBObject().append("name", "Tina");

        DBCursor cursor2 = table.find(searchQuery2);

        while (cursor2.hasNext()) {
            out.println( "Updated: " + cursor2.next());
        }

        out.println("Success!!");

    } catch (Exception e) {
        out.println("Failed: " + e.getMessage());
        e.printStackTrace();
    }
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	public String getServiceURI() throws Exception {
//	    CloudEnvironment environment = new CloudEnvironment();
//	    if ( environment.getServiceDataByLabels("mongodb").size() == 0 ) {
//	        throw new Exception( "No MongoDB service is bund to this app!!" );
//	    } 
//
//	    Map credential = (Map)((Map)environment.getServiceDataByLabels("mongodb").get(0)).get( "credentials" );
//	 
//	    return (String)credential.get( "url" );
		return "mongodb://47209d2c-2203-46e8-a744-af3fdef447ce:b7b531db-e201-417e-acd3-946e4535fa0b@159.8.128.211:10089/db";
	  }

}
