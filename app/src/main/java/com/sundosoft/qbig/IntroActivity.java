package com.sundosoft.qbig;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

public class IntroActivity extends AppIntro {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SliderPage sliderPage1 = new SliderPage();
        sliderPage1.setTitle("모든 자격증 기출문제가\n한 곳에!");
        sliderPage1.setDescription("프린트로 뽑아서 푸는 귀찮은 공부는 이제 안녕~");
        //sliderPage1.setImageDrawable(image);
        sliderPage1.setTitleColor(Color.parseColor("#333333"));
        sliderPage1.setDescColor(Color.parseColor("#999999"));
        sliderPage1.setBgColor(Color.parseColor("#FFFFFF"));
        addSlide(AppIntroFragment.newInstance(sliderPage1));

        SliderPage sliderPage2 = new SliderPage();
        sliderPage2.setTitle("title");
        sliderPage2.setDescription("description");
        //sliderPage2.setImageDrawable(image);
        sliderPage2.setTitleColor(Color.parseColor("#333333"));
        sliderPage2.setDescColor(Color.parseColor("#999999"));
        sliderPage2.setBgColor(Color.parseColor("#FFFFFF"));
        addSlide(AppIntroFragment.newInstance(sliderPage2));

        SliderPage sliderPage3 = new SliderPage();
        sliderPage3.setTitle("title");
        sliderPage3.setDescription("description");
        //sliderPage3.setImageDrawable(image);
        sliderPage3.setTitleColor(Color.parseColor("#333333"));
        sliderPage3.setDescColor(Color.parseColor("#999999"));
        sliderPage3.setBgColor(Color.parseColor("#FFFFFF"));
        addSlide(AppIntroFragment.newInstance(sliderPage3));

        SliderPage sliderPage4 = new SliderPage();
        sliderPage4.setTitle("title");
        sliderPage4.setDescription("description");
        //sliderPage3.setImageDrawable(image);
        sliderPage4.setTitleColor(Color.parseColor("#333333"));
        sliderPage4.setDescColor(Color.parseColor("#999999"));
        sliderPage4.setBgColor(Color.parseColor("#FFFFFF"));
        addSlide(AppIntroFragment.newInstance(sliderPage3));

        showSkipButton(false);
        setColorDoneText(Color.parseColor("#0000AA"));
        setNextArrowColor(Color.parseColor("#0000AA"));
        setIndicatorColor(Color.parseColor("#0000FF"),Color.parseColor("#0000AA"));
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        //startActivity(new Intent(getApplication(), LoginActivity.class));
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }

}
