package com.wo1haitao.controls;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wo1haitao.R;

/**
 * Created by user on 9/7/2017.
 */

public class CustomRatingBar extends LinearLayout {
    //internal component
    ImageView rating_star1, rating_star2, rating_star3, rating_star4, rating_star5;
    int numRating = 0;

    public int getNumRating() {
        return numRating;
    }

    public void setNumRating(int numRating) {
        this.numRating = numRating;
    }

    public CustomRatingBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initControl(context);
    }

    public CustomRatingBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context);
    }

    public CustomRatingBar(Context context) {
        super(context);
        initControl(context);
    }

    private void initControl(Context context)
    {
        View customRating = inflate(getContext(), R.layout.custom_rating_bar, null);
        addView(customRating);
        // layout is inflated, assign local variables to components
        rating_star1 = (ImageView) customRating.findViewById(R.id.rating_star1);
        rating_star2 = (ImageView) customRating.findViewById(R.id.rating_star2);
        rating_star3 = (ImageView) customRating.findViewById(R.id.rating_star3);
        rating_star4 = (ImageView) customRating.findViewById(R.id.rating_star4);
        rating_star5 = (ImageView) customRating.findViewById(R.id.rating_star5);

        rating_star1.setOnClickListener(onClickStar);
        rating_star2.setOnClickListener(onClickStar);
        rating_star3.setOnClickListener(onClickStar);
        rating_star4.setOnClickListener(onClickStar);
        rating_star5.setOnClickListener(onClickStar);
    }

    ImageView.OnClickListener onClickStar = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.rating_star1:{
                    rating_star1.setImageResource(R.drawable.star_filled);
                    rating_star2.setImageResource(R.drawable.star_empty);
                    rating_star3.setImageResource(R.drawable.star_empty);
                    rating_star4.setImageResource(R.drawable.star_empty);
                    rating_star5.setImageResource(R.drawable.star_empty);
                    CustomRatingBar.this.numRating = 1;
                    break;
                }
                case R.id.rating_star2:{
                    rating_star1.setImageResource(R.drawable.star_filled);
                    rating_star2.setImageResource(R.drawable.star_filled);
                    rating_star3.setImageResource(R.drawable.star_empty);
                    rating_star4.setImageResource(R.drawable.star_empty);
                    rating_star5.setImageResource(R.drawable.star_empty);
                    CustomRatingBar.this.numRating = 2;
                    break;
                }
                case R.id.rating_star3:{
                    rating_star1.setImageResource(R.drawable.star_filled);
                    rating_star2.setImageResource(R.drawable.star_filled);
                    rating_star3.setImageResource(R.drawable.star_filled);
                    rating_star4.setImageResource(R.drawable.star_empty);
                    rating_star5.setImageResource(R.drawable.star_empty);
                    CustomRatingBar.this.numRating = 3;
                    break;
                }
                case R.id.rating_star4:{
                    rating_star1.setImageResource(R.drawable.star_filled);
                    rating_star2.setImageResource(R.drawable.star_filled);
                    rating_star3.setImageResource(R.drawable.star_filled);
                    rating_star4.setImageResource(R.drawable.star_filled);
                    rating_star5.setImageResource(R.drawable.star_empty);
                    CustomRatingBar.this.numRating = 4;
                    break;
                }
                case R.id.rating_star5:{
                    rating_star1.setImageResource(R.drawable.star_filled);
                    rating_star2.setImageResource(R.drawable.star_filled);
                    rating_star3.setImageResource(R.drawable.star_filled);
                    rating_star4.setImageResource(R.drawable.star_filled);
                    rating_star5.setImageResource(R.drawable.star_filled);
                    CustomRatingBar.this.numRating = 5;
                    break;
                }
            }
        }
    };
}
