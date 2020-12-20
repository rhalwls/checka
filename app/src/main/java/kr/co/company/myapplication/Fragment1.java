package kr.co.company.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import kr.co.company.myapplication.R;

public class Fragment1 extends Fragment {
    Button btnTimerController;
    //Date time;
    Timer myTimer;
    TextView timeView;
    Activity activity = null;
    Date initialDate = null;
    int tmp = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment1, container, false);

        activity = getActivity();

        final TextView tv = view.findViewById(R.id.textview1);
        final TextView timeView = view.findViewById(R.id.timeText);
        final String texts[] = {
                "최선을 다하지 않으면서\n최고를 바라지 마라.",
                "내일 죽는 사람처럼 살고\n영원히 사는 사람처럼 배워라.",
                "뇌는 믿고 기대하는\n방향으로 작동한다.",
                "불가능이란 노력하지\n않는 자의 변명이다.",
                "지금도 다른 사람의\n책장은 넘어가고 있다."
        };
        Random r = new Random();
        int i = r.nextInt(5);
        tv.setText(texts[i]);

        view.findViewById(R.id.btnTimerControl).getBackground().setColorFilter(Color.parseColor("#81d4fa"), PorterDuff.Mode.MULTIPLY);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (activity != null) {
                    MainActivity.sleep(1000);

                    if (activity != null)
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (initialDate != null) {
                                    long diff = (new Date().getTime() - initialDate.getTime()) / 1000L;
                                    long h = diff / 60 / 60, m = diff / 60, s = diff % 60;
                                    timeView.setText(String.format(Locale.US, "%02d:%02d:%02d", h, m, s));
                                }

                                if (new Date().getSeconds() % 5 == 0) {
                                    tv.setText(texts[tmp]);

                                    tmp++;
                                    tmp %= texts.length;
                                }
                            }
                        });
                }
            }
        }).start();

        btnTimerController = (Button) view.findViewById(R.id.btnTimerControl);
        btnTimerController.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnTimerController.getText().equals("공부중단하기")) {
                    btnTimerController.setText("공부시작하기");
                    initialDate = null;
                } else {
                    btnTimerController.setText("공부중단하기");
                    initialDate = new Date();
                }


            }
        });

        return view;
    }


    public class Timer extends AsyncTask<Date, String, Integer> {
        Date accumulatedTime;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected In teger doInBackground(Date... dates) {
            Log.i("timer doinback", "실행시작");
            accumulatedTime = dates[0];
            Date threadStartTime = new Date();
            for (Date current = new Date(); current.getTime() - threadStartTime.getTime() < 1 * 60 * 1000; ) {
                if (btnTimerController.getText().equals("공부중단하기")) {
                    try {
                        Thread.sleep(1000);
                        accumulatedTime.setTime(accumulatedTime.getTime() + 1000);
                        SimpleDateFormat myTimeFormat = new SimpleDateFormat("mm:ss");
                        String s = myTimeFormat.format(accumulatedTime);

                        publishProgress(s);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Log.i("타이머", "sleep 못함");
                    }
                } else {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Log.i("타이머", "sleep 못함");
                    }
                }
            }
            int result = 1;
            return result;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Log.i("타이머스레드", values[0]);
            Fragment1.this.change_time(values[0]);
        }

        /*

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);

            Log.d("Result", "result : " + result);
        }
        */
    }

    void change_time(String s) {
        timeView.setText(s);
    }
}
