/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2014 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */
package com.sample;
 
import java.io.IOException;
 
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.util.JSON;
 
/**
 * Servlet implementation class MyGroceryListServlet
 */
@WebServlet("/MyGroceryList")
public class MyGroceryListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
     
    private static final String[] myGroceryList = new String[] {
        "Milk",
        "Bananna",
        "Apple",
        "Cereal",
        "Potato"};
        
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyGroceryListServlet() {
        super();
    }
 
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Reference: http://docs.mongodb.org/ecosystem/tutorial/getting-started-with-java-driver/#getting-started-with-java-driver
         
        String envVars = System.getenv("VCAP_SERVICES");
         
        DBObject dbO = (DBObject)JSON.parse(envVars);
        String parsedString = dbO.get("mongodb").toString();   
         
        // Remove trailing and starting array brackets (otherwise it won't be valid JSON)
        parsedString = parsedString.replaceFirst("\\[ ", "");
        parsedString = parsedString.replaceFirst("\\]$","");
         
        // Get the credentials
        dbO = (DBObject)JSON.parse(parsedString);
        parsedString = dbO.get("credentials").toString();
         
        // For debugging only
        // System.out.println(parsedString);
         
        dbO = (DBObject)JSON.parse(parsedString);
         
        System.out.println("Host name : " + dbO.get("hostname"));  
        String hostName = dbO.get("hostname").toString();
        int port = Integer.parseInt(dbO.get("port").toString());
        String dbName = dbO.get("db").toString();
        String userName = dbO.get("username").toString();
        String password = dbO.get("password").toString();
         
        Mongo mongoClient = new Mongo(hostName,port);
         
        DB db = mongoClient.getDB(dbName);
        db.authenticate(userName, password.toCharArray());
         
        // Clean up old entries
        DBCollection coll = db.getCollection("testCollection");
        coll.drop();
         
        BasicDBObject lastAddedObject = null;
        for (String curItem : myGroceryList) {
            lastAddedObject = new BasicDBObject("i", curItem);
            coll.insert(lastAddedObject);
        }
        response.getWriter().println("<b>My grocery list is:</b>");
         
        coll.remove(lastAddedObject);
        DBCollection loadedCollection = db.getCollection("testCollection");    
        DBCursor cursor = loadedCollection.find();
        try {
           response.getWriter().println("<ul>");
           while(cursor.hasNext()) {
               response.getWriter().println("<li>" + cursor.next().get("i") + "</li>");
           }
           response.getWriter().println("</ul>");
        } finally {
           cursor.close();
        }  
    }
 
    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
    }
 
}