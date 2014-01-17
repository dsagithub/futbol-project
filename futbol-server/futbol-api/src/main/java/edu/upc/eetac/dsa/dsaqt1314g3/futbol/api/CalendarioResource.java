package edu.upc.eetac.dsa.dsaqt1314g3.futbol.api;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.links.CalendarioLinkBuilder;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.links.ClubLinkBuilder;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.links.ComentariosLinkBuilder;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model.Calendario;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model.CalendarioCollection;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model.Club;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model.ClubCollection;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.BadRequestException;

@Path("/campeonato/{idCampeonato}/calendario/")
public class CalendarioResource {
	private DataSource ds = DataSourceSPA.getInstance().getDataSource();

	@Context
	private UriInfo uriInfo;
	@Context
	private SecurityContext security;
	
	@GET
	@Produces(MediaType.FUTBOL_API_CALENDARIO_COLLECTION)
	public CalendarioCollection getcalendarios(@PathParam("idCampeonato") String idCampeonato,
			@QueryParam("offset") String offset,
			@QueryParam("length") String length,
			@QueryParam("pattern") String pattern) {
		if ((offset == null) || (length == null))
			throw new BadRequestException("Indica un offset y un length ");
		int ioffset, ilength, icount = 0;
		try {
			ioffset = Integer.parseInt(offset);
			if (ioffset < 0)
				throw new NumberFormatException();
		} catch (NumberFormatException e) {
			throw new BadRequestException(
					"Offset es un entero igual o mayor de 0.");
		}
		try {
			ilength = Integer.parseInt(length);
			if (ilength < 1)
				throw new NumberFormatException();
		} catch (NumberFormatException e) {
			throw new BadRequestException(
					"Lenght ha de ser entero igual o mayor a 1.");
		}

		CalendarioCollection Calendarios = new CalendarioCollection();

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServiceUnavailableException(e.getMessage());
		}

		try {
			Statement stmt = conn.createStatement();
			String sql = null;
//			if (pattern != null) {
//				sql = "select * from Calendario where PONAQUILOQUENECESITES like '%" + pattern
//						+ "%' LIMIT " + offset + "," + length;
//			} 
			if (pattern != null) {
				sql = "select * from Calendario where idEquipoA like '%" + pattern
						+ "%' OR idEquipoB like '%"+pattern
						+"%'LIMIT " + offset + "," + length;
			} 
			else
			{
				sql = "select * from Calendario where idCampeonato="+ idCampeonato 
						+" LIMIT " + offset + "," + length;
			}
				
			
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Calendario calendario = new Calendario();
				calendario.setIdCampeonato(rs.getString("idCampeonato"));
				calendario.setIdPartido(rs.getString("idPartido"));
				calendario.setIdEquipoA(rs.getString("idEquipoA"));
				calendario.setIdEquipoB(rs.getString("idEquipoB"));
				calendario.setJornada(rs.getString("jornada"));
				calendario.setFecha(rs.getString("fecha"));
				calendario.setHora(rs.getString("hora"));
				calendario.addLink(CalendarioLinkBuilder.buildURICalendarios(uriInfo, "0", "15", pattern, "Self", calendario.getIdCampeonato()));
				Calendarios.addCalendario(calendario);
				icount++;
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			throw new InternalServerException(e.getMessage());
		}
		
		if (ioffset != 0) {
			String prevoffset = "" + (ioffset - ilength);
			Calendarios.addLink(CalendarioLinkBuilder.buildURICalendarios(uriInfo, prevoffset, length, null, "prev",idCampeonato));
			
			}
		Calendarios.addLink(CalendarioLinkBuilder.buildURICalendarios(uriInfo, offset, length, null, "self", idCampeonato));

			String nextoffset = "" + (ioffset + ilength);
			if (ilength <= icount) {
				Calendarios.addLink(CalendarioLinkBuilder.buildURICalendarios(uriInfo, nextoffset, length, null, "next", idCampeonato));

		}
		
