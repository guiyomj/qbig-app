package com.sundosoft.qbig;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Fragment_4 extends Fragment {
    View view;
    ImageView photo, pen;
    TextView signout, changeps, logout, notice, inquiry, myinquiry;
    TextView name,email;
    Switch push;

    String user_email, user_name;
    private DatabaseReference dbRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_4, container, false);
        photo=(ImageView)view.findViewById(R.id.photo);
        photo.setBackground(new ShapeDrawable(new OvalShape()));
        photo.setClipToOutline(true);
        pen=(ImageView)view.findViewById(R.id.pen);

        user_name=((MainActivity)getActivity()).user_name;
        user_email=((MainActivity)getActivity()).user_email;

        logout = (TextView)view.findViewById(R.id.logout);
        signout = (TextView) view.findViewById(R.id.signout);
        name=(TextView)view.findViewById(R.id.name);
        email=(TextView)view.findViewById(R.id.email);
        notice=(TextView)view.findViewById(R.id.notice);
        inquiry=(TextView)view.findViewById(R.id.inquiry);
        myinquiry=(TextView)view.findViewById(R.id.myinquiry);
        changeps = (TextView)view.findViewById(R.id.change_ps);
        push = (Switch)view.findViewById(R.id.push);
        push.setChecked(((MainActivity)getActivity()).isPush2);

        push.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    ((MainActivity)getActivity()).saveData("val2",true);
                    Toast.makeText(view.getContext(), "푸시알림이 설정되었습니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    ((MainActivity)getActivity()).saveData("val2",false);
                    Toast.makeText(view.getContext(), "푸시알림이 해제되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dbRef = FirebaseDatabase.getInstance().getReference("user");

        name.setText(user_name+" 님");
        email.setText(user_email);

        if (((MainActivity)getActivity()).user_email.contains("gmail")) {
            changeps.setVisibility(View.GONE);
        }

        changeps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PsDialog();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {View dlgView = View.inflate(view.getContext(),R.layout.dialog_signout,null);
                final Dialog dlg = new Dialog(view.getContext());
                dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dlg.setContentView(dlgView);
                TextView ok,cancel,cs;
                ok = (TextView)dlgView.findViewById(R.id.ok_bt);
                cancel = (TextView)dlgView.findViewById(R.id.cancel_bt);
                cs = (TextView)dlgView.findViewById(R.id.cs);
                cs.setText("로그아웃을 하시겠습니까?");

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(view.getContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                        dlg.dismiss();

                        ((MainActivity)getActivity()).googlesignOut();
                        ((MainActivity)getActivity()).clearApplicationData();
                        Intent intent = new Intent(view.getContext(), LoginActivity.class);
                        ((MainActivity)getActivity()).finishMain();
                        startActivity(intent);
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dlg.dismiss();
                    }
                });

                dlg.show();
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dlgView = View.inflate(view.getContext(),R.layout.dialog_signout,null);
                final Dialog dlg = new Dialog(view.getContext());
                dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dlg.setContentView(dlgView);
                TextView ok,cancel;
                ok = (TextView)dlgView.findViewById(R.id.ok_bt);
                cancel = (TextView)dlgView.findViewById(R.id.cancel_bt);

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(view.getContext(), "회원탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        dlg.dismiss();

                        ((MainActivity)getActivity()).googlerevokeAccess();
                        ((MainActivity)getActivity()).clearApplicationData();
                        dbRef.child(((MainActivity)getActivity()).uid).removeValue();
                        SignOut task2 = new SignOut();
                        task2.execute(((MainActivity)getActivity()).user_email);

                        //Intent intent2 = new Intent(view.getContext(), LoginActivity.class);
                        ((MainActivity)getActivity()).finishMain();
                        //startActivity(intent2);
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dlg.dismiss();
                    }
                });

                dlg.show();

            }
        });

        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent noticeIntent = new Intent(view.getContext(), NoticeActivity.class);
                startActivity(noticeIntent);
            }
        });

        inquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent InquiryIntent = new Intent(view.getContext(), InquiryActivity.class);
                InquiryIntent.putExtra("email",user_email);
                startActivity(InquiryIntent);
            }
        });

        myinquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), MyInquiryActivity.class);
                intent.putExtra("email",user_email);
                intent.putExtra("title","내 문의내역");
                startActivity(intent);
            }
        });

        pen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NameDialog();
            }
        });

        return view;
    }

    public void NameDialog() {
        View dlgView = View.inflate(view.getContext(),R.layout.dialog_edit,null);
        final Dialog dlg = new Dialog(view.getContext());
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dlg.setContentView(dlgView);
        TextView ok;
        ImageView exit;
        final EditText edit;

        edit=(EditText)dlgView.findViewById(R.id.edit);
        exit=(ImageView)dlgView.findViewById(R.id.exit);
        ok = (TextView)dlgView.findViewById(R.id.ok_bt);
        edit.setText(((MainActivity)getActivity()).user_name);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_name=edit.getText().toString();
                NameChange task = new NameChange();
                task.execute(((MainActivity)getActivity()).user_email,new_name);
                //dbRef.child(((MainActivity)getActivity()).uid).child("name").setValue(new_name);
                ((MainActivity)getActivity()).user_name=new_name;
                name.setText(new_name+" 님");
                Toast.makeText(view.getContext(), "성공적으로 변경되었습니다.", Toast.LENGTH_SHORT).show();
                dlg.dismiss();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });

        dlg.show();
    }


    public void PsDialog() {
        View dlgView = View.inflate(view.getContext(),R.layout.dialog_edit,null);
        final Dialog dlg = new Dialog(view.getContext());
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dlg.setContentView(dlgView);
        TextView ok, text;
        ImageView exit;
        final EditText edit;

        edit=(EditText)dlgView.findViewById(R.id.edit);
        exit=(ImageView)dlgView.findViewById(R.id.exit);
        ok = (TextView)dlgView.findViewById(R.id.ok_bt);
        text = (TextView)dlgView.findViewById(R.id.text);
        text.setText("비밀번호 변경");
        edit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        edit.setSingleLine();
        edit.setTransformationMethod( PasswordTransformationMethod.getInstance() );

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prm=edit.getText().toString();
                ((MainActivity)getActivity()).emailchangeps(prm);
                Toast.makeText(view.getContext(), "비밀번호가 성공적으로 변경되었습니다.", Toast.LENGTH_SHORT).show();
                dlg.dismiss();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });

        dlg.show();
    }
}
