/*
 *
 *  * Created by Murillo Comino on 09/02/19 12:26
 *  * Github: github.com/MurilloComino
 *  * StackOverFlow: pt.stackoverflow.com/users/128573
 *  * Email: murillo_comino@hotmail.com
 *  *
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 09/02/19 12:11
 *
 */

package com.onimus.munote.files;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import com.onimus.munote.R;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.onimus.munote.Constants.*;

public class ImageUtilities {
    private static File image;
    private String pathYearMonth;
    private String pathDir;
    private static File fileDir;

    private String path1;
    private static String noPath;
    private Context context;

    public ImageUtilities(Context context) {
        this.context = context;
    }

    public void createDirectory(String nomePasta) {

        File dir;

        String timeYear;
        String timeMonth;
        String timeAll;

        timeYear = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
        timeMonth = new SimpleDateFormat("MMMM", Locale.ENGLISH).format(new Date());
        pathYearMonth = timeYear + "/" + timeMonth;
        timeAll = FOLDER_NAME + nomePasta + pathYearMonth;

        //Cria pasta
        dir = createReturnDir(timeAll);

        //Verifica se a pasta não existe e não foi criado
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.d(LOG_D, "Folder not created");
            }
        }
        pathDir = dir.getAbsolutePath();
        fileDir = dir;
    }

    @SuppressWarnings("deprecation")
    private File createReturnDir(String pathDir) {
        File path;
        File dir;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P){
            path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        } else {
            path = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        }

        dir = new File(path, pathDir);

        return dir;
    }

    public File createImageFile() {
        // Create an image file name
        String timeFileName = new SimpleDateFormat("dd_HHmmss", Locale.getDefault()).format(new Date()) + ".jpg";
        image = new File(pathDir, timeFileName);

        // Save a file: path for use with ACTION_VIEW intents
        path1 = image.getAbsolutePath();
        noPath = pathYearMonth + "/" + timeFileName;
        return image;
    }

    public static String returnNoPath() {
        return noPath;
    }

    public void setPhotoToBitmap(ImageView iv_photo) {
        int widthPhoto;
        int heightPhoto;

        MainUtilities mainUtilities = new MainUtilities();

        //Código para pegar height e width do ImageView
        // width = iv_photo.getWidth();
        // height = iv_photo.getHeight();

        //Cria essa tag pra fazer verificação se é uma nova imagem ou a imagem padrão;
        iv_photo.setTag("new");

        //Não gera um bitmap. Ele inicializa a variavel options com as informações
        // vindas da imagem: height e width;
        BitmapFactory.Options options = new BitmapFactory.Options();
        //não consome a foto
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path1, options);

        widthPhoto = options.outWidth;
        heightPhoto = options.outHeight;

        int height = 512;
        int width = 400;
        // Determine how much to scale down the image
        if (widthPhoto == heightPhoto || widthPhoto > heightPhoto) {
            int scaleFactor = Math.min(widthPhoto / height, heightPhoto / width);

            // Decode the image file into a Bitmap sized to fill the View
            options.inJustDecodeBounds = false;
            options.inSampleSize = scaleFactor;

            Bitmap bitmap = rotateImage(BitmapFactory.decodeFile(path1, options));
            if (!path1.isEmpty()) {
                try {
                    //Salva a imagem rotacionada no arquivo que já havia sido criado
                    File file = new File(path1);
                    FileOutputStream fOut = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fOut);
                    fOut.flush();
                    fOut.close();
                } catch (Exception ignored) {

                }

            } else {
                mainUtilities.setMessage(context, R.string.error_folder_creater, false);

            }
            iv_photo.setImageBitmap(bitmap);
        } else {

            int scaleFactor = Math.min(widthPhoto / width, heightPhoto / height);

            // Decode the image file into a Bitmap sized to fill the View
            options.inJustDecodeBounds = false;
            options.inSampleSize = scaleFactor;

            Bitmap bitmap = BitmapFactory.decodeFile(path1, options);
            if (!path1.isEmpty()) {
                try {
                    //Salva a imagem no arquivo que já havia sido criado
                    File file = new File(path1);
                    FileOutputStream fOut = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fOut);
                    fOut.flush();
                    fOut.close();
                } catch (Exception ignored) {

                }


            } else {
                mainUtilities.setMessage(context, R.string.error_folder_creater, false);
            }
            iv_photo.setImageBitmap(bitmap);

        }
    }

    private static Bitmap rotateImage(Bitmap source) {

        Matrix matrix = new Matrix();
        matrix.postRotate((float) 90);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public static void deleteImage() {
        if (image.exists()) {
            //noinspection ResultOfMethodCallIgnored
            image.delete();
        }
    }

    static void deleteImage(File imgFile) {
        if (imgFile.exists()) {
            //noinspection ResultOfMethodCallIgnored
            imgFile.delete();
        }
    }

    public static boolean checkDirectory() {
        return !fileDir.exists();
    }

}
