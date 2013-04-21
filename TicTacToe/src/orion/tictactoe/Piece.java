/**
 * 
 */
package orion.tictactoe;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * 
 * @author andre
 *
 */
public enum Piece {
	EMPTY(0),
	NOUGHT(R.drawable.nought),
	CROSS(R.drawable.cross);
		
	/**The resource for drawing the state*/
	private int mMediaResource;
	
	private Piece(int resource){
	    mMediaResource = resource;
	}
	
	public Drawable getMedia(Context context){
	    if(mMediaResource != 0){
	        //Hopefully cache of Resource class won't allow multiple
	        // drawable for the same media
	        return context.getResources().getDrawable(mMediaResource);
	    }else return null;
	}
}
