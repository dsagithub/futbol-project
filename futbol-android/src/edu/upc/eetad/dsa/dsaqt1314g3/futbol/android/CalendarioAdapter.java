package edu.upc.eetad.dsa.dsaqt1314g3.futbol.android;

import java.util.ArrayList;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.Calendario;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CalendarioAdapter extends BaseAdapter{
	
	private ArrayList <Calendario> data;
	private LayoutInflater inflater;
	
	public CalendarioAdapter(Context context, ArrayList<Calendario> data) {
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
		return Long.parseLong(((Calendario)getItem(position)).getIdPartido());
	}
	
	private static class ViewHolder {
		TextView tvJornada;
		TextView tvEquipoA;
		TextView tvEquipoB;
		TextView tvFecha;
		
	}

	@Override
	//mapeas los widgets
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_row_calendario, null);
			viewHolder = new ViewHolder();
			viewHolder.tvJornada = (TextView) convertView
					.findViewById(R.id.tvJornada);
			viewHolder.tvEquipoA = (TextView) convertView
					.findViewById(R.id.tvEquipoA);
			viewHolder.tvEquipoB = (TextView) convertView
					.findViewById(R.id.tvEquipoB);
			viewHolder.tvFecha = (TextView) convertView
					.findViewById(R.id.tvFecha);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		//obtener datos
		String jornada = data.get(position).getJornada();
		String equipoA = data.get(position).getIdEquipoA();
		String equipoB = data.get(position).getIdEquipoB();
		String fecha = data.get(position).getFecha();
		//poner datos
		viewHolder.tvJornada.setText("Jornada: " + jornada);
		viewHolder.tvEquipoA.setText(equipoA);
		viewHolder.tvEquipoB.setText(equipoB);
		viewHolder.tvFecha.setText(fecha);
		return convertView;
	}

}

