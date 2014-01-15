package edu.upc.eetad.dsa.dsaqt1314g3.futbol.android;

import java.util.ArrayList;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.Club;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class ClubAdapter extends BaseAdapter{
	
	private ArrayList <Club> data;
	private LayoutInflater inflater;
	
	public ClubAdapter(Context context, ArrayList<Club> data) {
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
		return Long.parseLong(((Club)getItem(position)).getIdClub());
	}
	
	private static class ViewHolder {
		TextView tvNombre;
		//si hubiera un button pues aqui... es una vista
	}

	@Override
	//mapeas los widgets
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_row_club, null);
			viewHolder = new ViewHolder();
			viewHolder.tvNombre = (TextView) convertView
					.findViewById(R.id.tvNombre);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		//obtener datos
		String nombre = data.get(position).getNombre();
		//poner datos
		viewHolder.tvNombre.setText(nombre);
		return convertView;
	}

}

