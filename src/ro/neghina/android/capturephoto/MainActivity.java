package ro.neghina.android.capturephoto;

import ro.neghina.android.capturephoto.util.CapturePhoto;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MainActivity extends Activity implements OnClickListener {
		
	private ImageView photoLeft;
	private ImageView photoRight;
	
	private CapturePhoto capture;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		photoLeft = (ImageView)findViewById(R.id.photo_left);
		photoLeft.setOnClickListener(this);
		
		photoRight = (ImageView)findViewById(R.id.photo_right);
		photoRight.setOnClickListener(this);
		
		capture = new CapturePhoto(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		int targetID = v.getId();
		selectImage(targetID);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

	 	if (resultCode == RESULT_OK){
	 		ImageView imageView = (ImageView)findViewById(requestCode);
	 		if(capture.getActionCode() == CapturePhoto.PICK_IMAGE) {
	 			Uri targetUri = data.getData();
	 			if(targetUri != null)
	 				capture.handleMediaPhoto(targetUri, imageView);
	 		}
	 		else {
	 			capture.handleCameraPhoto(imageView);
	 		}
	 	}
	}
	
	private void selectImage(final int id) {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                	// take photo from camera
        			capture.dispatchTakePictureIntent(CapturePhoto.SHOT_IMAGE, id);
                } else if (items[item].equals("Choose from Library")) {
                	// take photo from gallery
                	capture.dispatchTakePictureIntent(CapturePhoto.PICK_IMAGE, id);
                } else if (items[item].equals("Cancel")) {
                	// close the dialog
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

}
