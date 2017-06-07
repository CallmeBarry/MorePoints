package com.barry.morepoints;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DrawingView _view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _view = (DrawingView) findViewById(R.id.DrawingView);
        Bitmap bmp1 = BitmapFactory.decodeResource(getResources(),R.drawable.waiyi);
        Bitmap bmp2 = BitmapFactory.decodeResource(getResources(),R.drawable.duanqun);
        Bitmap bmp3 = BitmapFactory.decodeResource(getResources(),R.drawable.cwtao);
        Bitmap bmp4 = BitmapFactory.decodeResource(getResources(),R.drawable.dk);
        Bitmap bmp5 = BitmapFactory.decodeResource(getResources(),R.drawable.ck);
        CustomBitmap customBitmap1 = new CustomBitmap(bmp1);
        CustomBitmap customBitmap2 = new CustomBitmap(bmp2);
        CustomBitmap customBitmap3 = new CustomBitmap(bmp3);
        CustomBitmap customBitmap4 = new CustomBitmap(bmp4);
        CustomBitmap customBitmap5 = new CustomBitmap(bmp5);
        customBitmap1.setId(1);
        customBitmap2.setId(2);
        customBitmap3.setId(3);
        customBitmap4.setId(4);
        customBitmap5.setId(5);
        if (getSavedMatrix(1) != null){
            Log.e("tag", "matrix 1 is not null");
            customBitmap1.setMatrix(getSavedMatrix(1));

        }
        if (getSavedMatrix(2) != null){
            Log.e("tag", "matrix 2 is not null");
            customBitmap2.setMatrix(getSavedMatrix(2));
        }
        if (getSavedMatrix(3) != null){
            Log.e("tag", "matrix 2 is not null");
            customBitmap3.setMatrix(getSavedMatrix(3));
        }
        if (getSavedMatrix(4) != null){
            Log.e("tag", "matrix 2 is not null");
            customBitmap4.setMatrix(getSavedMatrix(4));
        }
        if (getSavedMatrix(5) != null){
            Log.e("tag", "matrix 2 is not null");
            customBitmap5.setMatrix(getSavedMatrix(5));
        }
        _view.addBitmap(customBitmap1);
        _view.addBitmap(customBitmap2);
        _view.addBitmap(customBitmap3);
        _view.addBitmap(customBitmap4);
        _view.addBitmap(customBitmap5);
    }

    //保存matrix
    private void saveMatrix(CustomBitmap customBitmap){
        Log.e("tag", "save matrix" + customBitmap.getId());
        SharedPreferences.Editor editor = getSharedPreferences("matrix", 1).edit();
        Matrix matrix = customBitmap.matrix;
        float[] values = new float[9];
        matrix.getValues(values);
        JSONArray array = new JSONArray();
        for (float value:values){
            try {
                array.put(value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        editor.putString(String.valueOf(customBitmap.getId()), array.toString());
        editor.commit();
        Log.e("tag", "save matrix id:" + customBitmap.getId() + "---------"+values[Matrix.MPERSP_0] + " , " + values[Matrix.MPERSP_1] + " , " +
                values[Matrix.MPERSP_2] + " , " + values[Matrix.MSCALE_X] + " , " +
                values[Matrix.MSCALE_Y] + " , " + values[Matrix.MSKEW_X] + " , " +
                values[Matrix.MSKEW_Y] + " , " +values[Matrix.MTRANS_X] + " , " +
                values[Matrix.MTRANS_Y]);
    }

    //获取matrix
    private Matrix getSavedMatrix(int id){
        SharedPreferences sp = getSharedPreferences("matrix", 1);
        String result = sp.getString(String.valueOf(id), null);
        if (result != null){
            float[] values = new float[9];
            Matrix matrix = new Matrix();
            try {
                JSONArray array = new JSONArray(result);
                for (int i = 0; i < array.length(); i++) {
                    values[i] = Float.valueOf(String.valueOf(array.getDouble(i)));
                }
                matrix.setValues(values);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("tag", "get matrix id:" + id + "---------"+values[Matrix.MPERSP_0] + " , " + values[Matrix.MPERSP_1] + " , " +
                    values[Matrix.MPERSP_2] + " , " + values[Matrix.MSCALE_X] + " , " +
                    values[Matrix.MSCALE_Y] + " , " + values[Matrix.MSKEW_X] + " , " +
                    values[Matrix.MSKEW_Y] + " , " +values[Matrix.MTRANS_X] + " , " +
                    values[Matrix.MTRANS_Y]);

            return matrix ;
        }
        return null;
    }

    @Override
    public void finish() {
        List<CustomBitmap> list = _view.getViews();
        for (CustomBitmap customBitmap:list){
            saveMatrix(customBitmap);
        }
        super.finish();
    }
}
