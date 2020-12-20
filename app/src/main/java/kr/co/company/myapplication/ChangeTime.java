package kr.co.company.myapplication;

import android.content.Intent;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Date;

public class ChangeTime extends AppCompatActivity {
    EditText editTextForDate;
    EditText editTextForTime;
    Button btnSetNReturn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_time);

        editTextForDate=(EditText)findViewById(R.id.editTextForDay);
        editTextForTime=(EditText)findViewById(R.id.editTextForTime);
        btnSetNReturn=(Button)findViewById(R.id.btnSetNReturn);
        btnSetNReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String d =editTextForDate.getText()+" "+editTextForTime.getText();
                try {
                    Date newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(d);
                    Intent resultIntent =new Intent();
                    resultIntent.putExtra("newTime",newDate);
                    setResult(RESULT_OK,resultIntent);
                    finish();
                }catch (ParseException e){
                    e.printStackTrace();
                    Toast.makeText(ChangeTime.this, "시간변경 실패",Toast.LENGTH_LONG);
                }
            }
        });
    }
}


