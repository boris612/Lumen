package lumen.zpr.fer.hr.lumen;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Collection;
import java.util.List;
import java.util.Set;
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
        String currentCategory = pref.getString("category","sve");
        System.out.println("Trenutna kategorija "+ currentCategory);
        if (!categories.contains("sve")) categories.add("sve");

        String[] cats = new String[categories.size()];
        cats = categories.toArray(cats);
        CategoryAdapter adapter = new CategoryAdapter(this, cats);
        listView.setAdapter(adapter);

        /*
        for (String category : categories) {
            final RadioButton button = new RadioButton(this);

            button.setText(category);
            button.setTextSize(30);
            button.setLayoutDirection(RadioButton.LAYOUT_DIRECTION_RTL);
            button.setBackgroundResource(R.drawable.custom_divider);
            radioGroup.addView(button);
            button.getLayoutParams().width=RadioGroup.LayoutParams.MATCH_PARENT;

            if (category.equals(currentCategory)) button.setChecked(true);

        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Button current = findViewById(checkedId);
                System.out.println(current.getText().toString());
                editor.putString("category",current.getText().toString());
                editor.commit();
            }

        });
        */

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
}
