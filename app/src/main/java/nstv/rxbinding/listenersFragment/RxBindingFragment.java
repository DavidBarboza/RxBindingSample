package nstv.rxbinding.listenersFragment;


import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxCompoundButton;
import com.jakewharton.rxbinding2.widget.RxRadioGroup;
import com.jakewharton.rxbinding2.widget.RxSeekBar;

import nstv.rxbinding.R;
import nstv.rxbinding.model.SuperShape;

public class RxBindingFragment extends Fragment {

    protected SuperShape superShape;

    //UI ELEMENTS
    ImageView shapeImageView;
    //RGB Background
    TextView redTextView;
    TextView greenTextView;
    TextView blueTextView;
    SeekBar redSeekBar;
    SeekBar greenSeekBar;
    SeekBar blueSeekBar;
    //Shape selection
    RadioGroup shapeRadioGroup;
    //WithLogo
    ImageView iconImageView;
    CheckBox withLogoCheck;
    RadioGroup iconRadioGroup;

    ArrayMap<Integer, Integer> iconBtnToResource;

    public RxBindingFragment() {
        // Required empty public constructor
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iconBtnToResource = new ArrayMap<>();
        iconBtnToResource.put(R.id.radioBtn_android, R.drawable.ic_android);
        iconBtnToResource.put(R.id.radioBtn_bubble, R.drawable.ic_bubble);
        iconBtnToResource.put(R.id.radioBtn_cloud, R.drawable.ic_cloud);
        iconBtnToResource.put(R.id.radioBtn_heart, R.drawable.ic_heart);
        iconBtnToResource.put(R.id.radioBtn_star, R.drawable.ic_star);

    }

    public static RxBindingFragment getInstance(SuperShape superShape) {
        RxBindingFragment fragment = new RxBindingFragment();
        fragment.superShape = superShape;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_listeners_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    protected void init() {
        if (superShape == null) {
            superShape = new SuperShape();
        }
        initUIElements();
        setUpListeners();
    }

    protected void initUIElements() {
        shapeImageView = (ImageView) getView().findViewById(R.id.imageView_shape);
        //RGB Background
        redTextView = (TextView) getView().findViewById(R.id.text_red);
        redTextView.setText(superShape.getRed() + "");
        greenTextView = (TextView) getView().findViewById(R.id.text_green);
        greenTextView.setText(superShape.getGreen() + "");
        blueTextView = (TextView) getView().findViewById(R.id.text_blue);
        blueTextView.setText(superShape.getBlue() + "");
        redSeekBar = (SeekBar) getView().findViewById(R.id.seekBar_red);
        redSeekBar.setProgress(superShape.getRed());
        greenSeekBar = (SeekBar) getView().findViewById(R.id.seekBar_green);
        greenSeekBar.setProgress(superShape.getGreen());
        blueSeekBar = (SeekBar) getView().findViewById(R.id.seekBar_blue);
        blueSeekBar.setProgress(superShape.getBlue() * 100 / 255);
        //Shape selection
        shapeRadioGroup = (RadioGroup) getView().findViewById(R.id.radioGrp_shape);
        shapeRadioGroup.check(superShape.getShape() == R.drawable.shape_square ? R.id.radioBtn_square : R.id.radioBtn_circle);
        shapeImageView.setImageResource(superShape.getShape());
        //WithLogo
        iconImageView = (ImageView) getView().findViewById(R.id.imageView_icon);
        withLogoCheck = (CheckBox) getView().findViewById(R.id.check_withIcon);
        withLogoCheck.setChecked(superShape.getWithIcon());
        iconRadioGroup = (RadioGroup) getView().findViewById(R.id.radioGrp_icon);
        iconRadioGroup.check(getBtnIdForIcon(superShape.getIconDrawable()));
        showHideIcon();

        updateBackgroundColor();
    }

    protected void setUpListeners() {
        //RGB Background
        RxSeekBar.changes(redSeekBar)
                .filter(progress -> progress != superShape.getRed())
                .subscribe(progress -> {
                    superShape.setRed(progress);
                    redTextView.setText(superShape.getRed() + "");
                    updateBackgroundColor();
                });

        RxSeekBar.changes(greenSeekBar)
                .filter(progress -> progress != superShape.getGreen())
                .subscribe(progress -> {
                    superShape.setGreen(progress);
                    greenTextView.setText(superShape.getGreen() + "");
                    updateBackgroundColor();
                });

        RxSeekBar.changes(blueSeekBar)
                .map(progress -> progress * 255 / 100)
                .filter(progress -> progress != superShape.getBlue())
                .subscribe(progress -> {
                    superShape.setBlue(progress);
                    blueTextView.setText(superShape.getBlue() + "");
                    updateBackgroundColor();
                });

        //Shape selection
        RxRadioGroup.checkedChanges(shapeRadioGroup)
                .subscribe(btnId -> {
                    if (btnId == R.id.radioBtn_square) {
                        superShape.setShape(R.drawable.shape_square);
                    } else {// Circle
                        superShape.setShape(R.drawable.shape_circle);
                    }
                    shapeImageView.setImageResource(superShape.getShape());
                    updateBackgroundColor();
                });

        //Icon selection
        RxCompoundButton.checkedChanges(withLogoCheck)
                .subscribe(checked -> {
                    superShape.setWithIcon(checked);
                    showHideIcon();
                });


        RxRadioGroup.checkedChanges(iconRadioGroup)
                .map(btnId -> iconBtnToResource.get(btnId))
                .subscribe(iconRes -> {
                    superShape.setIconDrawable(iconRes);
                    updateIcon();
                });
    }

    //UI operations
    protected void updateBackgroundColor() {
        if (shapeImageView == null) {
            return;
        }
        ((GradientDrawable) shapeImageView.getDrawable()).setColor(superShape.getBackgroundColor());
        updateIconTint();
    }

    protected void updateIconTint() {
        iconImageView.setColorFilter(getContrastColor(superShape.getRed(), superShape.getGreen(), superShape.getBlue()));
    }

    protected void showHideIcon() {
        iconRadioGroup.setVisibility(superShape.getWithIcon() ? View.VISIBLE : View.INVISIBLE);
        iconImageView.setVisibility(superShape.getWithIcon() ? View.VISIBLE : View.INVISIBLE);

    }

    protected void updateIcon() {
        iconImageView.setImageDrawable(getActivity().getDrawable(superShape.getIconDrawable()));
    }

    // Util methods
    public int getBtnIdForIcon(int iconRes) {
        for (Integer key : iconBtnToResource.keySet()) {
            if (iconBtnToResource.get(key).equals(iconRes)) {
                return key;
            }
        }
        return R.id.radioBtn_android;
    }

    public static int getContrastColor(int red, int green, int blue) {
        int min = Math.min(Math.min(red, green), blue);
        int max = Math.max(Math.max(red, green), blue);
        int minMax = min + max;

        int newColor = Color.rgb(minMax - red, minMax - green, minMax - blue);

        if (newColor == Color.WHITE) {
            return Color.BLACK;
        } else if (newColor == Color.BLACK) {
            return Color.WHITE;
        }

        //Adjust color for better visibility
        float[] hsvColor = new float[3];
        Color.colorToHSV(newColor, hsvColor);
        if (hsvColor[2] < 0.5) {
            hsvColor[2] = (hsvColor[2] + 0.5f) % 1.0f;
        }

        return Color.HSVToColor(Color.alpha(newColor), hsvColor);

    }
}
