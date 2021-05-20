package com.example.cchat;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class groupfragement extends Fragment {
private View groupview;
private ListView listView;
private ArrayAdapter<String>arrayAdapter;
private ArrayList<String>list_of_group=new ArrayList<>();
private DatabaseReference groupref;
    public groupfragement() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       groupview= inflater.inflate(R.layout.fragment_groupfragement, container, false);
groupref= FirebaseDatabase.getInstance().getReference().child("Groups");
       Initializefield();
    Retrivedisplay();
  listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int position, long ID) {
          String currentgroupname=adapterView.getItemAtPosition(position).toString();
          Intent groupintent=new Intent(getContext(),GroupchatActivity.class);
          groupintent.putExtra("groupname",currentgroupname);
          startActivity(groupintent);
      }
  });
       return  groupview;
    }

    private void Retrivedisplay() {
        groupref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Set<String>set=new HashSet<>();
                Iterator iterator=snapshot.getChildren().iterator();
                while (iterator.hasNext())
                {
            set.add(((DataSnapshot)iterator.next()).getKey());

                }
                list_of_group.clear();
                list_of_group.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void Initializefield() {
        listView=groupview.findViewById(R.id.listview);
        arrayAdapter=new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,list_of_group);
        listView.setAdapter(arrayAdapter);

    }
}