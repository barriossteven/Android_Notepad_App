package com.example.barrios.notepad;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class NoteActivity extends AppCompatActivity {

    private Button saveButton;
    private TextView noteName;
    private EditText noteText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        saveButton = (Button) findViewById(R.id.saveButton);
        noteName = (TextView) findViewById(R.id.noteName);
        noteText = (EditText) findViewById(R.id.noteText);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteName.setText("save was clicked");
                //yess
            }
        });

    }








}
