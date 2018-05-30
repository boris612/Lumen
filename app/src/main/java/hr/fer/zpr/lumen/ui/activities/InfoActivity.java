package hr.fer.zpr.lumen.ui.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import javax.inject.Inject;

import hr.fer.zpr.lumen.R;
import hr.fer.zpr.lumen.dagger.activity.DaggerActivity;
import hr.fer.zpr.lumen.localization.LocalizationConstants;
import hr.fer.zpr.lumen.localization.LocalizationProvider;
import hr.fer.zpr.lumen.ui.wordgame.util.ViewConstants;

/**
 * Created by Zlatko on 21-Jan-18.
 */

public class InfoActivity extends DaggerActivity {

    @Inject
    LocalizationProvider provider;

    @Inject
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getLumenApplication().getApplicationComponent().inject(this);
        setContentView(R.layout.activity_info);
        String language=preferences.getString(ViewConstants.PREFERENCES_GUI_LANGUAGE,ViewConstants.DEFAULT_GUI_LANGUAGE);
        TextView title=findViewById(R.id.titleView);
        TextView text=findViewById(R.id.opis);
        title.setText(provider.getValueForLanguage(language, LocalizationConstants.INFO_TITLE_PROPERTY));
        text.setText(provider.getValueForLanguage(language,LocalizationConstants.INFO_TEXT_PROPERTY));
        text.setMovementMethod(new ScrollingMovementMethod());

        final ImageButton returnBtn = findViewById(R.id.mainMenuButton);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
