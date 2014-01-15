package edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
 
public class FutbolAPI {
	private final static String TAG = FutbolAPI.class.toString();
	private final static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
 
	public NoticiasCollection getNoticias(URL url) {
		NoticiasCollection noticias = new NoticiasCollection();
 
		HttpURLConnection urlConnection = null;
		try {
			urlConnection = (HttpURLConnection) url.openConnection();
 
			urlConnection.setRequestProperty("Accept",
					MediaType.FUTBOL_API_NOTICIAS_COLLECTION);
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);
			urlConnection.connect();
 
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
 
			JSONObject jsonObject = new JSONObject(sb.toString());
			JSONArray jsonLinks = jsonObject.getJSONArray("links");
			parseLinks(jsonLinks, noticias.getLinks());
 
			JSONArray jsonNoticias = jsonObject.getJSONArray("noticias");
			for (int i = 0; i < jsonNoticias.length(); i++) {
				JSONObject jsonNoticia = jsonNoticias.getJSONObject(i);
				Noticia noticia = parseNoticia(jsonNoticia);
 
				noticias.addNoticia(noticia);
			}
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} catch (ParseException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}
 
		return noticias;
	}
 
	public Noticia getNoticia(URL url) {
		Noticia noticia = new Noticia();
	 
		HttpURLConnection urlConnection = null;
		try {
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestProperty("Accept",
					MediaType.FUTBOL_API_NOTICIA);
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);
			urlConnection.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			JSONObject jsonObject = new JSONObject(sb.toString());
			JSONArray jsonLinks = jsonObject.getJSONArray("links");
			parseLinks(jsonLinks, noticia.getLinks());
			
			JSONObject jsonNoticia = new JSONObject(sb.toString());
			noticia = parseNoticia(jsonNoticia);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} catch (ParseException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		}finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}
	 
		return noticia;
	}
	
	public ClubCollection getClubs(URL url) {
		ClubCollection clubs = new ClubCollection();
 
		HttpURLConnection urlConnection = null;
		try {
			urlConnection = (HttpURLConnection) url.openConnection();
 
			urlConnection.setRequestProperty("Accept",
					MediaType.FUTBOL_API_CLUB_COLLECTION);
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);
			urlConnection.connect();
 
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
 
			JSONObject jsonObject = new JSONObject(sb.toString());
			JSONArray jsonLinks = jsonObject.getJSONArray("links");
			parseLinks(jsonLinks, clubs.getLinks());
 
			JSONArray jsonClubs = jsonObject.getJSONArray("clubs");
			for (int i = 0; i < jsonClubs.length(); i++) {
				JSONObject jsonClub = jsonClubs.getJSONObject(i);
				Club club = parseClub(jsonClub);
 
				clubs.addClub(club);
			}
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} catch (ParseException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}
 
		return clubs;
	}
	
	public Club getClub(URL url) {
		Club club = new Club();
 
		HttpURLConnection urlConnection = null;
		try {
			urlConnection = (HttpURLConnection) url.openConnection();
 
			urlConnection.setRequestProperty("Accept",
					MediaType.FUTBOL_API_CLUB);
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);
			urlConnection.connect();
 
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
 
			JSONObject jsonObject = new JSONObject(sb.toString());
			JSONArray jsonLinks = jsonObject.getJSONArray("links");
			parseLinks(jsonLinks, club.getLinks());
 
			JSONObject jsonClub = new JSONObject(sb.toString());
			club = parseClub(jsonClub);
 
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} catch (ParseException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		}finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}
 
		return club;
	}
	
	public EquipoCollection getEquipos(URL url) {
		EquipoCollection equipos = new EquipoCollection();
 
		HttpURLConnection urlConnection = null;
		try {
			urlConnection = (HttpURLConnection) url.openConnection();
 
			urlConnection.setRequestProperty("Accept",
					MediaType.FUTBOL_API_EQUIPO_COLLECTION);
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);
			urlConnection.connect();
 
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
 
			JSONObject jsonObject = new JSONObject(sb.toString());
			JSONArray jsonLinks = jsonObject.getJSONArray("links");
			parseLinks(jsonLinks, equipos.getLinks());
 
			JSONArray jsonEquipos = jsonObject.getJSONArray("equipos");
			for (int i = 0; i < jsonEquipos.length(); i++) {
				JSONObject jsonEquipo = jsonEquipos.getJSONObject(i);
				Equipo equipo = parseEquipo(jsonEquipo);
 
				equipos.addEquipo(equipo);
			}
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} catch (ParseException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}
 
		return equipos;
	}
	
	public Equipo getEquipo(URL url) {
		Equipo equipo = new Equipo();
 
		HttpURLConnection urlConnection = null;
		try {
			urlConnection = (HttpURLConnection) url.openConnection();
 
			urlConnection.setRequestProperty("Accept",
					MediaType.FUTBOL_API_EQUIPO);
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);
			urlConnection.connect();
 
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
 
			JSONObject jsonObject = new JSONObject(sb.toString());
			JSONArray jsonLinks = jsonObject.getJSONArray("links");
			parseLinks(jsonLinks, equipo.getLinks());
 
			JSONObject jsonEquipo = new JSONObject(sb.toString());
			equipo = parseEquipo(jsonEquipo);
 
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} catch (ParseException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		}finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}
 
		return equipo;
	}
	
	public JugadoresCollection getJugadores(URL url) {
		JugadoresCollection jugadores = new JugadoresCollection();
 
		HttpURLConnection urlConnection = null;
		try {
			urlConnection = (HttpURLConnection) url.openConnection();
 
			urlConnection.setRequestProperty("Accept",
					MediaType.FUTBOL_API_JUGADORES_COLLECTION);
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);
			urlConnection.connect();
 
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
 
			JSONObject jsonObject = new JSONObject(sb.toString());
			JSONArray jsonLinks = jsonObject.getJSONArray("links");
			parseLinks(jsonLinks, jugadores.getLinks());
 
			JSONArray jsonJugadores = jsonObject.getJSONArray("jugadores");
			for (int i = 0; i < jsonJugadores.length(); i++) {
				JSONObject jsonJugador = jsonJugadores.getJSONObject(i);
				Jugadores jugador = parseJugador(jsonJugador);
 
				jugadores.addJugadores(jugador);
			}
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} catch (ParseException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}
 
		return jugadores;
	}
 
	public Jugadores getJugador(URL url) {
		Jugadores jugador = new Jugadores();
	 
		HttpURLConnection urlConnection = null;
		try {
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestProperty("Accept",
					MediaType.FUTBOL_API_JUGADORES);
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);
			urlConnection.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			JSONObject jsonObject = new JSONObject(sb.toString());
			JSONArray jsonLinks = jsonObject.getJSONArray("links");
			parseLinks(jsonLinks, jugador.getLinks());
			
			JSONObject jsonJugador = new JSONObject(sb.toString());
			jugador = parseJugador(jsonJugador);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} catch (ParseException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		}finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}
	 
		return jugador;
	}
	
	public CalendarioCollection getCalendarios(URL url) {
		CalendarioCollection calendarios = new CalendarioCollection();
 
		HttpURLConnection urlConnection = null;
		try {
			urlConnection = (HttpURLConnection) url.openConnection();
 
			urlConnection.setRequestProperty("Accept",
					MediaType.FUTBOL_API_CALENDARIO_COLLECTION);
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);
			urlConnection.connect();
 
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
 
			JSONObject jsonObject = new JSONObject(sb.toString());
			JSONArray jsonLinks = jsonObject.getJSONArray("links");
			parseLinks(jsonLinks, calendarios.getLinks());
 
			JSONArray jsonCalendarios = jsonObject.getJSONArray("calendarios");
			for (int i = 0; i < jsonCalendarios.length(); i++) {
				JSONObject jsonCalendario = jsonCalendarios.getJSONObject(i);
				Calendario calendario = parseCalendario(jsonCalendario);
 
				calendarios.addCalendario(calendario);
			}
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} catch (ParseException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}
 
		return calendarios;
	}
 
	public Calendario getCalendario(URL url) {
		Calendario calendario = new Calendario();
	 
		HttpURLConnection urlConnection = null;
		try {
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestProperty("Accept",
					MediaType.FUTBOL_API_CALENDARIO);
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);
			urlConnection.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			JSONObject jsonObject = new JSONObject(sb.toString());
			JSONArray jsonLinks = jsonObject.getJSONArray("links");
			parseLinks(jsonLinks, calendario.getLinks());
			
			JSONObject jsonCalendario = new JSONObject(sb.toString());
			calendario = parseCalendario(jsonCalendario);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} catch (ParseException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		}finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}
	 
		return calendario;
	}
	
	public RetransmisionCollection getRetransmisiones(URL url) {
		RetransmisionCollection retransmisiones = new RetransmisionCollection();
 
		HttpURLConnection urlConnection = null;
		try {
			urlConnection = (HttpURLConnection) url.openConnection();
 
			urlConnection.setRequestProperty("Accept",
					MediaType.FUTBOL_API_RETRA_COLLECTION);
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);
			urlConnection.connect();
 
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
 
			JSONObject jsonObject = new JSONObject(sb.toString());
			JSONArray jsonLinks = jsonObject.getJSONArray("links");
			parseLinks(jsonLinks, retransmisiones.getLinks());
 
			JSONArray jsonRetransmisiones = jsonObject.getJSONArray("retransmisiones");
			for (int i = 0; i < jsonRetransmisiones.length(); i++) {
				JSONObject jsonRetransmision = jsonRetransmisiones.getJSONObject(i);
				Retransmision retransmision = parseRetransmision(jsonRetransmision);
 
				retransmisiones.addRetrans(retransmision);
			}
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} catch (ParseException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}
 
		return retransmisiones;
	}
 
	public Retransmision getRetransmision(URL url) {
		Retransmision retransmision = new Retransmision();
	 
		HttpURLConnection urlConnection = null;
		try {
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestProperty("Accept",
					MediaType.FUTBOL_API_RETRA);
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);
			urlConnection.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			JSONObject jsonObject = new JSONObject(sb.toString());
			JSONArray jsonLinks = jsonObject.getJSONArray("links");
			parseLinks(jsonLinks, retransmision.getLinks());
			
			JSONObject jsonRetransmision = new JSONObject(sb.toString());
			retransmision = parseRetransmision(jsonRetransmision);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} catch (ParseException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		}finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}
	 
		return retransmision;
	}
	
	public ComentariosCollection getComentarios(URL url) {
		ComentariosCollection comentarios = new ComentariosCollection();
 
		HttpURLConnection urlConnection = null;
		try {
			urlConnection = (HttpURLConnection) url.openConnection();
 
			urlConnection.setRequestProperty("Accept",
					MediaType.FUTBOL_API_COMENTARIOS_COLLECTION);
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);
			urlConnection.connect();
 
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
 
			JSONObject jsonObject = new JSONObject(sb.toString());
			JSONArray jsonLinks = jsonObject.getJSONArray("links");
			parseLinks(jsonLinks, comentarios.getLinks());
 
			JSONArray jsonComentarios = jsonObject.getJSONArray("comentarios");
			for (int i = 0; i < jsonComentarios.length(); i++) {
				JSONObject jsonComentario = jsonComentarios.getJSONObject(i);
				Comentario comentario = parseComentario(jsonComentario);
 
				comentarios.addComentario(comentario);
			}
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} catch (ParseException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}
 
		return comentarios;
	}
 
	public Comentario getComentario(URL url) {
		Comentario comentario = new Comentario();
	 
		HttpURLConnection urlConnection = null;
		try {
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestProperty("Accept",
					MediaType.FUTBOL_API_COMENTARIO);
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);
			urlConnection.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			JSONObject jsonObject = new JSONObject(sb.toString());
			JSONArray jsonLinks = jsonObject.getJSONArray("links");
			parseLinks(jsonLinks, comentario.getLinks());
			
			JSONObject jsonComentario = new JSONObject(sb.toString());
			comentario = parseComentario(jsonComentario);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} catch (ParseException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		}finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}
	 
		return comentario;
	}
	
	public CampeonatosCollection getCampeonatos(URL url) {
		CampeonatosCollection campeonatos = new CampeonatosCollection();
 
		HttpURLConnection urlConnection = null;
		try {
			urlConnection = (HttpURLConnection) url.openConnection();
 
			urlConnection.setRequestProperty("Accept",
					MediaType.FUTBOL_API_CAMPEONATOS_COLLECTION);
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);
			urlConnection.connect();
 
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
 
			JSONObject jsonObject = new JSONObject(sb.toString());
			JSONArray jsonLinks = jsonObject.getJSONArray("links");
			parseLinks(jsonLinks, campeonatos.getLinks());
 
			JSONArray jsonCampeonatos = jsonObject.getJSONArray("campeonatos");
			for (int i = 0; i < jsonCampeonatos.length(); i++) {
				JSONObject jsonCampeonato = jsonCampeonatos.getJSONObject(i);
				Campeonatos campeonato = parseCampeonato(jsonCampeonato);
 
				campeonatos.addCampeonatos(campeonato);
			}
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} catch (ParseException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}
 
		return campeonatos;
	}
 
	public Campeonatos getCampeonato(URL url) {
		Campeonatos campeonato = new Campeonatos();
	 
		HttpURLConnection urlConnection = null;
		try {
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestProperty("Accept",
					MediaType.FUTBOL_API_CAMPEONATOS);
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);
			urlConnection.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			JSONObject jsonObject = new JSONObject(sb.toString());
			JSONArray jsonLinks = jsonObject.getJSONArray("links");
			parseLinks(jsonLinks, campeonato.getLinks());
			
			JSONObject jsonCampeonato = new JSONObject(sb.toString());
			campeonato = parseCampeonato(jsonCampeonato);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} catch (ParseException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		}finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}
	 
		return campeonato;
	}
	
	private void parseLinks(JSONArray source, List<Link> links)
			throws JSONException {
		for (int i = 0; i < source.length(); i++) {
			JSONObject jsonLink = source.getJSONObject(i);
			Link link = new Link();
			link.setRel(jsonLink.getString("rel"));
			//link.setTitle(jsonLink.getString("title"));
			link.setType(jsonLink.getString("type"));
			link.setUri(jsonLink.getString("uri"));
			links.add(link);
		}
	}
 
	private Noticia parseNoticia(JSONObject source) throws JSONException,
			ParseException {
		Noticia noticia = new Noticia();
		noticia.setIdNoticia(source.getInt("idNoticia"));
		if (source.has("content"))
			noticia.setContent(source.getString("content"));
		String tsLastModified = source.getString("lastModified").replace("T",
				" ");
		noticia.setLastModified(sdf.parse(tsLastModified));
		noticia.setIdClub(source.getInt("idClub"));
		noticia.setTitulo(source.getString("titulo"));
		noticia.setMedia(source.getString("media"));
 
		JSONArray jsonStingLinks = source.getJSONArray("links");
		parseLinks(jsonStingLinks, noticia.getLinks());
		return noticia;
	}
	
	private Club parseClub(JSONObject source) throws JSONException,
	ParseException {
		Club club = new Club();
		club.setIdClub(source.getString("idClub"));
		club.setNombre(source.getString("nombre"));
 
		JSONArray jsonStingLinks = source.getJSONArray("links");
		parseLinks(jsonStingLinks, club.getLinks());
		return club;
	}
	
	private Equipo parseEquipo(JSONObject source) throws JSONException,
	ParseException {
		Equipo equipo = new Equipo();
		equipo.setIdEquipo(source.getString("idEquipo"));
		equipo.setNombre(source.getString("nombre"));
 
		JSONArray jsonStingLinks = source.getJSONArray("links");
		parseLinks(jsonStingLinks, equipo.getLinks());
		return equipo;
	}
	
	private Jugadores parseJugador(JSONObject source) throws JSONException,
	ParseException {
		Jugadores jugador = new Jugadores();
		jugador.setDni(source.getString("dni"));
		jugador.setNombre(source.getString("nombre"));
		jugador.setApellidos(source.getString("apellidos"));
		jugador.setIdequipo(source.getInt("idequipo"));
 
		JSONArray jsonStingLinks = source.getJSONArray("links");
		parseLinks(jsonStingLinks, jugador.getLinks());
		return jugador;
	}
	
	private Calendario parseCalendario(JSONObject source) throws JSONException,
	ParseException {
		Calendario calendario = new Calendario();
		calendario.setIdCampeonato(source.getString("idCampeonato"));
		calendario.setIdPartido(source.getString("idPartido"));
		calendario.setIdEquipoA(source.getString("idEquipoA"));
		calendario.setIdEquipoB(source.getString("idEquipoB"));
		calendario.setJornada(source.getString("jornada"));
		calendario.setFecha(source.getString("fecha"));
		calendario.setHora(source.getString("hora"));
 
		JSONArray jsonStingLinks = source.getJSONArray("links");
		parseLinks(jsonStingLinks, calendario.getLinks());
		return calendario;
	}
	
	private Retransmision parseRetransmision(JSONObject source) throws JSONException,
	ParseException {
		Retransmision retransmision = new Retransmision();
		retransmision.setId(source.getString("id"));
		retransmision.setIdPartido(source.getString("idPartido"));
		retransmision.setTiempo(source.getString("tiempo"));
		retransmision.setTexto(source.getString("texto"));
		retransmision.setMedia(source.getString("media"));
 
		JSONArray jsonStingLinks = source.getJSONArray("links");
		parseLinks(jsonStingLinks, retransmision.getLinks());
		return retransmision;
	}
	
	private Comentario parseComentario(JSONObject source) throws JSONException,
	ParseException {
		Comentario comentario = new Comentario();
		comentario.setIdComentario(source.getInt("idComentario"));
		comentario.setIdPartido(source.getString("idPartido"));
		comentario.setTiempo(source.getString("tiempo"));
		comentario.setTexto(source.getString("texto"));
		comentario.setMedia(source.getString("media"));
		comentario.setIdUsuario(source.getInt("idUsuario"));
 
		JSONArray jsonStingLinks = source.getJSONArray("links");
		parseLinks(jsonStingLinks, comentario.getLinks());
		return comentario;
	}
	
	private Campeonatos parseCampeonato(JSONObject source) throws JSONException,
	ParseException {
		Campeonatos campeonato = new Campeonatos();
		campeonato.setIdcampeonatos(source.getInt("idcampeonatos"));
		campeonato.setNombre(source.getString("nombre"));
 
		JSONArray jsonStingLinks = source.getJSONArray("links");
		parseLinks(jsonStingLinks, campeonato.getLinks());
		return campeonato;
	}
	
	
	public Noticia createNoticia(URL url, String titulo, String content) {
		Noticia noticia = new Noticia();
		noticia.setTitulo(titulo);
		noticia.setContent(content);
		
		HttpURLConnection urlConnection = null;
		try {
			JSONObject jsonNoticia = createJsonNoticia(noticia);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestProperty("Accept",
					MediaType.FUTBOL_API_NOTICIA);
			urlConnection.setRequestProperty("Content-Type",
					MediaType.FUTBOL_API_NOTICIA);
			urlConnection.setRequestMethod("POST");
			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);
			urlConnection.connect();
		
			PrintWriter writer = new PrintWriter(
					urlConnection.getOutputStream());
			writer.println(jsonNoticia.toString());
			writer.close();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		
			jsonNoticia = new JSONObject(sb.toString());
			noticia = parseNoticia(jsonNoticia);
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} catch (ParseException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}
		
		return noticia;
	}
	 
	private JSONObject createJsonNoticia(Noticia noticia) throws JSONException {
		JSONObject jsonNoticia = new JSONObject();
		jsonNoticia.put("titulo", noticia.getTitulo());
		jsonNoticia.put("content", noticia.getContent());
	 
		return jsonNoticia;
	}
	
	public Comentario createComentario(URL url, String tiempo, String texto) {
		Comentario comentario = new Comentario();
		comentario.setTexto(texto);
		comentario.setTiempo(tiempo);
		comentario.setIdUsuario(1);
		
		HttpURLConnection urlConnection = null;
		try {
			JSONObject jsonComentario = createJsonComentario(comentario);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestProperty("Accept",
					MediaType.FUTBOL_API_COMENTARIO);
			urlConnection.setRequestProperty("Content-Type",
					MediaType.FUTBOL_API_COMENTARIO);
			urlConnection.setRequestMethod("POST");
			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);
			urlConnection.connect();
		
			PrintWriter writer = new PrintWriter(
					urlConnection.getOutputStream());
			writer.println(jsonComentario.toString());
			writer.close();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		
			jsonComentario = new JSONObject(sb.toString());
			comentario = parseComentario(jsonComentario);
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} catch (ParseException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}
		
		return comentario;
	}
	 
	private JSONObject createJsonComentario(Comentario comentario) throws JSONException {
		JSONObject jsonComentario = new JSONObject();
		jsonComentario.put("texto", comentario.getTexto());
		jsonComentario.put("tiempo", comentario.getTiempo());
	 
		return jsonComentario;
	}
}
