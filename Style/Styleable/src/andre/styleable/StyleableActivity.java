package andre.styleable;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

public class StyleableActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		TextView tv = (TextView) findViewById(R.id.text);
		View v = findViewById(R.id.imageView1);

		try {
			Context context = this.createPackageContext("andre.style.provider", CONTEXT_RESTRICTED);
			int styleId = context.getResources().getIdentifier("textStyle","style", context.getPackageName());

			applyStyle(tv, context, styleId);
			applyStyle(v,context,styleId);


		} catch (NameNotFoundException e) {
			Log.i("ORIANI", " PROBLEM");
		}


	}


	private void applyStyle(View view, Context context, int styleId){
		applyStyleView(view,context,styleId);

		if(view instanceof TextView){
			TextView tv = (TextView) view;
			applyStyleTextView(tv,context,styleId);
		}

		if(view instanceof ImageView){
			ImageView iv = (ImageView) view;
			applyStyleImageView(iv,context,styleId);
		}
	}


	//Check View.java implementation to learn how to add new attributes
	private void applyStyleView(View view, Context context, int styleId){
		int attrs[] = {
				android.R.attr.background, //R.stylable.View_background = 12
				android.R.attr.visibility, //R.stylable.View_visibility = 20
		};

		final int BACKGROUND = 0;
		final int VISIBILITY = 1;

		TypedArray a = context.obtainStyledAttributes(styleId, attrs);
		final int count = a.getIndexCount();
		for (int i = 0; i < count; i++) {
			int index = a.getIndex(i);
			switch(index){
			case BACKGROUND:
				Drawable drawable = a.getDrawable(index);
				if(drawable != null){view.setBackgroundDrawable(drawable);}
				break;
			case VISIBILITY:
				final int visibilityMap[] = {View.VISIBLE,View.INVISIBLE,View.GONE};
				int value = a.getInt(index, 0);
				view.setVisibility(visibilityMap[value]);
				break;
			}
		}
		a.recycle();
	}

	//Check View.java implementation to learn how to add new attributes
	private void applyStyleImageView(ImageView image, Context context, int styleId){

		int attrs[]={
				android.R.attr.src, // R.stylable.ImageView_src = 0
				android.R.attr.scaleType, //R.styleable.ImageView_scaleType = 1
		};

		final int SRC = 0;
		final int SCALETYPE = 1;

		final ScaleType[] scaleTypeArray = {
		        ScaleType.MATRIX,
		        ScaleType.FIT_XY,
		        ScaleType.FIT_START,
		        ScaleType.FIT_CENTER,
		        ScaleType.FIT_END,
		        ScaleType.CENTER,
		        ScaleType.CENTER_CROP,
		        ScaleType.CENTER_INSIDE
		    };

		TypedArray a = context.obtainStyledAttributes(styleId, attrs);
		final int count = a.getIndexCount();
		for (int i = 0; i < count; i++) {
			int index = a.getIndex(i);

			switch(index){
			case SRC:
				Drawable d = a.getDrawable(index);
				if (d != null){image.setImageDrawable(d);}
				break;
			case SCALETYPE:
				int pos = a.getInt(index, -1);
				if(pos>=0){
					image.setScaleType(scaleTypeArray[pos]);
				}
				break;
			}
		}
		a.recycle();
	}

	//Check TextView.java implementation to learn how to add new attributes
	private void applyStyleTextView(TextView textView, Context context, int styleId) {

		//THE ORDER OF ATTRS MUST OBEY THE ORDER OF STYLABLE VALUES TO WORK
		int attrs[] = {
				android.R.attr.textSize, // R.stylable.TextView_textSize = 2
				android.R.attr.typeface, // R.stylable.TextView_typeface = 3
				android.R.attr.textStyle, // R.stylable.TextView_textStyle = 4
				android.R.attr.textColor, // R.stylable.TextView_textColor = 5
				android.R.attr.gravity, // R.stylable.TextView_gravity = 10
				android.R.attr.text, // R.stylable.TextView_text = 18
				android.R.attr.maxLines, // R.stylable.TextView_maxLines = 22
				android.R.attr.minLines, // R.stylable.TextView_minLines = 25
		};

		final int TEXTSIZE = 0;
		final int TYPEFACE = 1;
		final int TEXTSTYLE = 2;
		final int TEXTCOLOR = 3;
		final int GRAVITY = 4;
		final int TEXT = 5;
		final int MAXLINES = 6;
		final int MINLINES = 7;

		TypedArray a = context.obtainStyledAttributes(styleId, attrs);

		final int count = a.getIndexCount();
		for (int i = 0; i < count; i++) {
			int index = a.getIndex(i);

			switch (index) {
			case TEXTSIZE:
				int textSize = a.getDimensionPixelSize(index, 15);
				textView.setTextSize(textSize);
				break;

			case TYPEFACE:
				final int SANS = 1;
				final int SERIF = 2;
				final int MONOSPACE = 3;

				int typefaceIndex = a.getInt(index, -1);
				Typeface tf = null;
				switch (typefaceIndex) {
				case SANS:
					tf = Typeface.SANS_SERIF;
					break;

				case SERIF:
					tf = Typeface.SERIF;
					break;

				case MONOSPACE:
					tf = Typeface.MONOSPACE;
					break;
				}
				textView.setTypeface(tf);
				break;

			case TEXTSTYLE:
				int styleIndex = a.getInt(index, -1);
				textView.setTypeface(textView.getTypeface(), styleIndex);
				break;

			case TEXTCOLOR:
				ColorStateList textColor = a.getColorStateList(index);
				textView.setTextColor(textColor != null ? textColor : ColorStateList
						.valueOf(0xFF000000));
				break;

			case GRAVITY:
				textView.setGravity(a.getInt(index,-1));
				break;

			case TEXT:
				textView.setText(a.getText(index));
				break;

			case MINLINES:
				textView.setMinLines(a.getInt(index,-1));
				break;

			case MAXLINES:
				textView.setMaxLines(a.getInt(index,-1));
				break;
			}
		}

		a.recycle();
	}
}