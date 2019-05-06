package com.example.picture;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private ImageView imageView2;
    private Button button;
    private Button button2;
    private Drawable drawable;

    /**
     * 文件夹下图片的真实路径
     */
    private String scanpath;
    /**
     * 所有图片的名字
     */
    public String[] allFiles;
    /**
     * 想要查找的文件夹
     */
    private File folder;
    private List<ImageView> mItems = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.commit_button);//确定按钮：移出图片
        button2 = findViewById(R.id.scan_button);//扫描按钮：显示图片
        imageView = findViewById(R.id.imageView);
        imageView2 = findViewById(R.id.imageView2);
        myPermission();

//        SDCardListener listener = new SDCardListener(getAbsolutePath());
//开始监听
//        listener.startWatching();
        /*
         * 在这里做一些操作，比如创建目录什么的
         */


        //showPicture(imageView,imageView2);//显示图片

        //移出图片
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveFile();
                imageView.setImageBitmap(null);
                imageView2.setImageBitmap(null);
            }
        });
        //设置图片
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                moveFile2();//移动所有图片到原来文件夹
                showPicture(imageView,imageView2);//显示图片
            }
        });

        //最后停止监听
//        listener.stopWatching();
    }
    //文件夹监听
    class SDCardListener extends FileObserver {

        public SDCardListener(String path) {
            /*
             * 这种构造方法是默认监听所有事件的,如果使用super(String,int)这种构造方法，
             * 则int参数是要监听的事件类型.
             */
            super(path);
//            super(path,FileObserver.CREATE);
        }

        @Override
        public void onEvent(int event, String path) {
            switch(event) {
                case FileObserver.ALL_EVENTS:
                    Log.d("all", "path:"+ path);
                    break;
                case FileObserver.CREATE:
                    Log.d("Create", "path:"+ path);
                    System.out.println("创建文件！");
                    break;
                case FileObserver.MOVED_FROM:
                    Log.d("MOVED_FROM", "path:"+ path);
                    System.out.println("移动文件！");
                    break;
                case FileObserver.MOVED_TO://图片加载进目标文件夹
                    Log.d("MOVED_TO", "path:"+ path);
                    System.out.println("移动到该文件夹下！");
                    showPicture(imageView,imageView2);//显示图片
                    break;
                case FileObserver.MODIFY:
                    Log.d("MODIFY", "path:"+ path);
                    System.out.println("修改文件！");
                    break;
            }
        }
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public void myPermission() {
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    /**
         * 遍历文件名显示图片
         */
        private void showPicture(ImageView imageView1,ImageView imageView2){
            String path = getAbsolutePath();
            String name = "";
            String suffix = "";
            int count = 0;
            File onefile = new File(path);
            File[] allfiles = onefile.listFiles();

            for (int i = 0; i < allfiles.length; i++) {
                name = allfiles[i].getName();
                int index = name.lastIndexOf(".")+1;
                suffix = name.substring(index);

                if (suffix.equals("JPG")){
                    String filePath = getAbsolutePath()+name;
                    Bitmap bm = BitmapFactory.decodeFile(filePath);
                    count++;
                    if (count==1){
                        imageView.setImageBitmap(bm);
                    }else{
                        imageView2.setImageBitmap(bm);
                    }
                }
            }
        }


        private void moveFile(){
            String name = "";
            String suffix = "";
            String path = getAbsolutePath();
            String targetPath = getTargetPath();
            InputStream inStream = null;
            OutputStream outStream = null;
            File onefile = new File(path);
            File[] files = onefile.listFiles();
            for (File f:files
            ) {
                name = f.getName();
                int index = name.lastIndexOf(".")+1;
                suffix = name.substring(index);
                if (suffix.equals("JPG")){
                    String targetPath2 = targetPath + f.getName();//目标文件路径
                    File file2 = new File(targetPath2);
                    f.renameTo(file2);
                }
            }
    }
    private void moveFile2(){
        String name = "";
        String suffix = "";
        String path = getTargetPath();
        String targetPath = getAbsolutePath();
        InputStream inStream = null;
        OutputStream outStream = null;
        File onefile = new File(path);
        File[] files = onefile.listFiles();
        for (File f:files
        ) {
            name = f.getName();
            int index = name.lastIndexOf(".")+1;
            suffix = name.substring(index);
            if (suffix.equals("JPG")){
                String targetPath2 = targetPath + f.getName();//目标文件路径
                File file2 = new File(targetPath2);
                f.renameTo(file2);
            }
        }
    }

        private void moveFile(String oldPath,String targetPath){
            InputStream inStream = null;
            OutputStream outStream = null;

            try{

                File afile =new File(oldPath);
                File bfile =new File(targetPath);

                inStream = new FileInputStream(afile);
                outStream = new FileOutputStream(bfile);

                byte[] buffer = new byte[1024];

                int length;
                //copy the file content in bytes
                while ((length = inStream.read(buffer)) > 0){

                    outStream.write(buffer, 0, length);

                }

                inStream.close();
                outStream.close();

                //delete the original file
                afile.delete();

                System.out.println("File is copied successful!");

            }catch(IOException e){
                e.printStackTrace();
            }
        }

    /**
     * 刷新文件夹
     */
    /*public void rescanSdcard() {
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                Uri.parse("file://"+ Environment.getExternalStorageDirectory().getAbsolutePath())));
    }*/
    private String getAbsolutePath(){
        String str = getExternalSdCardPath();
        String path = str + "/Mobi/";
        return path;
    }
    private String getTargetPath(){
        String str = getExternalSdCardPath();
        String path = str + "/Mobi/Exports/";
        return path;
    }

    public static String getExternalSdCardPath() {
        String  path = null;
        String state = Environment.getExternalStorageState();
        //Environment.getExternalStorageDirectory()获取的是内部SDcard
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            path = Environment.getExternalStorageDirectory().getPath();
        } else {  //Environment.getExternalStorageDirectory()获取的是外部SDcard，但没插外部sdcard
            List<String> devMountList = getDevMountList();
            for (String devMount : devMountList) {
                File file = new File(devMount);
                if (file.isDirectory() && file.canWrite()) {
                    path = file.getAbsolutePath();
                    break;
                }
            }
        }
        Log.d("", "getExternalSdCardPath: path = "+path);
        return path;
    }
    //照片路径：/storage/emulated/0/DCIM/Camera/IMG_20190425_082020.jpg
    //获取android所有的挂载点
    private static List<String> getDevMountList() {
        List<String> mVold = new ArrayList<String>(10);
        mVold.add("/mnt/sdcard");

        try {
            File voldFile = new File("/system/etc/vold.fstab");
            if(voldFile.exists()){
                Scanner scanner = new Scanner(voldFile);
                while (scanner.hasNext()) {
                    String line = scanner.nextLine();
                    if (line.startsWith("dev_mount")) {
                        String[] lineElements = line.split(" ");
                        String element = lineElements[2];
                        Log.d("", "Vold: element = "+element);

                        if (element.contains(":"))
                            element = element.substring(0, element.indexOf(":"));
                        if (!element.equals("/mnt/sdcard"))
                            mVold.add(element);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mVold;
    }
}