		return Calendarios;
	}
	
	@GET
	@Path("{idPartido}")
	@Produces(MediaType.FUTBOL_API_CALENDARIO)
	public Calendario getCalendario(@PathParam("idPartido") String idPartido){
		
		Calendario calendario = new Calendario();
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServiceUnavailableException(e.getMessage());
		}
		Statement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = conn.createStatement();
			String sql = "select * from Calendario where idPartido=" + idPartido;
			rs = stmt.executeQuery(sql);
			if (rs.next())
			{
				calendario.setIdCampeonato(rs.getString("idCampeonato"));
				calendario.setIdPartido(rs.getString("idPartido"));
				calendario.setIdEquipoA(rs.getString("idEquipoA"));
				calendario.setIdEquipoB(rs.getString("idEquipoB"));
				calendario.setJornada(rs.getString("jornada"));
				calendario.setFecha(rs.getString("fecha"));
				calendario.setHora(rs.getString("hora"));
				calendario.addLink(CalendarioLinkBuilder.buildURICalendarioId(uriInfo, "self",calendario.getIdCampeonato(), calendario.getIdPartido()));
				calendario.addLink(CalendarioLinkBuilder.buildURIRetransmision(uriInfo, "Retransmisiones", "0", "15",calendario.getIdCampeonato(), idPartido));
				calendario.addLink(CalendarioLinkBuilder.buildURIComentarios(uriInfo, "comentarios", "0", "15", calendario.getIdCampeonato(), idPartido));
				calendario.addLink(CalendarioLinkBuilder.buildURICampeonatos(uriInfo, "campeonatos", calendario.getIdCampeonato()));
			
			}
			else
			{
				throw new CalendarioNotFoundException("No existe calendario para esa id de partido");
			}
		}
		catch (SQLException e) 
		{
			throw new InternalServerException(e.getMessage());
		}
		 finally 
		 {
			try 
			{
					stmt.close();
					conn.close();
				} catch (SQLException e) {
					throw new InternalServerException(e.getMessage());
				}
			}
		
		return calendario;
		
		}
	@POST
	@Consumes(MediaType.FUTBOL_API_CALENDARIO)
	@Produces(MediaType.FUTBOL_API_CALENDARIO)
	public Calendario createCalendario(Calendario calendario, @PathParam("idCampeonato") String idCampeonato)
	{
		if (!security.isUserInRole("administrator"))
		{
			throw new ForbiddenException("Solo administrador puede realizar esta acción");
		}
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServiceUnavailableException(e.getMessage());
		}
		if ((calendario.getIdEquipoA().length()==0) || (calendario.getIdEquipoB().length()==0) || (calendario.getFecha().length()==0) || (calendario.getJornada().length()==0) || (calendario.getHora().length()==0) )
		{
			throw new BadRequestException("Ninguno de los parámetros puede estar vacío");
		}
		
	
		try{
			Statement stmt = conn.createStatement();
			String sql = "insert into Calendario (idCampeonato, idEquipoA, idEquipoB, jornada, fecha, hora ) values('"
			+idCampeonato
			+ "', '"
			+ calendario.getIdEquipoA()
			+ "', '"
			+ calendario.getIdEquipoB()
			+ "', '"
			+calendario.getJornada()
			+ "', '"
			+calendario.getFecha()
			+ "', '"
			+calendario.getHora()
			+"')";
			stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
			int  idPartido = rs.getInt(1);
		calendario.setIdPartido(Integer.toString(idPartido));
		calendario.addLink(CalendarioLinkBuilder.buildURICalendarioId(uriInfo, "self",idCampeonato, calendario.getIdPartido()));


			
			rs.close();
			stmt.close();
			conn.close();
			}
			else {
				throw new CalendarioNotFoundException("no encontrado");
			}
			
			
			}
		 catch (SQLException e) {
				throw new InternalServerException(e.getMessage());
			}
		return calendario;
		}

	@DELETE
	@Path("{idPartido}")
	public void deleteSting(@PathParam("idPartido") String id) {
		Connection conn = null;
		
		if (!security.isUserInRole("administrator"))
		{
			throw new ForbiddenException("Solo administrador puede realizar esta acción");
		}
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServiceUnavailableException(e.getMessage());
		}
		Statement stmt = null;
		String sql;
		try {
			stmt = conn.createStatement();
			sql = "delete from Calendario where idPartido=" + id;
			int rs2 = stmt.executeUpdate(sql);
			if (rs2 == 0)
				throw new CalendarioNotFoundException("No hay partido en el calendario con esta id");

		} catch (SQLException e) {
			throw new InternalServerException(e.getMessage());
		} finally {
			try {
				conn.close();
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	@PUT
	@Path("{idPartido}")
	@Consumes(MediaType.FUTBOL_API_CALENDARIO)
	@Produces(MediaType.FUTBOL_API_CALENDARIO)
	public Calendario updateCalendario(@PathParam("idPartido") String id,@PathParam("idCampeonato") String idCampeonato, Calendario calendario) {
	
		if (!security.isUserInRole("administrator"))
		{
			throw new ForbiddenException("Solo administrador puede realizar esta acción");
		}
		
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServiceUnavailableException(e.getMessage());
		}
		if ((calendario.getIdEquipoA().length()==0) || (calendario.getIdEquipoB().length()==0) || (calendario.getFecha().length()==0) || (calendario.getJornada().length()==0) || (calendario.getHora().length()==0) )
		{
			throw new BadRequestException("Ninguno de los parámetros puede estar vacío");
		}
		try {
			Statement stmt = conn.createStatement();
			String sql = "update Calendario set Calendario.idEquipoA='" + calendario.getIdEquipoA()
					+ "',Calendario.idEquipoB='" + calendario.getIdEquipoB()
					+ "',Calendario.jornada='" + calendario.getJornada()
					+ "',Calendario.fecha='" + calendario.getFecha()
					+ "',Calendario.hora='" + calendario.getHora()
					
					  + "' where Calendario.idPartido=" + id;
			int rs2 = stmt.executeUpdate(sql);
			if (rs2 == 0)
				throw new CalendarioNotFoundException("No existe calendario para actualizar");
			calendario.setIdPartido(id);
			calendario.addLink(CalendarioLinkBuilder.buildURICalendarioId(uriInfo, "self",idCampeonato, calendario.getIdPartido()));
		
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			throw new InternalServerException(e.getMessage());
		}
		return calendario;
	}
	
}
