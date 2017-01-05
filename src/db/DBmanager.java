package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import objects.Movie;
import window.popup;




public class DBmanager {
	static Connection c = null;
	public static Connection dbConnector(){
		
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:Movies.sqlite");
			return c;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new popup("Fehler in der Verbindung", "Die Verbindung zur Datenbank konnte nicht hergestellt werden.", "Überprüfen sie bitte die Position der Datenbank");
		System.err.println("Verbindung zur Datenbank konnte nicht hergestellt werden.");
		return null;
	}
	
	
	public static ObservableList<Movie> getAllMovies(){
		ObservableList<Movie> movies = FXCollections.observableArrayList();
		Statement st;
		ResultSet rs;
		try {
			
			st = c.createStatement();
			rs = st.executeQuery("SELECT * FROM movielist");
			ResultSetMetaData rsmd = rs.getMetaData();
			int ColCount = rsmd.getColumnCount();
			
			while(rs.next()){
				Object[] a = new Object[ColCount];
				for(int i = 0; i < ColCount; i++){
					
					a[i] = rs.getObject(i+1);
				}
				for(Object o:a){
					System.out.print(o+" ");
				}
				Movie m = new Movie((int)a[0], (String)a[1], (int)a[2], (int)a[3], (String)a[4], (String)a[5], (String) a[6], (String) a[7]);
				movies.add(m);
			}
			st.close();
			for(Movie m:movies){
				if(m.getCategory() =="null"){
					m.setCategory(null);
				}
				if(m.getDescription() == "null"){
					m.setDescription(null);
				}
				if(m.getLocation() == "null"){
					m.setLocation(null);
				}
				if(m.getSeries() == "null"){
					m.setSeries(null);
					
				}
			}
			return movies;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return null;
		
	}
	
	public static void deleteMovie(Movie m){
		
		try {
			Statement st = c.createStatement();
			st.execute("DELETE FROM movielist WHERE id = "+m.getID());
			st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static int getNextID(){
		try {
			Statement st = c.createStatement();
			ResultSet rs = st.executeQuery("SELECT id FROM movielist");
			List<Integer> ids = new ArrayList<>();
			int nextID = 0;
			while(rs.next()){
				int id = rs.getInt(1);
				ids.add(id);	
			}
			boolean run = true;
			while(run){
				if(!ids.contains(nextID)){
					run = false;
					st.close();
					return nextID;
				}
				nextID++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return -1;
	}
	

	
	public static void removeSaveloc(String saveloc){
		
		try {
			Statement st = c.createStatement();
			st.execute("UPDATE settings SET saveloc = null WHERE saveloc = '"+saveloc+"'");
			st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void addSaveloc(String saveloc){
		try {
			Statement st = c.createStatement();
			st.execute("INSERT INTO settings (saveloc) VALUES ('"+saveloc+"')");
			st.close();
		} catch (SQLException e) {
			// TODO: handle exception
		}
	}
	public static void addSeries(String s){
		try {
			Statement st = c.createStatement();
			st.execute("INSERT INTO settings (serien) VALUES ('"+s+"')");
			st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void removeSeries(String s){
		try {
			Statement st = c.createStatement();
			st.execute("UPDATE settings SET serien = null WHERE serien = '"+s+"'");
			st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public static void addCategory(String s){
		try {
			Statement st = c.createStatement();
			st.execute("INSERT INTO settings (category) VALUES ('"+s+"')");
			st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void removeCategory(String s){
		try {
			Statement st = c.createStatement();
			st.execute("UPDATE settings SET category = Null WHERE serien = '"+s+"'");
			st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void addMovie(Movie m){
		try {
			if(m.getCategory() != null){
				m.setCategory(" '"+m.getCategory()+"' ");
			}
			
			if(m.getDescription() != null){
				m.setDescription(" '"+m.getDescription()+"' ");
				System.out.println(m.getDescription());
			}
			
			if(m.getLocation() != null){
				m.setLocation(" '"+m.getLocation()+"' ");
			}
			
			if(m.getSeries() != null){
				m.setSeries(" '"+m.getSeries()+"' ");
			}
			Statement st = c.createStatement();
			//insert into movielist ...
			
			st.execute("INSERT INTO movielist (id, name, fsk, length, speicherort, serie, Kategorie, InfoText) VALUES ("+m.getID()+", '"+m.getTitle()+"', "+m.getFSK()
			+", "+m.getLength()+", "+m.getLocation()+", "+m.getSeries()+", "+m.getCategory()+", "+m.getDescription()+")");
			
			
			st.close();
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			System.err.println("An error occured when adding a movie");
		}
	}

}
