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

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.DataSourceSPA;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.InternalServerException;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.MediaType;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.ServiceUnavailableException;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.UserNotFoundException;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.links.UsersLinkBuilder;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model.User;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model.UserCollection;

@Path("/users")
public class UserResource {

	private DataSource ds = DataSourceSPA.getInstance().getDataSource();
	@Context
	private UriInfo uriInfo;
	@Context
	private SecurityContext security;

	@GET
	@Produces(MediaType.FUTBOl_API_USER_COLLECTION)
	public UserCollection getUsuarios(@QueryParam("pattern") String pattern,
			@QueryParam("offset") String offset,
			@QueryParam("length") String length) {

		if ((offset == null) || (length == null))
			throw new BadRequestException(
					"offset and length are mandatory parameters");
		int ioffset, ilength;
		int icount = 0;
		try {
			ioffset = Integer.parseInt(offset);
			if (ioffset < 0)
				throw new NumberFormatException();
		} catch (NumberFormatException e) {
			throw new BadRequestException(
					"offset must be an integer greater or equal than 0.");
		}
		try {
			ilength = Integer.parseInt(length);
			if (ilength < 1)
				throw new NumberFormatException();
		} catch (NumberFormatException e) {
			throw new BadRequestException(
					"length must be an integer greater or equal than 1.");
		}
		UserCollection usuarios = new UserCollection();
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServiceUnavailableException(e.getMessage());
		}
		try {
			Statement stmt = conn.createStatement();
			String sql = null;
			if (pattern != null) {
				sql = "select * from usuarios where username like '%" + pattern
						+ "%'";
			} else {
				sql = "select * from usuarios LIMIT " + offset + "," + length;
			}
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				User usuario = new User();
				usuario.setIdusuario(rs.getString("idUsuario"));
				usuario.setUsername(rs.getString("username"));
				usuario.setEmail(rs.getString("email"));
				usuario.setName(rs.getString("nombre"));
				usuario.addLink(UsersLinkBuilder.buildURIUser(uriInfo, "self",
						usuario.getUsername()));
				usuarios.addUser(usuario);
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
			usuarios.addLink(UsersLinkBuilder.buildURIUserList(uriInfo,
					prevoffset, length, pattern, "prev"));
		}
		usuarios.addLink(UsersLinkBuilder.buildURIUserList(uriInfo, offset,
				length, pattern, "self"));
		String nextoffset = "" + (ioffset + ilength);
		if (ilength <= icount) {
			usuarios.addLink(UsersLinkBuilder.buildURIUserList(uriInfo,
					nextoffset, length, pattern, "next"));
		}
		return usuarios;
	}

	@POST
	@Consumes(MediaType.FUTBOL_API_USER)
	@Produces(MediaType.FUTBOL_API_USER)
	public User createUser(User user) {
		if (user == null) {
			throw new BadRequestException("error");
		} else {
			if (user.getUsername().length() == 0) {
				throw new BadRequestException("faltan parametros");
			}
			if (user.getName().length() == 0) {
				throw new BadRequestException("faltan parametros");
			}
			if (user.getEmail().length() == 0) {
				throw new BadRequestException("faltan parametros");
			}
			if (user.getPassword().length() == 0) {
				throw new BadRequestException("faltan parametros");
			}
		}
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServiceUnavailableException(e.getMessage());
		}
		try {
			Statement stmt = conn.createStatement();
			String sql = "select * from usuarios where username='"
					+ user.getUsername() + "'";
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				throw new UserExisteException();
			}
			stmt.close();
		} catch (SQLException e) {
			throw new InternalServerException(e.getMessage());
		}
		try {
			Statement stmt = conn.createStatement();
			String sql = "insert into usuarios (username,email,nombre,password,role) values ('"
					+ user.getUsername()
					+ "','"
					+ user.getEmail()
					+ "','"
					+ user.getName()
					+ "',MD5('"
					+ user.getPassword()
					+ "'),'registered')";
			stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				int isUsuario = rs.getInt(1);
				user.setIdusuario(Integer.toString(isUsuario));
				user.setPassword(null);
				user.addLink(UsersLinkBuilder.buildURIUser(uriInfo, "self",
						user.getUsername()));
				rs.close();
				stmt.close();
				conn.close();

			} else {
				throw new UserNotFoundException();
			}
		} catch (SQLException e) {
			throw new InternalServerException(e.getMessage());
		}
		return user;
	}

	@GET
	@Path("/{username}")
	@Produces(MediaType.FUTBOL_API_USER)
	public User getUser(@PathParam("username") String username) {
		User user = new User();
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServiceUnavailableException(e.getMessage());
		}
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			String sql = "select * from usuarios where username='" + username
					+ "'";
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				user.setEmail(rs.getString("email"));
				user.setName(rs.getString("nombre"));
				user.setUsername(rs.getString("username"));
				user.setIdusuario(rs.getString("idUsuario"));
				user.addLink(UsersLinkBuilder.buildURIUser(uriInfo, "self",
						username));
			} else {
				throw new UserNotFoundException();
			}
		} catch (SQLException e) {
			throw new InternalServerException(e.getMessage());
		} finally {
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				throw new InternalServerException(e.getMessage());
			}
		}
		return user;
	}

	@PUT
	@Path("/{username}")
	@Produces(MediaType.FUTBOL_API_USER)
	@Consumes(MediaType.FUTBOL_API_USER)
	public User updateUSER(@PathParam("username") String username, User user) {
		if (security.getUserPrincipal().getName() != username) {
			if (!security.isUserInRole("administrator")) {
				throw new ForbiddenException("DENEGADO: FALTA PERMISOS");
			}
		}
		if (user == null) {
			throw new BadRequestException("error");
		} else {
			if (user.getName().length() == 0) {
				throw new BadRequestException("faltan parametros");
			}
			if (user.getEmail().length() == 0) {
				throw new BadRequestException("faltan parametros");
			}
		}
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServiceUnavailableException(e.getMessage());
		}

		try {
			Statement stmt = conn.createStatement();
			String sql = "select * from usuarios where username='" + username
					+ "'";
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				if (user.getPassword()== null){
					sql = "update usuarios set usuarios.email='" + user.getEmail()
							+ "', usuarios.nombre='" + user.getName()
							+ "' where usuarios.username='" + username + "'";
				}else{
					sql = "update usuarios set usuarios.email='" + user.getEmail()
						+ "', usuarios.nombre='" + user.getName()
						+ "', usuarios.password=MD5('" + user.getPassword()
						+ "') where usuarios.username='" + username + "'";
				}
				user.setUsername(rs.getString("username"));
				user.setIdusuario(rs.getString("idUsuario"));
				user.addLink(UsersLinkBuilder.buildURIUser(uriInfo, "self",
						user.getUsername()));
				stmt.executeUpdate(sql);
			} else {
				throw new UserNotFoundException();
			}
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			throw new InternalServerException(e.getMessage());
		}
		return user;
	}

	@DELETE
	@Path("/{username}")
	public void deleteUser(@PathParam("username") String username) {
		if (security.getUserPrincipal().getName() != username) {
			if (!security.isUserInRole("administrator")) {
				throw new ForbiddenException("DENEGADO: FALTA PERMISOS");
			}
		}
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServiceUnavailableException(e.getMessage());
		}
		Statement stmt = null;
		String sql;
		try {
			stmt = conn.createStatement();
			sql = "delete from usuarios where usuarios.username='" + username
					+ "'";
			int rs2 = stmt.executeUpdate(sql);
			if (rs2 == 0)
				throw new UserNotFoundException();
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
}