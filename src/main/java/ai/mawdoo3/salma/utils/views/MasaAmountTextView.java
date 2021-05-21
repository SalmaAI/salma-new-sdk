package ai.mawdoo3.salma.utils.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;

import ai.mawdoo3.salma.R;
import ai.mawdoo3.salma.utils.AppUtils;

/**
 * Created by iSaleem on 12/21/20
 */
public class MasaAmountTextView extends AppCompatTextView {


    private String afterChar;
    private int amountType;


    public MasaAmountTextView(Context context) {
        this(context, null, 0);
    }

    public MasaAmountTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        init(context, attrs, 0);
    }

    public MasaAmountTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }


    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MasaAmountTextView);


        if (array != null) {

            afterChar = array.getString(R.styleable.MasaAmountTextView_reduceAfterChar);
            amountType = array.getInteger(R.styleable.MasaAmountTextView_amountTextType, 0);
            setTypeface(context);


            array.recycle();

            CharSequence charSequence = getText();
            if (charSequence != null) {
                this.setText(charSequence);
            }


        }


    }

    private void setTypeface(Context context) {
        Typeface typeface;
        AmountType type = AmountType.getAmountTypeByID(amountType);
        //set font family
        typeface = ResourcesCompat.getFont(context, type.FONT);
        setTypeface(typeface);
        //set text size
        setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(type.AMOUNT_SIZE));
        //set text color
        setTextColor(AppUtils.INSTANCE.getColorFromAttr(context, type.AMOUNT_TEXT_COLOR));

    }

    public SpannableStringBuilder getSpannableText(String text) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        if (!text.isEmpty() && text.contains(afterChar)) {
            AmountType type = AmountType.getAmountTypeByID(amountType);

            builder.setSpan(
                    new AbsoluteSizeSpan((int) getResources().getDimension(type.FRACTION_SIZE)),
                    text.indexOf(afterChar),
                    text.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }
        return builder;
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (afterChar != null) {
            if (text != null) {
                SpannableStringBuilder spannableString = getSpannableText(text.toString());
                super.setText(spannableString, type);
            }
        } else {
            super.setText(text, type);
        }
    }

    public enum AmountType {

        AMOUNT_1(0, R.font.font_bold, R.dimen.amount_1, R.dimen.amount_1_fractions, R.attr.amount1TextColor),
        AMOUNT_2(1, R.font.font_regular, R.dimen.amount_2, R.dimen.amount_2_fractions, R.attr.amount2TextColor);

        private final int ID;
        private final int FONT;
        private final int AMOUNT_SIZE;
        private final int FRACTION_SIZE;
        private final int AMOUNT_TEXT_COLOR;

        AmountType(int id, int font, int amountSize, int fractionSize, int amountTextColor) {
            this.FONT = font;
            this.AMOUNT_SIZE = amountSize;
            this.FRACTION_SIZE = fractionSize;
            this.ID = id;
            this.AMOUNT_TEXT_COLOR = amountTextColor;
        }

        public static AmountType getAmountTypeByID(int id) {
            for (AmountType type : AmountType.values()) {
                if (type.getID() == id) {
                    return type;
                }
            }
            return null;
        }

        public int getFONT() {
            return FONT;
        }

        public int getID() {
            return ID;
        }

        public int getAmountSize() {
            return AMOUNT_SIZE;
        }

        public int getFractionSize() {
            return FRACTION_SIZE;
        }
    }
}