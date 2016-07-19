package dvis.prototype.Controller;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;

public class StartActivity extends AppCompatActivity {

    ArrayList<String> ar;
    EditText psd;
    EditText vpsd;
    EditText etr;
    ListView lv;
    ArrayAdapter<String> ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        ar = new ArrayList<>();

        psd = (EditText)findViewById(R.id.psdedit);
        vpsd = (EditText)findViewById(R.id.vpsdedit);
        etr = (EditText)findViewById(R.id.etredit);
        lv = (ListView)findViewById(R.id.listView);

        ad = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, ar);
        lv.setAdapter(ad);
        lv.setDividerHeight(2);
    }

    public void onAddCan(View v) {
        ar.add(String.valueOf(ad.getCount() + 1) + "/" + psd.getText() + "/" + vpsd.getText());
        ad = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, ar);
        lv.setAdapter(ad);
    }

    public void onDeleteCan(View v) {
        ar.remove(ad.getItem(ad.getCount()));
        ad = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, ar);
        lv.setAdapter(ad);
    }

    public void onStartDV(View v) {

        String canNum;
        String psd;
        String vpsd;

        Connection conn = null;

        try {


            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();

            Log.i("Connection", "MSSQL driver load");


            conn = DriverManager.getConnection("jdbc:jtds:sqlserver://127.0.0.1/dvis_db", "root", "lec2554ec");

            Log.i("Connection", "MSSQL open");

            Statement stmt = conn.createStatement();

            /////////////////////////////////////////////////

            //모든 데이터 삭제 및 결과테이블 0으로 초기화
            //유권자 정보 업데이트

            String stmtString = "SET FOREIGN_KEY_CHECKS=0; \n" +
                    "\n" +
                    "truncate table voteresult;\n" +
                    "truncate table votenum;\n" +
                    "truncate table candidates;\n" +
                    "\n" +
                    "SET FOREIGN_KEY_CHECKS=1; \n" +
                    "\n" +
                    "insert into voteresult\n" +
                    "values (1, 0, 0, 0, 0, NULL, " + etr.getText().toString() + ");\n";

            for(String can: ar) {
                canNum = can.split("/")[0];
                psd = can.split("/")[1];
                vpsd = can.split("/")[2];
                // 해당하는 후보 삽입

                stmtString += "INSERT INTO candidates value (" + canNum + ", '" + psd + "', '"  + vpsd + "');\n" +
                                "INSERT INTO votenum value (" + canNum + ", 0, 0, 0);\n";


            }

            ///////////////////////////////////////////////////

            int rs = stmt.executeUpdate(stmtString);

            if(rs == 0) {
                Toast.makeText(this, "생성에 실패했습니다.", Toast.LENGTH_LONG).show();
                conn.close();
            }
            else {
                Toast.makeText(this, "성공적으로 선거 테이블을 생성하였습니다.", Toast.LENGTH_LONG).show();
                conn.close();
                finish();
            }


        } catch (Exception e)

        {

            Log.w("Error connection", "" + e.getMessage());

        }

    }

}
