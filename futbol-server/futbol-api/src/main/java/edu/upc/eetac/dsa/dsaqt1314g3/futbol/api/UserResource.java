package edu.upc.eetac.dsa.dsaqt1314g3.futbol.api;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.DataSourceSPA;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.InternalServerException;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.MediaType;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.ServiceUnavailableException;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.UserNotFoundException;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model.User;

@Path("/users")
public class UserResource {

	private DataSource ds = DataSourceSPA.getInstance().getDataSource();

	@Context
	private SecurityContext security;

	@POST
	@Consumes(MediaType.FUTBOL_API_USER)
	@Produces(MediaType.FUTBOL_API_USER)
	public User createUser(User user) {
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServiceUnavailableException(e.getMessage());
		}
		try {
			Statement stmt = conn.createStatement();
			String sql = "insert into users values ('" + user.getUsername()
					+ "','" + user.getName() + "','" + user.getEmail() + "')";
			stmt.executeUpdate(sql);
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			throw new InternalServerException(e.getMessage());
		}
		return user;
	}

	@GET
	@Path("/{username}")
	@Produces(MediaType.FUTBOL_API_USER)
	public User getUser(@PathParam("username") String username) {
		CacheControl cc = new CacheControl();
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
			String sql = "select * from users where username='" + username
					+ "'";
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				user.setEmail(rs.getString("email"));
				user.setName(rs.getString("name"));
				user.setUsername(rs.getString("username"));
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
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServiceUnavailableException(e.getMessage());
		}

		try {
			Statement stmt = conn.createStatement();
			String sql = "select * from users where username='" + username
					+ "'";
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				sql = "update users set users.email='" + user.getEmail()
						+ "' where users.username='" + username + "'";
				user.setName(rs.getString("name"));
				user.setUsername(rs.getString("username"));
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
			sql = "delete from users where users.username='" + username + "'";
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