package edu.upc.eetad.dsa.dsaqt1314g3.futbol.android;

import java.util.ArrayList;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.Jugadores;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class JugadorAdapter extends BaseAdapter{
	
	private ArrayList <Jugadores> data;
	private LayoutInflater inflater;
	
	public JugadorAdapter(Context context, ArrayList<Jugadores> data) {
		super();
		inflater = LayoutInflater.from(context);
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size();
	}
	 
	@Override
	public Object getItem(int position) {
		return data.get(position);
	}
	 
	@Override
	public long getItemId(int position) {
		return Long.parseLong(((Jugadores)getItem(position)).getDni());
	}
	
	private static class ViewHolder {
		TextView tvNombre;
		
	}

	@Override
	//mapeas los widgets
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_row_jugdor, null);
			viewHolder = new ViewHolder();
			viewHolder.tvNombre = (TextView) convertView
					.findViewById(R.id.tvNombre);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		//obtener datos
		String nombre = data.get(position).getNombre();
		String apellido = data.get(position).getApellidos();
		//poner datos
		viewHolder.tvNombre.setText(nombre + " " + apellido);
		return convertView;
	}

}

