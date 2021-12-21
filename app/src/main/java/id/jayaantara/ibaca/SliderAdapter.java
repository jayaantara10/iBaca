package id.jayaantara.ibaca;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class SliderAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater inflater;

    public SliderAdapter(Context context){
        this.context = context;
    }

    public int[]  slide_images = {
            R.drawable.gambar_1,
            R.drawable.gambar_2,
            R.drawable.gambar_3

    };

    public String[] slide_headings = {
            "Sharing material",
            "Self-study",
            "Easy Tasks"
    };

    public String[] slide_desc = {
            "Let's share material with friends around you online without needing to bother with iBaca",
            "Make it easy to learn with material that other users share in the form of articles or reading materials on the iBaca application",
            "Make the task easier and faster because of the material stay in iBaca"
    };

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (RelativeLayout) object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position){
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.slide_layout, container,false);

        ImageView slide_image_view = (ImageView) view.findViewById(R.id.slide_image);
        TextView slide_heading_view = (TextView) view.findViewById(R.id.slide_heading);
        TextView slide_desc_view = (TextView) view.findViewById(R.id.slide_desc);

        slide_image_view.setImageResource(slide_images[position]);
        slide_heading_view.setText(slide_headings[position]);
        slide_desc_view.setText(slide_desc[position]);
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
