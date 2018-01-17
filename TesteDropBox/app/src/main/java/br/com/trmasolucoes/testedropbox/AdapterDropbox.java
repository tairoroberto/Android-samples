package br.com.trmasolucoes.testedropbox;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dropbox.client2.DropboxAPI;

import java.util.List;

/**
 * Created by tairo on 12/07/15.
 */
public class AdapterDropbox extends BaseAdapter{
    private Context context;
    private LayoutInflater inflater;
    private List<DropboxAPI.Entry> list;

    public AdapterDropbox(Context context, List<DropboxAPI.Entry> list) {
        this.context = context;
        this.list = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        String html = "";
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item,null);
            holder = new ViewHolder();
            convertView.setTag(holder);

            holder.txtDropbox = (TextView) convertView.findViewById(R.id.txtDropbox);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        html = "<b>Path: <b />"+ list.get(position).path+" <br />";
        html += "<b>Size: <b />"+ list.get(position).size+" <br />";
        html += "<b>File Name: <b />"+ list.get(position).fileName()+" <br />";

        holder.txtDropbox.setText(Html.fromHtml(html));

        return convertView;
    }

    private class ViewHolder{
        public TextView txtDropbox;
    }
}
