package lumen.zpr.fer.hr.lumen;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Collection;
import java.util.List;

import lumen.zpr.fer.hr.lumen.menus.MainMenuActivity;

public class CategorySelectionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_selection);

        final RadioGroup radioGroup = findViewById(R.id.categoryGroup);
        List<String> categories = getIntent().getStringArrayListExtra("categories");

        final SharedPreferences pref=this.getSharedPreferences(getResources().getString(R.string.preference_file), Context.MODE_PRIVATE);
        final   SharedPreferences.Editor editor=pref.edit();

        final ImageButton returnBtn= findViewById(R.id.returnButton);
        String currentCategory = pref.getString("category","sve");
        System.out.println("Trenutna kategorija "+ currentCategory);
        categories.add("sve");
        for (String category : categories) {
            final RadioButton button = new RadioButton(this);
            button.setText(category);
            button.setTextSize(30);

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

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
