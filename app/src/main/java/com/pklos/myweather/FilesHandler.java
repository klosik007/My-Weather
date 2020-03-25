package com.pklos.myweather;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Scanner;

public class FilesHandler {
    Context _context;

    public FilesHandler(Context context){
        _context = context;
    }

    public String readFile(String fileName){
        try {
            FileInputStream fs = _context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fs);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) !=null){
                sb.append(line);
            }

            return sb.toString();
        } catch (FileNotFoundException fileNotFound){
            return null;
        } catch (IOException ioException){
            return null;
        }
    }

    public boolean createFile(String fileName, String jsonString){
        try{
            FileOutputStream fos = _context.openFileOutput(fileName, Context.MODE_PRIVATE);
            if(jsonString != null){
                fos.write(jsonString.getBytes());
            }
            fos.close();
            return true;
        } catch (FileNotFoundException fileNotFound){
            return false;
        } catch (IOException ioException) {
            return false;
        }
    }

    public boolean isFileAvailable(String fileName){
        String path = _context.getFilesDir().getAbsolutePath() + "/" + fileName;
        Log.d("FilesHandler", path);
        File file = new File(path);
        return file.exists();
    }

    public static String readerToString(Reader reader){
//        BufferedReader in = new BufferedReader(reader);
//        String line = null;
//        StringBuilder rslt = new StringBuilder();
//        while ((line = in.readLine()) != null) {
//            rslt.append(line);
//        }
        String text = new Scanner(reader).useDelimiter("\\Z").next();
        return text;
    }

    public static String readerToString2(Reader reader) throws IOException{
        char[] arr = new char[8*1024]; // 8K at a time
        StringBuffer buf = new StringBuffer();
        int numChars;

        while ((numChars = reader.read(arr, 0, arr.length)) > 0) {
            buf.append(arr, 0, numChars);
        }

        return buf.toString();
    }
}
