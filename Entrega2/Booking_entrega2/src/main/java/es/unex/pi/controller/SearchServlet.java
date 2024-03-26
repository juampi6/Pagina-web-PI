package es.unex.pi.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.KeyPair;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.tomcat.jakartaee.commons.compress.archivers.zip.PKWareExtraHeader.HashAlgorithm;

import es.unex.pi.dao.*;
import es.unex.pi.dao.JDBCAccommodationDAOImpl;
import es.unex.pi.model.*;

/**
 * Servlet implementation class SearchServlet
 */
@WebServlet( urlPatterns = {"/SearchServlet.do"})
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(HttpServlet.class.getName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Connection conn = (Connection) getServletContext().getAttribute("dbConn");
		PropertyDAO propDao = new JDBCPropertyDAOImpl();
		propDao.setConnection(conn);
		
		
		
		List<Property> searchList = propDao.getAllByCityName(request.getParameter("lugar-alojamiento"));
		
		String disponibilidad = request.getParameter("disp");
		
		logger.info("Valor del parametro disponiblidad: "+ disponibilidad);
		
		if(searchList == null || searchList.isEmpty()) {
			request.setAttribute("messages", "No existe ningún alojamiento con los requisitos mencionados");
			
			RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/index.jsp");
			view.forward(request, response);
		}
		else {
			AccommodationDAO accomDao = new JDBCAccommodationDAOImpl();
			accomDao.setConnection(conn);
			
			
		
			HashMap<Property,List<Accommodation>> propAccomList = new HashMap<Property, List<Accommodation>>();
			
			
			for(Property itProperty : searchList) {
				List<Accommodation> listAccom = accomDao.getAllBySearchIdp(itProperty.getId());
				propAccomList.put(itProperty, listAccom);
			}
			
			RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/listaAlojamientos.jsp");
			
			request.setAttribute("listaAlojamientos", propAccomList);
			request.setAttribute("ciudad", request.getParameter("lugar-alojamiento"));
			view.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}