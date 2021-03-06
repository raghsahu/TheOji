package com.example.admin.theoji.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.theoji.Add_Chatting;
import com.example.admin.theoji.ModelClass.ChatStudent_Model;
import com.example.admin.theoji.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.admin.theoji.ChatActivity.studentHashMap;

public class Student_Chat_Adapter extends RecyclerView.Adapter<Student_Chat_Adapter.ViewHolder> {


    private static final String TAG = "Student_Chat_Adapter";

    private ArrayList<ChatStudent_Model> StudentList;
    public Context context;
     String ChatUserID;


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView studentName, student_class;
        ImageView chat_stud_image;
        CardView cardeview;
        int pos;

        public ViewHolder(View view) {
            super(view);

            studentName=(TextView)view.findViewById(R.id.student_name_chat);
            chat_stud_image=view.findViewById(R.id.chat_stud_iv);

            cardeview = (CardView)view.findViewById(R.id.cardeview);

        }

    }

    public static Context mContext;
    public Student_Chat_Adapter(Context mContext, ArrayList<ChatStudent_Model> studentList) {
        context = mContext;
        StudentList = studentList;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.chat_student_model, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        final ChatStudent_Model chatStudent_model = StudentList.get(i);

        viewHolder.studentName.setText(chatStudent_model.getFirstname());
        viewHolder.chat_stud_image.setImageResource(R.drawable.img);
        Picasso.get()
                .load("https://jntrcpl.com/theoji/uploads/"+chatStudent_model.getUmeta_value())
                .placeholder(R.drawable.img)
                .into(viewHolder.chat_stud_image);

        viewHolder.cardeview.setTag(viewHolder);
        viewHolder.pos = i;


        viewHolder.cardeview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position=i;
                ChatUserID=studentHashMap.get(position);

                Intent intent = new Intent(context, Add_Chatting.class);
                intent.putExtra("ChatUserID",ChatUserID);
                context.startActivity(intent);
                Toast.makeText(context, "chat "+ChatUserID, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return StudentList.size();
    }
}