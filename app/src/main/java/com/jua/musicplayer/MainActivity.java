/*
The Music Player

Author: Jeewantha Udeshika Ariyawansha
@2022

 */

package com.jua.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    // Declaring variables
    ListView listView;
    String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.SongList);

        // Calling runtime permission method
        runTimePermission();
    }

    // Method to get permission from user
    // Dexter library is used to get permission
    public void runTimePermission(){
        // Getting permission dialog
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        // When Permission granted, display the songs
                        displaySongs();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        // If denied, keep asking the permission
                        runTimePermission();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    // Method to find song from the storage
    public ArrayList<File> findSong(File file){
        Log.d("JUA123", "JUA");

        ArrayList<File> songList = new ArrayList<File>();    // Array list to return with mp3 and wmv files
        File[] files = file.listFiles();                    // List all the files from given 'File' directory

        // Checking the files one by one
        for (File singleFile: files){
            // Checking the file is directory or the hidden file
            if (singleFile.isDirectory() && !singleFile.isHidden()){
                songList.addAll(findSong(singleFile));      // Recursive to this
            }

            // Else check the file is mp3 or wmv and add into returning list
            else if(singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wmv")){
                songList.add(singleFile);
            }
        }

        return songList;
    }

    // Method to display the song list
    public void displaySongs(){
        final ArrayList<File> mySongs = findSong(Environment.getExternalStorageDirectory()); // Finding the songs in the directory

        items = new String[mySongs.size()];

        // Displaying the song names in the listview
        for (int i=0; i< mySongs.size(); i++){
            items[i] = mySongs.get(i).getName().replace(".mp3", "").replace(".wmv", "");
        }

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        // Setting the adapter in listView
        listView.setAdapter(myAdapter);
    }
}