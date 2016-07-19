package dvis.prototype.Controller;

import android.content.Intent;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.sql.Connection;

import java.sql.DriverManager;

import java.sql.ResultSet;

import java.sql.Statement;

import java.sql.Connection;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // query2();
    }

    public void onBeginClicked(View v) {
        Intent it = new Intent(this, StartActivity.class);
        startActivity(it);
    }

    public void onConClicked(View v) {

    }

    public void query2()

    {

        Connection conn = null;

        try {


            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();

            Log.i("Connection", "MSSQL driver load");


            conn = DriverManager.getConnection("jdbc:jtds:sqlserver://127.0.0.1/dvis_db", "root", "lec2554ec");

            Log.i("Connection", "MSSQL open");

            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery("select * from state");


            //Print the data to the console

            while (rs.next()) {

                Log.w("Data:", rs.getString(3));

//                  Log.w("Data",reset.getString(2));

            }

            conn.close();


        } catch (Exception e)

        {

            Log.w("Error connection", "" + e.getMessage());

        }

    }


}
