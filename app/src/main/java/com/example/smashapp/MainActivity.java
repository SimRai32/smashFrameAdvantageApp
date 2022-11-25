package com.example.smashapp;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.smashapp.databinding.ActivityMainBinding;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("Frame Advantage on Shield");
        final String[] character = {"Bowser"};
        final String[] move = {"Jab 1"};
        TextView frames = (TextView)findViewById(R.id.frames);
        String data = "";
        // retrieving the data stored in the data.json file
        try {
            // retrieves data as bytes
            InputStream inputStream = getAssets().open("data.json");
            // reads the bytes of the input stream until it needs to block the execution
            int size = inputStream.available();
            // these bytes are stored in buffer
            byte[] buffer = new byte[size];
            // reads the bytes from the buffer variable
            inputStream.read(buffer);
            // info is stored as a string in data
            data = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // finds the drop down menu for characters
        Spinner spinnerCharacters=findViewById(R.id.characters);
        // renders all the items in chars
        ArrayAdapter<CharSequence>adapterChars=ArrayAdapter.createFromResource(this, R.array.chars, android.R.layout.simple_spinner_item);
        adapterChars.setDropDownViewResource(android.R.layout.simple_spinner_item);
        // attaching the chars array adapter to the characters spinner
        spinnerCharacters.setAdapter(adapterChars);
        // sets a new valuable to the variable character when a new item is selected in the character spinner
        spinnerCharacters.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                character[0] = spinnerCharacters.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Spinner spinnerMoves=findViewById(R.id.allMoves);
        // renders all the items in moves
        ArrayAdapter<CharSequence>adapterMoves=ArrayAdapter.createFromResource(this, R.array.moves, android.R.layout.simple_spinner_item);
        adapterMoves.setDropDownViewResource(android.R.layout.simple_spinner_item);
        // attaching the chars array adapter to the characters spinner
        spinnerMoves.setAdapter(adapterMoves);
        // sets a new valuable to the variable move when a new item is selected in the moves spinner
        spinnerMoves.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                move[0] = spinnerMoves.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Button button = (Button) findViewById(R.id.find);
        String finalData = data;
        // calculates the frame advantage whenever the button is clicked
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    frames.setTextSize(40);
                    // sets string to a json object
                    JSONObject json = new JSONObject(finalData);
                    // finds the character info as a json object
                    JSONObject charInfo = json.getJSONObject(character[0]);
                    // finds the move info as a json object
                    JSONObject moveInfo = charInfo.getJSONObject(move[0]);
                    int dur = moveInfo.getInt("duration");
                    int dam = moveInfo.getInt("damage");
                    // calculates the frame advantage
                    int calc = (int) Math.floor( ( dam + 4.45 ) / 2.235 ) - dur;
                    String plus = "";
                    // converts the calculated frame advantage into a string
                    String test = Integer.toString(calc);
                    if (calc < 0) {
                        frames.setTextColor(Color.RED);
                    }
                    else if (calc == 0) {
                        frames.setTextColor(Color.GRAY);

                    }
                    else {
                        frames.setTextColor(Color.GREEN);
                        plus = "+";
                    }
                    frames.setText(plus + test);

                } catch (Exception e) {
                    frames.setTextColor(Color.RED);
                    frames.setTextSize(20);
                    frames.setText("This move does not exist :(");
                    e.printStackTrace();
                }
            }
        });
    }
}

