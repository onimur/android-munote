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

package com.example.admin.munotes.files;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import com.example.admin.munotes.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.example.admin.munotes.Constants.FOLDER_NAME;

public class ImageUtilities {
    private static File image;
    private String caminhoYearMonth;
    String caminhodirPath;
    private static File fileDir;

    private String caminho;
    private static String caminhoSemPath;


    public void createDirectory(String nomePasta) throws IOException {

        File dir;

        String timeYear;
        String timeMoth;
        String timeAll;

        timeYear = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
        timeMoth = new SimpleDateFormat("MMMM", Locale.ENGLISH).format(new Date());
        caminhoYearMonth = timeYear + "/" + timeMoth;
        timeAll = FOLDER_NAME + nomePasta + caminhoYearMonth;


        //Cria pasta
        dir = createReturnDir(timeAll);

        //Verifica se a pasta não existe e não foi criado
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.d("LabCamera", "Pasta não criada");
            }
        }
        caminhodirPath = dir.getAbsolutePath();
        fileDir = dir;
    }

    private File createReturnDir(String pathDir) {
        File path;
        File dir;
        path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        dir = new File(path, pathDir);

        return dir;
    }

    public File createImageFile() throws IOException {
        // Create an image file name
        String timeFileName = new SimpleDateFormat("dd_HHmmss", Locale.getDefault()).format(new Date()) + ".jpg";
        image = new File(caminhodirPath, timeFileName);

        // Save a file: path for use with ACTION_VIEW intents
        caminho = image.getAbsolutePath();
        caminhoSemPath = caminhoYearMonth + "/" + timeFileName;
        return image;
    }
    public static String returnCaminhoSemPath() {
        return caminhoSemPath;
    }

    public void setPhotoToBitmap(Context context, ImageView iv_foto) {
        int larguraFOTO;
        int alturaFOTO;

       MainUtilities mainUtilities = new MainUtilities();

        //Código para pegar altura e largura do ImageView
        // largura = iv_foto.getWidth();
        // altura = iv_foto.getHeight();

        //Cria essa tag pra fazer verificação se é uma nova imagem ou a imagem padrão;
        iv_foto.setTag("new");

        //Não gera um bitmap. Ele inicializa a variavel options com as informações
        // vindas da imagem: altura e largura;
        BitmapFactory.Options options = new BitmapFactory.Options();
        //não consome a foto
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(caminho, options);

        larguraFOTO = options.outWidth;
        alturaFOTO = options.outHeight;


        int altura = 512;
        int largura = 400;
        // Determine how much to scale down the image
        if (larguraFOTO == alturaFOTO || larguraFOTO > alturaFOTO) {
            int scaleFactor = Math.min(larguraFOTO / altura, alturaFOTO / largura);

            // Decode the image file into a Bitmap sized to fill the View
            options.inJustDecodeBounds = false;
            options.inSampleSize = scaleFactor;

            Bitmap bitmap = rotateImage(BitmapFactory.decodeFile(caminho, options), 90);
            if (!caminho.isEmpty()) {
                try {
                    //Salva a imagem rotacionada no arquivo que já havia sido criado
                    File file = new File(caminho);
                    FileOutputStream fOut = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fOut);
                    fOut.flush();
                    fOut.close();
                } catch (Exception ignored) {

                }

            } else {
                mainUtilities.setMessage(context, R.string.error_folder_creater);

            }
            iv_foto.setImageBitmap(bitmap);
        } else {

            int scaleFactor = Math.min(larguraFOTO / largura, alturaFOTO / altura);

            // Decode the image file into a Bitmap sized to fill the View
            options.inJustDecodeBounds = false;
            options.inSampleSize = scaleFactor;

            Bitmap bitmap = BitmapFactory.decodeFile(caminho, options);
            if (!caminho.isEmpty()) {
                try {
                    //Salva a imagem no arquivo que já havia sido criado
                    File file = new File(caminho);
                    FileOutputStream fOut = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fOut);
                    fOut.flush();
                    fOut.close();
                } catch (Exception ignored) {

                }


            } else {
                mainUtilities.setMessage(context, R.string.error_folder_creater);
            }
            iv_foto.setImageBitmap(bitmap);

        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public static void deleteImage() {
        if (image.exists()) {
            image.delete();
        }
    }

    public static void deleteImage(File imgFile) {
        if (imgFile.exists()) {
            imgFile.delete();
        }
    }
    public static boolean checkDirectory() {
        return !fileDir.exists();
    }
}
