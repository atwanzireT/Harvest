package com.example.harvest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.harvest.adapters.IssueAdaptor;
import com.example.harvest.modals.IssueModal;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference database;
    IssueAdaptor issueAdaptor;
    ArrayList<IssueModal> list;
    //        ArrayList<IssueModal> list;
    SearchView searchField;
    IssueAdaptor.OnIssueClickListener onIssueClickListener;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.post_listRV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchField = view.findViewById(R.id.searchInput);
        database = FirebaseDatabase.getInstance().getReference("issues");

        list = new ArrayList<>();
        onIssueClickListener = new IssueAdaptor.OnIssueClickListener() {
            @Override
            public void onIssueClicked(int position) {
                Toast.makeText(getContext(), "clicked on Issue", Toast.LENGTH_SHORT).show();

//                startActivity(new Intent(getContext(), IssueActivity.class));
            }
        };
        issueAdaptor = new IssueAdaptor(getContext(), list, onIssueClickListener);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for( DataSnapshot dataSnapshot : snapshot.getChildren()){
                    IssueModal issueModal = dataSnapshot.getValue(IssueModal.class);
                    list.add(issueModal);
                }
                issueAdaptor.notifyDataSetChanged();
                recyclerView.setAdapter(issueAdaptor);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load issues", Toast.LENGTH_SHORT).show();
            }
        });

        searchField.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                txtSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                txtSearch(query);
                return false;
            }
        });
        return view;
    }

    private void txtSearch(String str){
        list = new ArrayList<>();
        issueAdaptor = new IssueAdaptor(getContext(), list, onIssueClickListener);

        //        Database Reference
        Query query = FirebaseDatabase.getInstance().getReference("issues")
                .orderByChild("title").startAt(str).endAt(str + "~");

//        database.child()
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for( DataSnapshot dataSnapshot : snapshot.getChildren()){
                    IssueModal issueModal = dataSnapshot.getValue(IssueModal.class);
                    list.add(issueModal);
                }
                issueAdaptor.notifyDataSetChanged();
                recyclerView.setAdapter(issueAdaptor);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}