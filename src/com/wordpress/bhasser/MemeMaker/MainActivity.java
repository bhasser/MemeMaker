package com.wordpress.bhasser.MemeMaker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.wordpress.bhasser.MemeMaker.*;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;



public class MainActivity extends Activity {
   
	private static final int SELECT_PICTURE = 1;
	EditText et_EditTextTop;
	EditText et_EditTextMiddle;
	EditText et_EditTextBottom;
	Bitmap src;
	ImageView iv;
	Canvas ivCanvas;
	Bitmap ourNewBitmap;
	Button buttonShare;
	boolean picSelected = false;

    private String selectedImagePath;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ((Button) findViewById(R.id.buttonLoadPicture))
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View arg0) {

                        // in onCreate or any event where your want the user to
                        // select a file
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent,
                                "Select Picture"), SELECT_PICTURE);
                        
                    }
                });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                picSelected = true;
                findViewById(R.id.buttonLoadPicture).setVisibility(View.INVISIBLE);
            }
        }
        
        et_EditTextTop = (EditText) findViewById(R.id.editTextTop);
        et_EditTextMiddle = (EditText) findViewById(R.id.editTextMiddle);
        et_EditTextBottom = (EditText) findViewById(R.id.editTextBottom);
        
        buttonShare = (Button) findViewById(R.id.buttonShare);
        
        buttonShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent share = new Intent(Intent.ACTION_SEND);
				share.setType("image/*");
				ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				ourNewBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
				File f = new File(Environment.getExternalStorageDirectory()
						+ "/MemeMaker.jpg");
				try {
					f.createNewFile();
					FileOutputStream fo = new FileOutputStream(f);
					fo.write(bytes.toByteArray());
					fo.close();
					bytes.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				share.putExtra(Intent.EXTRA_STREAM,
						Uri.parse("file:///sdcard/MemeMaker.jpg"));
				startActivity(Intent.createChooser(share, "Share Image"));
			}
		});
    	
		
        if(picSelected == true){
        	src = BitmapFactory.decodeFile((selectedImagePath));
        
		try {
            ExifInterface exif = new ExifInterface(selectedImagePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            }
            else if (orientation == 3) {
                matrix.postRotate(180);
            }
            else if (orientation == 8) {
                matrix.postRotate(270);
            }
            src = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true); // rotating bitmap
        }
        catch (Exception e) {

        }
		iv = (ImageView) findViewById(R.id.imgView);
		iv.setImageBitmap(src);
		ourNewBitmap = Bitmap.createBitmap(src.getWidth(), src.getHeight(),
				Bitmap.Config.ARGB_8888);
		ivCanvas = new Canvas(ourNewBitmap);
		
		Paint paint = new Paint();
		ivCanvas.drawBitmap(src, 0, 0, paint);
        }
		
		et_EditTextTop.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				updateCanvas();
			}
			
			
		});
				
		et_EditTextMiddle.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				updateCanvas();
			}
			
			
		});
		
		et_EditTextBottom.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				updateCanvas();
			}
			
			
		});
						
		
		
		
    }
    
    /**
     * helper to retrieve the path of an image URI
     */
    public String getPath(Uri uri) {
            // just some safety built in 
            if( uri == null ) {
                // TODO perform some logging or show user feedback
                return null;
            }
            // try to retrieve the image from the media store first
            // this will only work for images selected from gallery
            String[] projection = { MediaStore.Images.Media.DATA };
            Cursor cursor = managedQuery(uri, projection, null, null, null);
            if( cursor != null ){
                int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            }
            // this is our fallback here
            return uri.getPath();
    }
    
    
    public void updateCanvas() {
    
		Paint paint = new Paint();
		ivCanvas.drawBitmap(src, 0, 0, paint);
		paint.setColor(Color.RED);		
		paint.setTextAlign(Align.CENTER);
		
		Typeface tf = Typeface.create("Times New Roman",Typeface.BOLD);
		paint.setTypeface(tf);
		
		float verttop = 0.15f;		
		paint.setTextSize(200);
		ivCanvas.drawText(et_EditTextTop.getText().toString(), (ivCanvas.getWidth()/2), (ivCanvas.getHeight()*verttop), paint);
		
		float vertmiddle = 0.50f;		
		paint.setTextSize(200);
		ivCanvas.drawText(et_EditTextMiddle.getText().toString(), (ivCanvas.getWidth()/2), (ivCanvas.getHeight()*vertmiddle), paint);
		
		float vertbottom = 0.85f;		
		paint.setTextSize(200);
		ivCanvas.drawText(et_EditTextBottom.getText().toString(), (ivCanvas.getWidth()/2), (ivCanvas.getHeight()*vertbottom), paint);
						
		// Everything has been drawn to bmp, so we can set that here, now.
		iv.setImageBitmap(ourNewBitmap);

	}
   
    

}
