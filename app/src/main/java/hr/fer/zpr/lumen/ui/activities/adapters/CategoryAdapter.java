package hr.fer.zpr.lumen.ui.activities.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.Set;
import java.util.TreeSet;

import hr.fer.zpr.lumen.R;



public class CategoryAdapter extends ArrayAdapter implements CompoundButton.OnCheckedChangeListener {
    Context context;
    String[] categories;
    String[] categoriesNames;

    SparseBooleanArray states;
    Set<String> set;

    public CategoryAdapter(Context cont, String[] res, String[] names, Set<String> set) {
        super(cont, R.layout.category_option, res);
        context = cont;
        categories = res;
        categoriesNames = names;
        states = new SparseBooleanArray(res.length);
        this.set = set;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.category_option, parent, false);
        CheckBox box = convertView.findViewById(R.id.checkBox01);

        box.setText(categoriesNames[position]);

        box.setTag(position);
        if (states.indexOfKey(position) < 0) {
            box.setChecked(set.contains(categories[position]));
        } else {
            box.setChecked(states.get(position));
        }
        states.put(position, box.isChecked());

        box.setOnCheckedChangeListener(this);

        return convertView;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        states.put((Integer) buttonView.getTag(), isChecked);
        System.out.println(buttonView.getTag());
    }

    public void selectAll() {
        for (int i = 0; i < states.size(); i++) {
            int key = states.keyAt(i);
            states.put(key, true);
        }
        notifyDataSetChanged();
    }

    public void unselectAll() {
        for (int i = 0; i < states.size(); i++) {
            int key = states.keyAt(i);
            states.put(key, false);
        }
        notifyDataSetChanged();
    }

    public Set<String> getChecked() {
        Set<String> set = new TreeSet<>();
        for (int i = 0; i < states.size(); i++) {
            if (states.valueAt(i)) {
                set.add(categories[states.keyAt(i)]);
            }
        }

        return set;
    }
}
