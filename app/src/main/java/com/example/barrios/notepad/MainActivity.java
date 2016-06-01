package com.example.barrios.notepad;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ListView LVnotes;
    ArrayList<String> ListOfNotes;

    public static final String MyPREFERENCES = "myPref";
    public static final String JsonKey = "jsonData";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LVnotes = (ListView)findViewById(R.id.ListOfNotes);
        ListOfNotes = new ArrayList<String>();


        try {
            getNotes();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LVnotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displayNote(position);
            }
        });

    }

    public void getNotes() throws JSONException {
        ListOfNotes.clear();
        SharedPreferences sp = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String strJson = sp.getString(JsonKey,null);
        if(strJson == null){
            JSONArray jsonArray = new JSONArray();
            JSONObject notesObj = new JSONObject();
            try {
                notesObj.put("Notes",jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(JsonKey, notesObj.toString());
            editor.commit();

        }else{
            JSONObject jsonData = new JSONObject(strJson);
            JSONArray jArray = jsonData.getJSONArray("Notes");
            for(int i = 0; i<jArray.length(); i++){
                JSONObject json_data = jArray.getJSONObject(i);
                ListOfNotes.add(json_data.getString("Note_Name"));
                populateListView();
            }
        }

    }

    public void populateListView(){
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,ListOfNotes);

        LVnotes.setAdapter(arrayAdapter);
    }

    public void displayNote(int pos){
        Bundle bundle = new Bundle();
        bundle.putString("noteName", ListOfNotes.get(pos).toString());
        Intent intent = new Intent(this,NoteActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void newNote(){
        Bundle bundle = new Bundle();
        bundle.putString("noteName","");
        Intent intent = new Intent(this,NoteActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    @Override
    protected void onRestart() {// TODO Auto-generated method stub
        super.onRestart();

        //Do your code here
        try {
            getNotes();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.action_add:
                newNote();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
