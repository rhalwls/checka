package kr.co.company.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Fragment2 extends Fragment {
    MainActivity parent;
    private Button checkButton;
    private String instruction;
    private TextView introdution;
    private Button btnChangeTime;
    private Button btnSelectLocation;
    public static final int CALL_CHANGE_TIME = 3000;
    public static final int CALL_LOCATION_SELECT = 3001;

    public void setParent(MainActivity parent) {
        this.parent = parent;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2, container, false);
        checkButton = (Button) view.findViewById(R.id.checkButton);
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parent.check(parent.getPerson());
            }
        });

        updateGUI();

        btnChangeTime = (Button) view.findViewById(R.id.btnChangeTime);
        btnChangeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(parent, ChangeTime.class);
                intent.putExtra("me", parent.getPerson());
                startActivityForResult(intent, CALL_CHANGE_TIME);
            }
        });
        btnSelectLocation = (Button) view.findViewById(R.id.btnChangeLocation);
        btnSelectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(parent, LocationSelect.class);
                intent.putExtra("me", parent.getPerson());
                startActivityForResult(intent, CALL_LOCATION_SELECT);
            }
        });

        return view;
    }

    public void updateGUI() {
        /*
        instruction = "내정보\n" + parent.getPerson().getName() + "\n지각 횟수: " + parent.getPerson().getAbsence() + "\n약속시간: " + parent.getPerson().getTargetDate().toString() +
                "\n약속 장소: \n" + "latitude: " + parent.getPerson().getTargetLocationLatitude() + "longtitude: " + parent.getPerson().getTargetLocationLongtitude()
                + "\n오늘의 출석기회가 있나요? :"
                + parent.getPerson().canTodayCheck();
         */
        instruction =
                parent.getPerson().getName() + "\n" +
                        "지각 횟수: " + parent.getPerson().getAbsence() + "\n" +
                        "약속 시간: " + getDate(parent.getPerson().getTargetDate()) + "\n" +
                        // "약속 장소: \n" + "latitude: " + parent.getPerson().getTargetLocationLatitude() + "longtitude: " + parent.getPerson().getTargetLocationLongtitude() + "\n" +
                        (parent.getPerson().canTodayCheck() ? "출석해주세요!" : "출석 시간이 아닙니다");
        if (getView() != null) {
            introdution = (TextView) getView().findViewById(R.id.myintrodution);
            introdution.setText(instruction);
        }
    }

    public static String getDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return String.format(Locale.US, "%04d-%02d-%02d %d:%02d",
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CALL_CHANGE_TIME:
                    Date newDate = (Date) data.getExtras().get("newTime");
                    parent.getPerson().setTargetDate(newDate);
                    parent.update();
                    break;
                case CALL_LOCATION_SELECT:
                    //채울것!!!!
                    Person newPerson = (Person) data.getExtras().get("me");
                    parent.setPerson(newPerson);
                    parent.update();

                    break;
            }
        }
    }
}