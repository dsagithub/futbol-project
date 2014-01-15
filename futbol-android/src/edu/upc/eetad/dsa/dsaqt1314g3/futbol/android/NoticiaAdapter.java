package edu.upc.eetad.dsa.dsaqt1314g3.futbol.android;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.Noticia;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NoticiaAdapter extends BaseAdapter{
	
	private ArrayList <Noticia> data;
	private LayoutInflater inflater;
	
	public NoticiaAdapter(Context context, ArrayList<Noticia> data) {
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
		return Long.parseLong(Integer.toString(((Noticia)getItem(position)).getIdNoticia()));
	}
	
	private static class ViewHolder {
		TextView tvTitulo;
		TextView tvContent;
		TextView tvDate;
		
		//si hubiera un button pues aqui... es una vista
	}

	@Override
	//mapeas los widgets
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_row_noticia, null);
			viewHolder = new ViewHolder();
			viewHolder.tvTitulo = (TextView) convertView
					.findViewById(R.id.tvTitulo);
			viewHolder.tvContent = (TextView) convertView
					.findViewById(R.id.tvContent);
			viewHolder.tvDate = (TextView) convertView
					.findViewById(R.id.tvDate);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		//obtener datos
		String titulo = data.get(position).getTitulo();
		String content = data.get(position).getContent();
		String date = SimpleDateFormat.getInstance().format(
				data.get(position).getLastModified());
		//poner datos
		viewHolder.tvTitulo.setText(titulo);
		viewHolder.tvContent.setText(content);
		viewHolder.tvDate.setText(date);
		return convertView;
	}

}

