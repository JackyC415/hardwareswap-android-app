package com.example.jchen415.mywaytormobileapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CPU extends AppCompatActivity {

    //constant price
    final double priceAmount = 149.99;

    TextView numorder;
    Button confirms;
    DBController db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thaitea);

        //get the textbox
        numorder = (TextView) findViewById(R.id.order);
        //get the confirm button
        confirms = (Button) findViewById(R.id.confirm);
        //set it to the pointer
        db = new DBController(this);
        storeOrder();
    }

    //insert into the database
    public void storeOrder()
    {
        confirms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                order newOrder = new order();
                newOrder.setFoodName("CPU");
                newOrder.setFoodPrice(priceAmount);
                newOrder.setAmount(count);

                if (count <= 0) {
                    Toast.makeText(getApplicationContext(), "Please Add Orders!", Toast.LENGTH_LONG).show();
                    return;
                }

                db.insert(newOrder);
                Intent goTO = new Intent(CPU.this, Menu.class);
                startActivity(goTO);
            }
        });
    }

    //store numbers of orders
    private int count;

    //increase amount of orders
    public void countOrder(View v)
    {
        count ++;
        numorder.setText(Integer.toString(count));
    }

    //decrease amount of orders
    public void dCountOrder(View v)
    {
        //to prevent going to negative
        if(count > 0)
        {
            count--;
        }
        numorder.setText(Integer.toString(count));
    }
}
