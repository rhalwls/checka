package kr.co.company.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public static int MY_PERMISSIONS_REQUEST_LOCATION = 101;
    public static int MY_PERMISSIONS_REQUESIT_STORAGE = 102;
    private Person me;

    Toolbar toolbar;

    Fragment1 fragment1;
    Fragment2 fragment2;
    Fragment3 fragment3;
    Activity timeSelector;

    public static void sleep(final long mili) {
        try {
            Thread.sleep(mili);
        } catch (Exception e) {
            // Do nothing
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences pref = getSharedPreferences("isFirst", Activity.MODE_PRIVATE);
        boolean first = pref.getBoolean("isFirst", false);
        if (first == false) {
            Log.d("Is first Time?", "first");
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("isFirst", true);
            editor.commit();
            //앱 최초 실행시 하고 싶은 작업
            me = new Person();
            changeFile();
        }
        getInformation();
        //setSupportActionBar : 액션바 설정
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //액션바 기본 타이틀 보여지지 않게
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        //Fragment : 탭 클릭시 보여줄 화면들
        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        fragment2.setParent(this);
        fragment3 = new Fragment3();

        //기본으로 첫번째 Fragment를 보여지도록 설정
        final int commit;
        switch (commit = getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment1).commit()) {
        }


        //TabLayout에 Tab 3개 추가
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("My information"));
        tabs.addTab(tabs.newTab().setText("Team information"));
        tabs.addTab(tabs.newTab().setText("Join new team"));

        //탭 선택리스너
        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            //탭선택시
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Log.d("MainActivity", "선택된 탭 : " + position);

                Fragment selected = null;
                if (position == 0) {
                    selected = fragment1;
                } else if (position == 1) {
                    selected = fragment2;
                } else if (position == 2) {
                    selected = fragment3;
                }

                int commit;
                commit = getSupportFragmentManager().beginTransaction().replace(R.id.container, selected).commit();
            }

            //탭선택해제시
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            //선탭된탭을 다시 클릭시
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //fragment1.setterForFragment1();
        TimeFlows mytimeflows = new TimeFlows();
        mytimeflows.execute(1);
    }

    public Person getPerson() {
        return me;
    }

    public void setPerson(Person me) {
        this.me = me;
    }

    public void check(Person me) {
        if (me.canTodayCheck()) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_LOCATION);

                Log.d("checkfunction", "체크 permission not grangted");
            } else {
                startLocationService();
            }
        } else {
            Toast.makeText(this, "지금 인증할 수 없습니다.", Toast.LENGTH_LONG).show();
        }

    }

    private void startLocationService() {
        // �꾩튂 愿�由ъ옄 媛앹껜 李몄“
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // �꾩튂 �뺣낫瑜� 諛쏆쓣 由ъ뒪�� �앹꽦
        GPSListener gpsListener = new GPSListener();
        long minTime = 10000;
        float minDistance = 0;

        try {
            Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Double latitude = lastLocation.getLatitude();
            Double longitude = lastLocation.getLongitude();

            //textView.setText("�� �꾩튂 : " + latitude + ", " + longitude);
            // Toast.makeText(getApplicationContext(), "Last Known Location : " + "Latitude : " + latitude + "\nLongitude:" + longitude, Toast.LENGTH_LONG).show();


            Location targetLocation = new Location(new String("target"));
            targetLocation.setLatitude(me.getTargetLocationLatitude());
            targetLocation.setLongitude(me.getTargetLocationLongtitude());

            Log.i("lastLocation", "Last Known Location : " + "Latitude : " + latitude + "\nLongitude:" + longitude);
            Log.i("targetLocation", "Target Location :" + "Latitude : " + targetLocation.getLatitude() + "\nLongitude:" + targetLocation.getLongitude());
            if (targetLocation.distanceTo(lastLocation) < 15 && new Date().getTime() - me.getTargetDate().getTime() < 2 * 60 * 1000) {
                Log.i("인증했습니다", "인증 그리고 거리 오차는 " + targetLocation.distanceTo(lastLocation));
                Toast.makeText(this, "인증 했습니다\n거리 오차는 " + (int)targetLocation.distanceTo(lastLocation) + "M", Toast.LENGTH_LONG).show();
                me.setLastCheck(new Date());
                update();
            } else {
                if (targetLocation.distanceTo((lastLocation)) > 15) {
                    Toast.makeText(this, "범위 안에 있지 않아 인증 못했습니다\n거리 오차는 " +  (int)targetLocation.distanceTo(lastLocation) + "M", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "약속 시간이 맞지 않아 인증 못했습니다\n거리 오차는 " + (int)targetLocation.distanceTo(lastLocation) + "M", Toast.LENGTH_LONG).show();
                }
                Log.i("인증 못했습니다", "인증 그리고 거리 오차는 " + targetLocation.distanceTo(lastLocation));
            }
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 由ъ뒪�� �대옒�� �뺤쓽
     */
    private class GPSListener implements LocationListener {
        /**
         * �꾩튂 �뺣낫媛� �뺤씤�� �� �먮룞 �몄텧�섎뒗 硫붿냼��
         */
        public void onLocationChanged(Location location) {
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();

            String msg = "Latitude : " + latitude + "\nLongitude:" + longitude;
            Log.i("GPSListener", msg);

            //textView.setText("�� �꾩튂 : " + latitude + ", " + longitude);
            //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

    }

    public class TimeFlows extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            Date startTime = new Date();

            /*
            me.setAbsence(me.getAbsence() - 1);
            update();
            if (true)
            throw new RuntimeException();
            */
            
            for (Date current = new Date(); startTime.getTime() - current.getTime() < 1 * 60 * 1000; current = new Date()) {
                try {
                    //publishProgress(0);
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Date targetDate = me.getTargetDate();
                Long diff = (targetDate.getTime() - current.getTime());
                int dif = diff.intValue();
                Integer diffHours = dif / (60 * 60 * 1000);
                Integer diffMinutes = dif / (60 * 1000);
                Integer diffSeconds = dif / 1000;


                publishProgress(diffHours, diffMinutes, diffSeconds);
                if (current.getTime() - me.getTargetDate().getTime() > 1 * 60 * 1000 && me.getLastCheck().getTime() - me.getTargetDate().getTime() < 0) {//과거에 체크
                    //지각은 1분 후
                    //지각하면 업데이트 해야됨
                    Log.i("지각했습니다", "지각했습니다");

                    publishProgress(1);
                }

            }
            return 0;
        }

        @Override
        protected void onProgressUpdate(Integer... params) {

            if (params.length == 3) {
                Log.d("proupdate", "" + params[0] + params[1] + params[2]);
                //Toast.makeText(MainActivity.this, "" + params[0] + params[1] + params[2], Toast.LENGTH_LONG);
                update();
            } else if (params.length == 1) {
                me.setAbsence(me.getAbsence() + 1);
                me.setLastCheck(new Date());
                //Log.d("updategui", "결석했으므로 업데이트 함");
                Toast.makeText(getApplicationContext(), "지각하셨습니다", Toast.LENGTH_LONG).show();
                update();
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
        }
    }

    //업데이트 조건 : 앱 실행했다. 출석 했다, 날이 바뀌었다, 결석했다.
    //날이 바뀐 조건은 제외
    public void changeFile() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUESIT_STORAGE);//CODE바꾸기 ㅠㅠ 아직 안바꿈

            Log.d("updatefunction", "permission not grangted");
        } else {

            try {
                ObjectOutputStream oos = null;
                String ext = Environment.getExternalStorageState();
                String mSdPath;
                if (ext.equals(Environment.MEDIA_MOUNTED)) {
                    mSdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                } else {
                    mSdPath = Environment.MEDIA_UNMOUNTED;
                }
                File file = new File(mSdPath + "/me.dat");
                Log.d("file 경로", mSdPath + "/me.dat");
                FileOutputStream fos = new FileOutputStream(file);
                oos = new ObjectOutputStream(new BufferedOutputStream(fos));
                oos.writeObject(me);
                oos.close();
                Log.d("iostream", "파일 출력 성공");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.d("ioexeption", "파일 출력 실패");
                Toast.makeText(MainActivity.this, "fos IOException", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void update() {
        //파일 ㅣ바꾸기
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUESIT_STORAGE);//CODE바꾸기 ㅠㅠ 아직 안바꿈

            Log.d("updatefunction", "permission not grangted");
        } else {
            try {
                changeFile();
                if (fragment2 != null) {
                    fragment2.updateGUI();
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

    public void getInformation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUESIT_STORAGE);//CODE바꾸기 ㅠㅠ 아직 안바꿈

            Log.d("updatefunction", "permission not grangted");
        } else {

            try {
                ObjectInputStream ois = null;
                String ext = Environment.getExternalStorageState();
                String mSdPath;
                if (ext.equals(Environment.MEDIA_MOUNTED)) {
                    mSdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                } else {
                    mSdPath = Environment.MEDIA_UNMOUNTED;
                }
                File file = new File(mSdPath + "/me.dat");
                Log.d("file 경로", mSdPath + "/me.dat");
                FileInputStream fis = new FileInputStream(file);
                ois = new ObjectInputStream(new BufferedInputStream(fis));
                me = (Person) ois.readObject();
                ois.close();
                Log.d("iostream", "파일 출력 성공");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.d("ioexeption", "파일 출력 실패");
                Toast.makeText(MainActivity.this, "fos IOException", Toast.LENGTH_SHORT).show();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                Log.d("filenotfound", "파일 출력 실패");
                Toast.makeText(MainActivity.this, "fis ioexception-filenot fiound", Toast.LENGTH_SHORT).show();
            }
        }
    }
}