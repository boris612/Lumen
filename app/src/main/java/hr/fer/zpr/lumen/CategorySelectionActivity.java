package hr.fer.zpr.lumen;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import hr.fer.zpr.lumen.menus.CategoryAdapter;

public class CategorySelectionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_selection);

        final ListView listView = findViewById(R.id.listView01);
        List<String> categories = getIntent().getStringArrayListExtra("categories");

        final SharedPreferences pref = this.getSharedPreferences(getResources().getString(R.string.preference_file), Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();

        final ImageButton returnBtn = findViewById(R.id.returnButton);
        final Button allBtn = findViewById(R.id.allBtn);
        final Button noneBtn = findViewById(R.id.noneBtn);
        final Button saveBtn = findViewById(R.id.saveBtn);

        String[] cats = new String[categories.size()];
        cats = categories.toArray(cats);

        Set<String> categorySet = pref.getStringSet("category", null);
        if (categorySet == null) {
            categorySet = new TreeSet<>();
           //TODO:dohvacanje kategorija iz nove baze DBHelper helper = new DBHelper(getApplicationContext());
            //for (String cat : helper.getAllCategories())
            //    categorySet.add(cat);
            editor.putStringSet("category", categorySet);
            editor.commit();
        }

        final CategoryAdapter adapter = new CategoryAdapter(this, cats, categorySet);
        listView.setAdapter(adapter);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set<String> set = adapter.getChecked();

                if (set.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Odaberite najmanje jednu kategoriju.", Toast.LENGTH_SHORT).show();
                    return;
                }

                editor.putStringSet("category", set);
                editor.commit();

                onBackPressed();
            }
        });

        allBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.selectAll();
            }
        });
        noneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.unselectAll();
            }
        });
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
}
