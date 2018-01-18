package lumen.zpr.fer.hr.lumen;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.util.ArraySet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import lumen.zpr.fer.hr.lumen.menus.CategoryAdapter;
import lumen.zpr.fer.hr.lumen.menus.MainMenuActivity;

import static java.security.AccessController.getContext;

public class CategorySelectionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_selection);

        final ListView listView = findViewById(R.id.listView01);
        List<String> categories = getIntent().getStringArrayListExtra("categories");

        final SharedPreferences pref=this.getSharedPreferences(getResources().getString(R.string.preference_file), Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor=pref.edit();

        final ImageButton returnBtn= findViewById(R.id.returnButton);
        final Button allBtn = findViewById(R.id.allBtn);
        final Button noneBtn = findViewById(R.id.noneBtn);
        final Button saveBtn = findViewById(R.id.saveBtn);

        String[] cats = new String[categories.size()];
        cats = categories.toArray(cats);
        final CategoryAdapter adapter = new CategoryAdapter(this, cats, pref.getStringSet("category", null));
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
