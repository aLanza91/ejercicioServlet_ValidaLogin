package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import fotogramas.modelo.beans.BeanError;


@WebServlet(name="ValidaLogin",urlPatterns="/login")
public class Logins extends HttpServlet {
	
	private String nombreAp;
	private DataSource dsLogins;
	private static final String CONTENT_TYPE="text/html;charset=UTF-8";
	
	private String login;
	private String clave;
	private String nombre ="";
	
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Logins() {
        super();
    }
	
    
    public void init(ServletConfig config) throws ServletException {
    	super.init(config);
    	
    	System.out.println("En método init");
    	ServletContext application = config.getServletContext();
    	nombreAp = (String) application.getInitParameter("nombreAp");
    	System.out.println("Nombre Ap: "+nombreAp);
    	try {
	    	InitialContext initCtx = new InitialContext();
	        dsLogins = (DataSource) initCtx.lookup("java:jboss/datasources/MySQLDS2");
	        //DataSource ds = (DataSource) initCtx.lookup("java:app/env/MySqlDS");
	        System.out.println("Probando el acceso al DataSource al arrancar el servlet...");
	        Connection conDB = dsLogins.getConnection();
	        Statement s = conDB.createStatement();
	        System.out.println("Todo está ¡¡OK!!");
    	}
    	catch(NamingException ne) {
    		System.out.println("Error en servicio JNDI al intentar crear el contexto inicial del servlet o al buscar el datasource.");
    	}
    	catch(SQLException sqle) {
    		System.out.println("Error al acceder a la base de datos.");
    	}

    }
	/**
	 * Procesamiento de peticiones GET
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}
	/**
	 * Procesamiento de peticiones POST
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String login = request.getParameter("login");
		String clave = request.getParameter("clave");
		boolean control = true;
		
		Connection conexion = null;
		Statement st = null;
		ResultSet rs = null;
		PrintWriter out = response.getWriter();
		try {
			conexion = dsLogins.getConnection();
			st = conexion.createStatement();
			rs = st.executeQuery("select login,clave from usuarios where login = '"+login+"'");
			if (rs.next()) {
				if (!rs.getString("clave").equals(clave)) {
					out.write("La clave no coincide.");
					control = false;
				}
			}
			else
			{
				out.write("El login no existe.");
				control = false;
			}
		} catch (SQLException e) {
			out.write("Error en conexión a base de datos");
			control = false;
		
		}
		
		if (control == true){
			response.setContentType(CONTENT_TYPE);
			
			out.write("<html>");
			out.write("<head><title>Logins</title></head>");
			out.write("<body><p>");
			out.write("//nombreAp\\");out.write("<br>");
			out.write(nombre);
			out.write("<br>");
			out.write("Usted se ha logueado con el usuario: "+ "//Prueba\\ login");
			out.write("<br>");
			out.write("</body>");
			out.write("</html>");
			out.close();
		}
	}
}
