package com.codercamp.fitlife;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import codercamp.fitlife.R;


public class MealWaterTracking extends AppCompatActivity {
    double BMR;
    double totalCalories;
    double totalWater;
    double height;
    int weight;
    int age;
    String gender;
    String activityLevel;
    String preference;
    String firstName, lastName;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    TextView cals, wat, name;
    Button home;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_water_tracking);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        String uid = user.getUid();
        home = findViewById(R.id.homeBtn);
        cals = findViewById(R.id.calories);
        wat = findViewById(R.id.water);
        name = findViewById(R.id.fullName);
        home = findViewById(R.id.homeBtn);

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                firstName = dataSnapshot.child("First Name").getValue(String.class);
                lastName = dataSnapshot.child("Last Name").getValue(String.class);
                name.setText(firstName + " " + lastName);

                String userHeight = dataSnapshot.child("User Info").child("Height").getValue(String.class);
                height = Double.parseDouble(userHeight);

                String userWeight = dataSnapshot.child("User Info").child("Weight").getValue(String.class);
                weight = Integer.parseInt(userWeight);

                String userAge = dataSnapshot.child("User Info").child("Age").getValue(String.class);
                age = Integer.parseInt(userAge);

                String sex = dataSnapshot.child("User Info").child("Sex").getValue().toString();
                gender = sex;

                String activity = dataSnapshot.child("User Info").child("Activity").getValue().toString();
                activityLevel = activity;

                String goal = dataSnapshot.child("User Info").child("Goal").getValue().toString();
                preference = goal;

                getBMR(height, gender,  weight, age, activityLevel, preference, cals);
                setWaterIntakeLevel(weight, wat);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });


    }


    public void getBMR(double height, String gender, int weight, int age , String activityLevel, String preference, TextView cals)
    {

        if(gender.equals("Male"))
        {
            BMR = 66 + (6.3 * weight) + (12.9 * height) - (6.8 * age);
        }
        else if (gender.equals("Female"))
        {
            BMR = 655 + (4.3 * weight) + (4.7 * height) - (4.7 * age);
        }
        setTotalCalories(activityLevel, preference, BMR, cals);

    }

    public void setTotalCalories(String activityLevel, String preference, double BMR, TextView cals)
    {

        totalCalories = BMR;
        if(activityLevel.equals("No Activity"))
        {
            totalCalories = (BMR * 1.2);
        }
        else if(activityLevel.equals("Light Activity = Workout 2-3 Times a Week"))
        {
            totalCalories =(BMR * 1.375);
        }
        else if(activityLevel.equals("Moderate activity= Workout 3-4 Times Per Week"))
        {
            totalCalories = (BMR * 1.55);
        }
        else if(activityLevel.equals("Heavy Activity = Workout 4-5 Times Per Week"))
        {
            totalCalories = (BMR * 1.725);
        }


        if (preference.equals("Cutting"))
        {
            totalCalories = totalCalories - 250;
        }
        else if (preference.equals("Maintaining"))
        {
            totalCalories = totalCalories;
        }
        else if (preference.equals("Bulking"))
        {
            totalCalories = totalCalories + 250;
        }


        cals.setText(String.valueOf(totalCalories));
    }
    //test3
    public void setWaterIntakeLevel(int weight, TextView wat)
    {

        totalWater = weight/2;

        wat.setText(String.valueOf(totalWater));
    }

}