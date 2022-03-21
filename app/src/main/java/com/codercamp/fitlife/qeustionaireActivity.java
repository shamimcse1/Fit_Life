package com.codercamp.fitlife;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import codercamp.fitlife.R;

public class qeustionaireActivity extends AppCompatActivity{


    private Button next;
    EditText editAge, editWeight, editHeight;
    DatabaseReference reference;
    FirebaseAuth fAuth;
    Spinner sexDropdown, workoutDropdown, goalDropdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qeustionaire);//get the spinner from the xml.


        editAge = findViewById(R.id.etAge);
        editWeight=findViewById(R.id.etWeight);
        editHeight = findViewById(R.id.etHeight);


        //Spinner Information for Sex of user
        //get the spinner from the xml.
        sexDropdown = findViewById(R.id.spinner1);
        //create a list of items for the spinner.
        String[] sex = new String[]{"Male", "Female"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sex);
        //set the spinners adapter to the previously created one.
        sexDropdown.setAdapter(adapter);

        //Spinner Information for Activity Level
        workoutDropdown = findViewById(R.id.spinner2);
        String[] workoutLevel = new String[]{"No Activity", "Light Activity = Workout 2-3 Times a Week", "Moderate activity= Workout 3-4 Times Per Week" ,
                "Heavy Activity = Workout 4-5 Times Per Week"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, workoutLevel);
        workoutDropdown.setAdapter(adapter2);


        //Drop Down Menu For Fitness Goals
        goalDropdown = findViewById(R.id.spinner3);
        String[] goal = new String[]{"Maintaining", "Cutting", "Bulking"};
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, goal);
        goalDropdown.setAdapter(adapter3);

        fAuth = FirebaseAuth.getInstance();
        String user_id = fAuth.getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(user_id).child("User Info");
        next = findViewById(R.id.btnRegister);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String age = editAge.getText().toString().trim();
                String weight = editWeight.getText().toString().trim();
                String height = editHeight.getText().toString().trim();
                String sex = sexDropdown.getSelectedItem().toString().trim();
                String goal = goalDropdown.getSelectedItem().toString().trim();
                String active = workoutDropdown.getSelectedItem().toString().trim();


                if(TextUtils.isEmpty(age)){
                    editAge.setError("Age Required");
                    return;
                }
                if(TextUtils.isEmpty(weight)){
                    editWeight.setError("Weight Required");
                    return;
                }
                if(TextUtils.isEmpty(height)){
                    editHeight.setError("Height Required");
                    return;
                }

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        HashMap<String, String> questions = new HashMap();
                        questions.put("Age", age);
                        questions.put("Height", height);
                        questions.put("Weight", weight);
                        questions.put("Sex", sex);
                        questions.put("Goal", goal);
                        questions.put("Activity", active);
                        reference.setValue(questions);
                        Toast.makeText(qeustionaireActivity.this, "Questionnaire Added", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(qeustionaireActivity.this, "Task Failed!", Toast.LENGTH_SHORT).show();

                    }
                });

                startActivity(new Intent(getApplicationContext(),MainActivity.class));

            }
        });


    }



    public void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}