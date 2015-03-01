package com.wordpress.bhasser.MemeMaker;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class Rotate extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rotate);
		ImageView image = (ImageView) findViewById(R.id.imgView);
        Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.rotator);
        Bitmap bMapScaled = Bitmap.createScaledBitmap(bMap, 150, 100, true);
        image.setImageBitmap(bMapScaled);
	}
}
