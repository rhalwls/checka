package kr.co.company.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;

public class Fragment3 extends Fragment {
    public void alert(String str) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getContext());
        alertDialogBuilder.setTitle("스터디 참여");
        alertDialogBuilder
                .setMessage(str + "에 참여하겠습니까?")
                .setCancelable(true)
                .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        alertDialogBuilder.create().show();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment3, container, false);

        view.findViewById(R.id.iv2).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alert("우애옹스 정석출석스터디");
            }
        });

        view.findViewById(R.id.iv3).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alert("인천의 하버드 2호관");
            }
        });

        view.findViewById(R.id.iv4).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alert("서울대 도서관");
            }
        });

        view.findViewById(R.id.iv5).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alert("연세대 운동장");
            }
        });

        view.findViewById(R.id.iv6).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alert("서울대학교 알고리즘 스터디");
            }
        });

        view.findViewById(R.id.iv7).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alert("인하대학교 우애옹스");
            }
        });

        return view;
    }
}