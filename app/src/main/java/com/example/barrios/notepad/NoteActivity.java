package com.example.barrios.notepad;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NoteActivity extends AppCompatActivity {

    private Button saveButton;
    private EditText noteName;
    private EditText noteText;
    private String note;
    private String prevNoteName;
    private SharedPreferences sharedPreferences;
    private String strJson;

    //TextWatcher
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3)
        {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            checkFieldsForEmptyValues();
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        saveButton = (Button) findViewById(R.id.saveButton);
        noteName = (EditText) findViewById(R.id.noteName);
        noteName.addTextChangedListener(textWatcher);
        noteText = (EditText) findViewById(R.id.noteText);
        sharedPreferences = loadPreferences();
        checkFieldsForEmptyValues();

        Bundle bundle = getIntent().getExtras();
        prevNoteName = bundle.getString("noteName");


        if(!prevNoteName.equals("")){
            noteName.setText(prevNoteName);
            try {
                loadNote(prevNoteName);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    savePreferences();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });





    }
    private void loadNote(String noteName) throws JSONException {
        JSONObject noteobj = new JSONObject(strJson);
        JSONArray notes = noteobj.getJSONArray("Notes");

        for(int i = 0; i< notes.length(); i++){
            JSONObject note = (JSONObject)notes.get(i);
            if(note.getString("Note_Name").equals(prevNoteName)){
                noteText.setText(note.getString("Note_Text"));
            }
        }



    }
    private SharedPreferences loadPreferences(){

        SharedPreferences sp = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        strJson = sp.getString(MainActivity.JsonKey,null);
        return sp;
    }
    private void savePreferences() throws JSONException {
        String nName = noteName.getText().toString();

        if(prevNoteName.equals("")){
           addNewNote();
        }else if(nName.equals(prevNoteName)){
            //dont update name just note
            updateNote();
        }else {
            //update name and note
            updateNandT();
        }


    }

    private void updateNandT() throws JSONException {
        String nText = noteText.getText().toString();
        String nName = noteName.getText().toString();

        JSONObject noteobj = new JSONObject(strJson);
        JSONArray notes = noteobj.getJSONArray("Notes");
        for(int i = 0; i< notes.length(); i++){
            JSONObject note = (JSONObject)notes.get(i);
            if(note.getString("Note_Name").equals(prevNoteName)){
                ((JSONObject)notes.get(i)).put("Note_Text", nText);
                ((JSONObject)notes.get(i)).put("Note_Name", nName);
                prevNoteName = nName;
            }
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MainActivity.JsonKey, noteobj.toString());
        editor.commit();
        Toast.makeText(NoteActivity.this,"Note Saved",Toast.LENGTH_LONG).show();

    }
    private void addNewNote() throws JSONException {
        String nText = noteText.getText().toString();
        String nName = noteName.getText().toString();

        JSONObject noteobj = new JSONObject(strJson);
        JSONArray notes = noteobj.getJSONArray("Notes");
        JSONObject note = new JSONObject();
        note.put("Note_Name", nName);
        note.put("Note_Text", nText);
        notes.put(note);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MainActivity.JsonKey, noteobj.toString());
        editor.commit();
        Toast.makeText(NoteActivity.this,"Note Saved",Toast.LENGTH_LONG).show();
    }

    private void updateNote() throws JSONException {
        String nText = noteText.getText().toString();

        JSONObject noteobj = new JSONObject(strJson);
        JSONArray notes = noteobj.getJSONArray("Notes");
        for(int i = 0; i< notes.length(); i++){
            JSONObject note = (JSONObject)notes.get(i);
            if(note.getString("Note_Name").equals(prevNoteName)){
                ((JSONObject)notes.get(i)).put("Note_Text", nText);
            }
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MainActivity.JsonKey, noteobj.toString());
        editor.commit();
        Toast.makeText(NoteActivity.this,"Note Saved",Toast.LENGTH_LONG).show();
    }

    private  void checkFieldsForEmptyValues(){

        String s1 = noteName.getText().toString().trim();

        if(s1.trim().isEmpty())
        {
            saveButton.setEnabled(false);
        }else
        {
            saveButton.setEnabled(true);
        }

    }



    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        finish();
    }



}
