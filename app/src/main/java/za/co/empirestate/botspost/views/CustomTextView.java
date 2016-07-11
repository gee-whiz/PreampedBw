package za.co.empirestate.botspost.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by joel on 15/02/26.
 */
public class CustomTextView extends TextView {
    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (this.isInEditMode()) return ;

      //  final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SomeStyle);
      //  final String customFont = a.getString(R.styleable.SomeStyle_font);

        //Build a custom typeface-cache here!
       setTypeface(
                Typeface.createFromAsset(context.getAssets(), "ufonts.com_futura-lt-bold.tff")
        );
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


    }

}
