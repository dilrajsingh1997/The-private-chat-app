package com.example.dilrajsingh.chat;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Main3Activity extends AppCompatActivity {

    Button button;
    TextView textView;
    DBHandler2 dbHandler2;
    EditText editText;
    String whatever;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String id, em, name, messag, phone;
    MyService myservice;
    Boolean isbound = false;
    HashMap<String, ArrayList<String>> map;
    ArrayList<String> tempstring;
    HashMap<String, String> tempmap;
    ArrayList<String> arr;
    ListView list;
    String message;
    NotificationCompat.Builder not;
    final int notid = 54482;
    ArrayList<Integer> pos;
    ArrayList<String> time, read_token;
    DBHandler dbHandler;

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        dbHandler2 = new DBHandler2(this, null, null, 1);
        dbHandler = new DBHandler(this, null, null, 1);
        pos = new ArrayList<>();
        time = new ArrayList<>();
        read_token = new ArrayList<>();
        not = new NotificationCompat.Builder(Main3Activity.this);
        not.setAutoCancel(true);
        list = (ListView) findViewById(R.id.listView2);
        list.setDividerHeight(0);
        arr = new ArrayList<>();
        map = new HashMap<>();
        tempstring = new ArrayList<>();
        tempmap = new HashMap<>();
        button = (Button) findViewById(R.id.button2);
        editText = (EditText) findViewById(R.id.editText3);
        //textView = (TextView) findViewById(R.id.textView);
        final Bundle temp = getIntent().getExtras();
        name = temp.getString("name");
        phone = temp.getString("phone");
        // Toast.makeText(Main3Activity.this, name, Toast.LENGTH_SHORT).show();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = firebaseAuth.getCurrentUser();
        try{
            id = firebaseUser.getUid();
            em = firebaseAuth.getCurrentUser().getEmail();
            String[] xx = em.split("@");
            em = xx[0];
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        Intent i = new Intent(this, MyService.class);
        bindService(i, myconnection, Context.BIND_AUTO_CREATE);
        if(isNetworkAvailable()){
            //updateDB();
            refresh();
        }
        else{
            new noConnect().execute();
        }
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if (!pos.contains(position)) {
                    AlertDialog.Builder al = new AlertDialog.Builder(Main3Activity.this);
                    ArrayList<String> temp = new ArrayList<String>();
                    if(arr.get(position).split(" : ")[0].equals("You")){
                        temp.add("Delete");
                        temp.add("Info");
                    }
                    else{
                        temp.add("Delete");
                    }
                    al.setAdapter(new ArrayAdapter<String>(Main3Activity.this, android.R.layout.simple_expandable_list_item_1, temp), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    AlertDialog.Builder al = new AlertDialog.Builder(Main3Activity.this);
                                    al.setTitle("Do you want to delete this message?");
                                    al.setCancelable(false);
                                    al.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (isNetworkAvailable()) {
                                                int i;
                                                for (i = 0; i < pos.size(); i++) {
                                                    if (pos.get(i) > position) {
                                                        break;
                                                    }
                                                }
                                                String[] abc = arr.get(pos.get(i - 1)).split("< ");
                                                String ab = abc[1];
                                                String[] abcd = ab.split(" >");
                                                String f = abcd[0];
                                                databaseReference.child(em).child(name).child(f).child(time.get(position)).removeValue();
                                                Toast.makeText(Main3Activity.this, "Message deleted", Toast.LENGTH_SHORT).show();
                                                refresh();
                                            } else {
                                                Toast.makeText(Main3Activity.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    al.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                                    al.show();
                                    break;
                                case 1:
                                    AlertDialog.Builder ad = new AlertDialog.Builder(Main3Activity.this);
                                    if (read_token.get(position).equals("Read")) {
                                        ad.setMessage("This message has been read");
                                    } else {
                                        ad.setMessage("This message has not been read");
                                    }
                                    ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                                    ad.show();
                                    break;
                            }
                        }
                    });
                    al.show();
                }
                return true;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public class noConnect extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(Main3Activity.this, "Pre-execute", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            arr.clear();
            map.clear();
            pos.clear();
            time.clear();
            ArrayList<String> date = dbHandler2.dataToDate(name);
            for(String tmp : date){
                arr.add(tmp);
                pos.add(arr.size()-1);
                time.add("0");
                int c = dbHandler2.getDateCount(name, tmp);
                while(c!=0){
                    ArrayList<String> ar = dbHandler2.dataToChatf(name, tmp);
                    for(String x : ar){
                        arr.add(x);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(Main3Activity.this, "Post-execute", Toast.LENGTH_SHORT).show();
            list.setAdapter(new ArrayAdapter<String>(Main3Activity.this, android.R.layout.simple_list_item_1, arr));
        }
    }

    @Override
    public void onBackPressed() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getKey().equals(name)) {
                        for (DataSnapshot dd : ds.getChildren()) {
                            if (dd.getKey().equals(em)) {
                                for (DataSnapshot keys : dd.getChildren()) {
                                    for (DataSnapshot kk : keys.getChildren()) {
                                        if (kk.hasChild("read_token")) {
                                            updt(keys.getKey(), kk.getKey());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        super.onBackPressed();
    }

    public void refresh(){
        ValueEventListener vl = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arr.clear();
                map.clear();
                pos.clear();
                time.clear();
                read_token.clear();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    if(data.getKey().equals(em)) {
                        for (DataSnapshot post : data.getChildren()) {
                            if(post.getKey().equals(name)){
                                for(DataSnapshot keys : post.getChildren()){
                                    if(keys.getKey().contains(",")){
                                        String date = keys.getKey();
                                        messag += date + "\n\n";
                                        arr.add("< " + date + " >");
                                        read_token.add("Not applicable");
                                        pos.add(arr.size() - 1);
                                        time.add("0");
                                        for(DataSnapshot value : keys.getChildren()){
                                            String token = value.child("token").getValue().toString();
                                            read_token.add(value.child("read_token").getValue().toString());
                                            String token2 = token;
                                            if(!token.equals("You")){
                                                token = dbHandler.getPhone(token);
                                                if(token.equals("")){
                                                    token = token2;
                                                }
                                            }
                                            String mess = value.child("message").getValue().toString();
                                            messag += token + "\n";
                                            messag += mess + "\n\n";
                                            arr.add(token + " : " + mess);
                                            time.add(value.getKey());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                ArrayAdapter<String> add = new ArrayAdapter<String>(Main3Activity.this, android.R.layout.simple_list_item_1, arr);
                list.setAdapter(add);
                scrollMyListViewToBottom();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Main3Activity.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        databaseReference.addValueEventListener(vl);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    {
                        for (DataSnapshot dd : ds.getChildren()) {
                            {
                                for (DataSnapshot keys : dd.getChildren()) {
                                    for (DataSnapshot kk : keys.getChildren()) {
                                        if(ds.getKey().equals(name) && dd.getKey().equals(em) && kk.hasChild("read_token")){
                                            updt(keys.getKey(), kk.getKey());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void updt(String date, String time){
        databaseReference.child(name).child(em).child(date).child(time).child("read_token").setValue("Read");
    }

    public void onMessageSent(View view){
        if(!isNetworkAvailable()){
            Toast.makeText(Main3Activity.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
        }
        else{
            message  = editText.getText().toString();
            final MessageAdded messfrom = new MessageAdded(message, "Not read", em);
            final MessageAdded messto = new MessageAdded(message, "Not read", "You");
            firebaseAuth = FirebaseAuth.getInstance();
            databaseReference = FirebaseDatabase.getInstance().getReference();
            final String gettimex = gettime();
            final String[] tmp = gettimex.split(" - ");
            final String date = tmp[1];
            final String time = tmp[0];
            final String[] spl = time.split(":");
            final String hrs = spl[0];
            final String mns = spl[1];
            final String sss = spl[2];
            final long tm = (long) (Float.parseFloat(hrs)*3600 + Float.parseFloat(mns)*60 + Float.parseFloat(sss));
            // Toast.makeText(Main3Activity.this, Long.toString(tm), Toast.LENGTH_SHORT).show();
            databaseReference.child(em).child(name).child(date).child(Long.toString(tm)).setValue(messto);
            databaseReference.child(name).child(em).child(date).child(Long.toString(tm)).setValue(messfrom);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot post : dataSnapshot.getChildren()) {
                        if (post.getKey().equals(name)) {
                            for (DataSnapshot sec : post.getChildren()) {
                                if (sec.getKey().equals(em)) {
                                    if (!sec.hasChild("name")) {
                                        whatever = databaseReference.child(em).getKey();
                                        ItemAdded itemAdded = new ItemAdded(em, whatever);
                                        databaseReference.child(name).child(em).setValue(itemAdded);
                                        databaseReference.child(name).child(em).child(date).child(Long.toString(tm)).setValue(messfrom);
                                    }
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            editText.setText("");
            updateDB();
            refresh();
        }
    }

    private ServiceConnection myconnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyService.MyLocalBinder binder = (MyService.MyLocalBinder) service;
            myservice = binder.getService();
            isbound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isbound = false;
        }
    };

    public void updateDB(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dbHandler2.delTable();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    if(data.getKey().equals(em)){
                        for(DataSnapshot post : data.getChildren()){
                            final String sendto = post.getKey();
                            for(DataSnapshot dt : post.getChildren()){
                                final String date = dt.getKey();
                                for(DataSnapshot ms : dt.getChildren()){
                                    String token = ms.child("token").getValue().toString();
                                    String mess = ms.child("message").getValue().toString();
                                    dbHandler2.addItem(date, sendto, token + " : " + mess);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public String gettime(){
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss - yyyy, MMMM dd", Locale.ENGLISH);
        return (df.format(new Date()));
    }

    private void scrollMyListViewToBottom(){
        list.setSelection(list.getCount() - 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mn = getMenuInflater();
        mn.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }
}
