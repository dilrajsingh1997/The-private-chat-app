package com.example.dilrajsingh.chat;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> items;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String id, em, status;
    DBHandler dbHandler;
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<String> arr, arr2, as, chats;
    ProgressDialog pd;

    protected boolean isEmpty(EditText editText){

        return editText.getText().toString().trim().length() == 0;

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private boolean isInFront;

    @Override
    public void onResume() {
        super.onResume();
        isInFront = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isInFront = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pd = new ProgressDialog(MainActivity.this);
        arr = new ArrayList<>();
        isInFront = true;
        arr2 = new ArrayList<>();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        as = new ArrayList<>();
        chats = new ArrayList<>();
        dbHandler = new DBHandler(this, null, null, 1);
        items = new ArrayList<String>();
        listView = (ListView) findViewById(R.id.listView);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(isNetworkAvailable())
                    new rfsh().execute();
                else {
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(MainActivity.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if(isNetworkAvailable()){
            firebaseAuth = FirebaseAuth.getInstance();
            databaseReference = FirebaseDatabase.getInstance().getReference();
            firebaseUser = firebaseAuth.getCurrentUser();
            id = firebaseUser.getUid();
            em = firebaseAuth.getCurrentUser().getEmail();
            String[] temp = em.split("@");
            em = temp[0];
            new rfsh().execute();
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.hasChild(em)){
                        databaseReference.child(em).child("status").setValue("not online");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            databaseReference.child(em).child("status").setValue("online");
            Toast.makeText(MainActivity.this, "Loading chats...", Toast.LENGTH_SHORT).show();
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Log.d("refreshtoken", refreshedToken);
        }
        else{
            Toast.makeText(MainActivity.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
            arr = dbHandler.databaseToName();
            arr2 = dbHandler.databaseToPhone();
            ArrayAdapter<String> add = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, dbHandler.databaseToPhone());
            listView.setAdapter(add);
        }

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder al1 = new AlertDialog.Builder(MainActivity.this);
                String[] opt = {"Edit", "Delete", "About"};
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        status = dataSnapshot.child(arr.get(position)).child("status").getValue().toString();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                ArrayAdapter<String> add = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_expandable_list_item_1, opt);
                al1.setAdapter(add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                AlertDialog.Builder al2 = new AlertDialog.Builder(MainActivity.this);
                                final EditText ed = new EditText(MainActivity.this);
                                LinearLayout ll = new LinearLayout(MainActivity.this);
                                ViewGroup.LayoutParams ld = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                ed.setLayoutParams(ld);
                                ll.addView(ed);
                                al2.setTitle("Enter the name");
                                ed.setHint("Enter the name");
                                al2.setView(ll);
                                al2.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dbHandler.onUpdate(arr.get(position), ed.getText().toString());
                                        databaseReference.child(em).child(arr.get(position)).child("phone").setValue(ed.getText().toString());
                                    }
                                });
                                al2.setCancelable(false);
                                al2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                al2.show();
                                break;
                            case 1:
                                AlertDialog.Builder al = new AlertDialog.Builder(MainActivity.this);
                                al.setTitle("Do you want to delete this chat?");
                                al.setCancelable(false);
                                al.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dbHandler.delEntry(arr.get(position));
                                        databaseReference.child(em).child(arr.get(position)).removeValue();
                                    }
                                });
                                al.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                al.show();
                                break;
                            case 2:
                                final AlertDialog.Builder qw = new AlertDialog.Builder(MainActivity.this);
                                qw.setCancelable(true);
                                qw.setTitle("Information");
                                qw.setMessage("Name : " + arr2.get(position) + "\n" + "No. : " + arr.get(position) + "\n" + "Status : " + status);
                                qw.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                qw.show();
                                break;
                        }
                    }
                });
                al1.show();
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, Main3Activity.class);
                i.putExtra("name", arr.get(position));
                i.putExtra("phone", arr2.get(position));
                startActivity(i);
            }
        });
    }

    public class rfsh extends AsyncTask<Void, Void, Void>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setCancelable(false);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setMessage("Loading");
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    items.clear();
                    arr.clear();
                    dbHandler.delTable();
                    chats.clear();
                    for(DataSnapshot data : dataSnapshot.getChildren()){
                        if(data.getKey().equals(em)) {
                            for (DataSnapshot post : data.getChildren()) {
                                int count = 0;
                                try{
                                    ItemAdded ad = new ItemAdded();
                                    items.add(post.child("name").getValue().toString());
                                    if(count==0){
                                        ad = new ItemAdded(post.child("name").getValue().toString(), post.child("phone").getValue().toString());
                                    }
                                    else{
                                        ad = new ItemAdded(post.child("name").getValue().toString(), post.child("phone").getValue().toString() + " (" + Integer.toString(count) + ")");
                                    }
                                    dbHandler.addItem(ad);
                                }catch (NullPointerException e){

                                }

                            }
                        }
                    }
                    arr = dbHandler.databaseToName();
                    arr2 = dbHandler.databaseToPhone();
                    ArrayAdapter<String> add = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, dbHandler.databaseToPhone());
                    listView.setAdapter(add);
                    pd.dismiss();
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            databaseReference.addValueEventListener(valueEventListener);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            swipeRefreshLayout.setRefreshing(false);
            super.onPostExecute(aVoid);
        }
    }

    @Override
    public void onBackPressed() {
        if(isNetworkAvailable()){
            databaseReference.child(em).child("status").setValue("not online");
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.buttonAdd :
                if(isNetworkAvailable()){
                    addItem();
                }
                else{
                    Toast.makeText(MainActivity.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return true;
    }

    public void addItem(){
        AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
        ad.setTitle("Enter the information");
        LinearLayout ll = new LinearLayout(MainActivity.this);
        ll.setOrientation(LinearLayout.VERTICAL);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        ll.setLayoutParams(params);
        final EditText editText = new EditText(MainActivity.this);
        editText.setHint("Enter the phone no.");
        editText.setLayoutParams(params);
        final EditText editText2 = new EditText(MainActivity.this);
        editText2.setHint("What do you call him/her?");
        editText2.setLayoutParams(params);
        ll.addView(editText);
        ll.addView(editText2);
        ad.setView(ll);
        ad.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String name = editText.getText().toString();
                final String phone = editText2.getText().toString();
                final ItemAdded itemAdded = new ItemAdded(name, phone);
                if (isNetworkAvailable()) {
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.hasChild(name)) {
                                Toast.makeText(MainActivity.this, "The user " + name + " is not registered", Toast.LENGTH_SHORT).show();
                            } else if (dataSnapshot.child(em).hasChild(name)) {
                                Toast.makeText(MainActivity.this, "This user is already in your list", Toast.LENGTH_SHORT).show();
                            } else if (name.equals(em)) {
                                Toast.makeText(MainActivity.this, "You cannot add yourself", Toast.LENGTH_SHORT).show();
                            } else {
                                firebaseAuth = FirebaseAuth.getInstance();
                                databaseReference = FirebaseDatabase.getInstance().getReference();
                                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                databaseReference.child(em).child(name).setValue(itemAdded);
                                dbHandler.addItem(itemAdded);
                                Toast.makeText(MainActivity.this, "Added", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ad.show();
    }
}
