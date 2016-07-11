package za.co.empirestate.botspost.views;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Created by joel on 15/02/26.
 */
public class CustomTextView2 extends TextView {

    public CustomTextView2(Context context) {
        super(context);

        if (this.isInEditMode()) return ;

        //  final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SomeStyle);
        //  final String customFont = a.getString(R.styleable.SomeStyle_font);

        //Build a custom typeface-cache here!
        this.setTypeface(
                Typeface.createFromAsset(context.getAssets(), "ufonts.com_futura-lt-medium.ttf"));
    }
}
