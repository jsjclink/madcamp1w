package com.example.test2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.jar.Attributes;

public class FirstTab extends Fragment  {
    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.sample_1, R.drawable.sample_2,
            R.drawable.sample_3, R.drawable.sample_4,
            R.drawable.sample_5, R.drawable.sample_6,
            R.drawable.sample_7, R.drawable.sample_8,
            R.drawable.sample_9, R.drawable.sample_10,
            R.drawable.sample_11, R.drawable.sample_12,
            R.drawable.sample_13, R.drawable.sample_14,
            R.drawable.sample_15, R.drawable.sample_16,
            R.drawable.sample_17, R.drawable.sample_18,
            R.drawable.sample_19, R.drawable.sample_20
    };
    ArrayList<NameNumberModel> nnModels;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.first_tab, container, false);

        RecyclerView recyclerView = v.findViewById(R.id.mRecyclerView);

        String jsonString = getArguments().getString("jsonString");
        nnModels = jsonParsing(jsonString);

        NN_RecyclerViewAdapter adapter = new NN_RecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        v.findViewById(R.id.mCardView);

        return v;
    }

    public static FirstTab newInstance(String jsonString) {

        FirstTab f = new FirstTab();
        Bundle b = new Bundle();
        b.putString("jsonString", jsonString);

        f.setArguments(b);

        return f;
    }

    private class NN_RecyclerViewAdapter extends RecyclerView.Adapter<NN_RecyclerViewAdapter.MyViewHolder>{

        @NonNull
        @Override
        public NN_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.recycler_view_row, parent, false);
            return new NN_RecyclerViewAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull NN_RecyclerViewAdapter.MyViewHolder holder, int position) {
            holder.tvName.setText(nnModels.get(position).getName());
            holder.tvNumber.setText(nnModels.get(position).getNumber());
            holder.itemView.setTag(position);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), PhoneNumberDetailActivity.class);
                    int position = holder.getBindingAdapterPosition();
                    intent.putExtra("name", nnModels.get(position).getName());
                    intent.putExtra("number", nnModels.get(position).getNumber());
                    intent.putExtra("nnModel", nnModels.get(position));
                    intent.putExtra("position", position);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount()  {
            return nnModels.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{
            TextView tvName, tvNumber;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                tvName = itemView.findViewById(R.id.mPersonName);
                tvNumber = itemView.findViewById(R.id.mPersonNumber);
            }
        }
    }

    private ArrayList<NameNumberModel> jsonParsing(String jsonString){
        ArrayList<NameNumberModel> nnModels = new ArrayList<>();

        try{
            JSONObject jsonObject = new JSONObject(jsonString);

            JSONArray nnArray = jsonObject.getJSONArray("PhoneNames");
            for(int i = 0; i < nnArray.length(); i++){
                JSONObject nnObject = nnArray.getJSONObject(i);

                NameNumberModel nnModel = new NameNumberModel();

                nnModel.setName(nnObject.getString("name"));
                nnModel.setNumber(nnObject.getString("number"));
                nnModel.getPictures().add(Uri.parse("android.resource://com.example.test2/"
                        + mThumbIds[i]).toString());

                nnModels.add(nnModel);
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }

        return nnModels;
    }
}