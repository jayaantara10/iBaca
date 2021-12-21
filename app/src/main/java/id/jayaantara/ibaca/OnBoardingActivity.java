package id.jayaantara.ibaca;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OnBoardingActivity extends AppCompatActivity {

    private ViewPager view_pager_slide;
    private LinearLayout dots_layout;
    private TextView[] dots;
    private Button btn_mulai;

    private SliderAdapter sliderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        view_pager_slide = (ViewPager) findViewById(R.id.slide_view_pager);
        dots_layout = (LinearLayout) findViewById(R.id.dots_layout);
        btn_mulai = findViewById(R.id.btn_mulai);

        sliderAdapter = new SliderAdapter(this);

        view_pager_slide.setAdapter(sliderAdapter);

        addDotsIndicator(0);

        view_pager_slide.addOnPageChangeListener(listener);

        btn_mulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    public void addDotsIndicator(int position){
        dots = new TextView[3];
        dots_layout.removeAllViews();

        for(int i = 0; i< dots.length; i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#9900;"));
            dots[i].setTextSize(15);
            dots[i].setTextColor(getResources().getColor(R.color.cloud_blue));

            dots_layout.addView(dots[i]);

        }

        if(dots.length > 0){
            dots[position].setText(Html.fromHtml("&#11044;"));
        }

        if(position == 2 ){
            btn_mulai.setVisibility(View.VISIBLE);
        }else{
            btn_mulai.setVisibility(View.GONE);
        }
    }

    ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener(){

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}