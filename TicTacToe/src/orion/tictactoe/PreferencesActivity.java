package orion.tictactoe;

import com.actionbarsherlock.app.ActionBar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;

public class PreferencesActivity extends SherlockPreferenceActivity implements OnPreferenceChangeListener {

    public static final String PLAYER_PIECE_KEY = "players_piece";
    public static final String COMPUTER_PLAY_FIRST_KEY = "computer_play_first";
    
    private ListPreference mplayerPiecePreference;
    
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //There is support for Preference fragments in support.v4
        addPreferencesFromResource(R.xml.preferences);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        //Update the summary for teh player piece
        mplayerPiecePreference =  (ListPreference)findPreference(PLAYER_PIECE_KEY);
        mplayerPiecePreference.setOnPreferenceChangeListener(this);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String playerPieceValue = sharedPrefs.getString(PLAYER_PIECE_KEY, getString(R.string.player_piece_default_setting));
        updatePlayerPieceSummary(playerPieceValue);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        // Respond to the action bar's Up/Home button
        case android.R.id.home:
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if(preference.getKey().equals(PLAYER_PIECE_KEY)){
            updatePlayerPieceSummary((String)newValue);
        }
        return true;
    }
   
    private void updatePlayerPieceSummary(String currentValue){
        int preferenceIndex = mplayerPiecePreference.findIndexOfValue(currentValue);
        String mappedValue = getResources().getStringArray(R.array.player_piece_entries)[preferenceIndex];
        String newSummary = String.format(getResources().
                getString(R.string.player_piece_settings_summary),
                mappedValue);
        mplayerPiecePreference.setSummary(newSummary);
    }
}
