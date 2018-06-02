package hr.fer.zpr.lumen.ui.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;

import hr.fer.zpr.lumen.R;
import hr.fer.zpr.lumen.dagger.activity.DaggerActivity;
import hr.fer.zpr.lumen.localization.LocalizationConstants;
import hr.fer.zpr.lumen.localization.LocalizationProvider;
import hr.fer.zpr.lumen.ui.activities.adapters.CategoryAdapter;
import hr.fer.zpr.lumen.ui.wordgame.util.ViewConstants;
import hr.fer.zpr.lumen.wordgame.interactor.ChangeCategoriesUseCase;
import wordgame.db.database.WordGameDatabase;

public class CategorySelectionActivity extends DaggerActivity {

    @Inject
    WordGameDatabase database;

    @Inject
    SharedPreferences pref;

    @Inject
    LocalizationProvider provider;

    @Inject
    ChangeCategoriesUseCase changeCategoriesUseCase;

    private Button allBtn;
    private Button noneBtn;
    private Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getLumenApplication().getApplicationComponent().inject(this);
        setContentView(R.layout.activity_category_selection);

        final ListView listView = findViewById(R.id.listView01);
        final SharedPreferences.Editor editor = pref.edit();

        final ImageButton returnBtn = findViewById(R.id.returnButton);
        String language = pref.getString(ViewConstants.PREFERENCES_GUI_LANGUAGE, ViewConstants.DEFAULT_GUI_LANGUAGE);
        allBtn = findViewById(R.id.allBtn);
        noneBtn = findViewById(R.id.noneBtn);
        saveBtn = findViewById(R.id.saveBtn);
        TextView view = findViewById(R.id.categoryTitle);
        view.setText(provider.getValueForLanguage(language, LocalizationConstants.CATEGORIES_PROPERTY));
        allBtn.setText(provider.getValueForLanguage(language, LocalizationConstants.SELECT_ALL_PROPETY));
        noneBtn.setText(provider.getValueForLanguage(language, LocalizationConstants.CLEAR_PROPERTY));
        saveBtn.setText(provider.getValueForLanguage(language, LocalizationConstants.SAVE_PROPERTY));


        Set<String> categorySet = pref.getStringSet(ViewConstants.PREFERENCES_CATEGORIES, null);
        if (categorySet == null) {
            categorySet = new TreeSet<>();
            categorySet.addAll(database.categoryDao().getAllCategories().blockingGet());
            editor.putStringSet(ViewConstants.PREFERENCES_CATEGORIES, categorySet);
            editor.commit();
        }
        String[] categories = database.categoryDao().getAllCategories().blockingGet().toArray(new String[0]);
        String[] names = new String[categories.length];
        for (int i = 0; i < categories.length; i++)
            names[i] = provider.getValueForLanguage(language, categories[i]);

        final CategoryAdapter adapter = new CategoryAdapter(this, categories, names, categorySet);
        listView.setAdapter(adapter);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set<String> set = adapter.getChecked();

                if (set.isEmpty()) {
                    Toast.makeText(getApplicationContext(), provider.getValueForLanguage(pref.getString(ViewConstants.PREFERENCES_LANGUAGE, ViewConstants.DEFAULT_LANGUAGE), LocalizationConstants.CATEGORY_PICK_MESSAGE), Toast.LENGTH_SHORT).show();
                    return;
                }

                editor.putStringSet(ViewConstants.PREFERENCES_CATEGORIES, set);
                editor.commit();

                changeCategoriesUseCase.execute(set);
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
