package lumen.zpr.fer.hr.lumen.menus;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import lumen.zpr.fer.hr.lumen.R;

/**
 * Created by Kix on 17.1.2018..
 */

public class CategoryAdapter extends ArrayAdapter {
    Context context;
    String[] categories;

    public CategoryAdapter(Context cont, String[] res) {
        super(cont, R.layout.category_option, res);
        context = cont;
        categories = res;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.category_option, parent, false);
        CheckBox box = convertView.findViewById(R.id.checkBox01);

        box.setText(categories[position]);

        return convertView;
    }
}
