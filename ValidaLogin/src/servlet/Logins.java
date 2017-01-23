package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
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


@WebServlet(name="ValidaLogin",urlPatterns="/login")
public class Logins extends HttpServlet {
	
	private String nombreAp;
	private DataSource dsLogins;
	private static final String CONTENT_TYPE="text/html;charset=UTF-8";
	
	private String login;
	private String clave;
	private String nombre;
	
	
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
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nombre = request.getParameter("nombre");
		String login = request.getParameter("login");
		String clave = request.getParameter("clave");
		
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		out.write("<html>");
		out.write("<head><title>Logins</title></head>");
		out.write("<body><p>");
		out.write("//nombreAp\\");
		out.write("<br>");
		out.write("Usted se ha registrado con el usuario: "+ "//Prueba\\ login");
		out.write("</body>");
		out.write("</html>");
		out.close();
	}
    
}
