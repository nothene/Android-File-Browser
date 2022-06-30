package com.example.filebrowser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.*;
import android.view.View.*;
import java.io.File;
import java.io.IOException;
import android.widget.AdapterView.*;

public class MainActivity extends AppCompatActivity {
    public static final char Sep='/';

    TextView LblFileName;
    ListView LstFolder, LstFile;
    Button BtnChoose, BtnCancel;

    String Root="/mnt/", CurrentDir=Root, UpDir="", FolderNameList[], FileNameList[],
            RootDirInternal="/mnt/sdcard/", RootDirExternal="/mnt/emmc/";
    File CurrentFolder, FileList[];
    ArrayAdapter <String> AdpFolder, AdpFile;
    int i, numFolders, numFiles, posFile, posFolder;

    private static String GetUpDir(String DirName)
    {
        String result="";
        int i, j, l;
        l=DirName.length();
        i=l-2;
        while (DirName.charAt(i)!=Sep) i--;
        for(j=0; j<=i; j++)
            result=result+DirName.charAt(j);
        return result;
    }

    private void ShowFolders()
    {
//        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
//            Toast.makeText(this, "No SD Card Found", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, "SD Card Found", Toast.LENGTH_SHORT).show();
//        }
        CurrentFolder=new File(CurrentDir);
        FileList=CurrentFolder.listFiles();

        LblFileName.setText(CurrentDir);

        if (CurrentDir.equals(Root)) {
            FolderNameList=new String[3];
            FolderNameList[0]="..UpFolder";
            FolderNameList[1]=RootDirInternal;
            FolderNameList[2]=RootDirExternal;
            FileNameList=new String[0];
        } else {
            numFolders=0;
            numFiles=0;
            for(i=0; i<FileList.length; i++)
                if (FileList[i].isDirectory())
                    numFolders++;
                else if (FileList[i].isFile())
                    numFiles++;

            FolderNameList=new String[numFolders+1];;
            FileNameList=new String[numFiles];
            FolderNameList[0]=".. Up Folder";
            posFolder=1;
            posFile=0;

            for(i=0; i<FileList.length; i++) {
                if (FileList[i].isDirectory()) {
                    FolderNameList[posFolder]=FileList[i].getName()+Sep;
                    posFolder++;
                } else if (FileList[i].isFile()) {
                    FileNameList[posFile]=FileList[i].getName();
                    posFile++;
                }
            }
        }
        AdpFolder=new ArrayAdapter <String> (this, R.layout.act_list, R.id.LblFolderFile, FolderNameList);
        LstFolder.setAdapter(AdpFolder);
        AdpFile=new ArrayAdapter <String> (this, R.layout.act_list, R.id.LblFolderFile, FileNameList);
        LstFile.setAdapter(AdpFile);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LblFileName=(TextView)findViewById(R.id.LblFileName);
        LstFolder=(ListView)findViewById(R.id.LstFolder);
        LstFile=(ListView)findViewById(R.id.LstFile);
        BtnChoose=(Button)findViewById(R.id.BtnChoose);
        BtnCancel=(Button)findViewById(R.id.BtnCancel);

        ShowFolders();

        LstFolder.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                if (CurrentDir.equals(Root)) {
                    switch (position) {
                        case 1: CurrentDir=RootDirInternal; break;
                        case 2: CurrentDir=RootDirExternal; break;
                    }
                    UpDir=Root;
                    FolderNameList=new String[3];
                } else if (position==0) {
                    //if (!CurrentDir.equals(Root)) {
                    CurrentDir=GetUpDir(CurrentDir);
                    if (!CurrentDir.equals(Root))
                        UpDir=GetUpDir(CurrentDir);
                    //}
                } else {
                    UpDir=CurrentDir;
                    CurrentDir=CurrentDir+FileList[position-1].getName()+Sep;
                }
                ShowFolders();
            }
        });

        LstFile.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (position>=0)
                    LblFileName.setText(CurrentDir+FileNameList[position]);
            }
        });

        BtnChoose.setOnClickListener(
            new OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     Intent intent = new Intent(MainActivity.this, ViewImage.class);
                     intent.putExtra("filepath", LblFileName.getText());
                     startActivity(intent);
                 }
             }
        );
    }
}