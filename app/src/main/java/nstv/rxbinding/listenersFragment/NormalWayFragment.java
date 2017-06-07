package nstv.rxbinding.listenersFragment;


import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import nstv.rxbinding.R;
import nstv.rxbinding.model.SuperShape;

public class NormalWayFragment extends RxBindingFragment {

    public NormalWayFragment() {
        // Required empty public constructor
    }

    public static NormalWayFragment getInstance(SuperShape superShape) {
        NormalWayFragment fragment = new NormalWayFragment();
        fragment.superShape = superShape;
        return fragment;
    }

    @Override
    protected void setUpListeners() {
        //RGB Background
        redSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress != superShape.getRed()) {
                    superShape.setRed(progress);
                    redTextView.setText(superShape.getRed() + "");
                    updateBackgroundColor();
                }
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        greenSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress != superShape.getGreen()) {
                    superShape.setGreen(progress);
                    greenTextView.setText(superShape.getGreen() + "");
                    updateBackgroundColor();
                }
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        blueSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = progress * 255 / 100;
                if (progress != superShape.getBlue()) {
                    superShape.setBlue(progress);
                    blueTextView.setText(superShape.getBlue() + "");
                    updateBackgroundColor();
                }
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Shape selection
        shapeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioBtn_square) {
                    superShape.setShape(R.drawable.shape_square);
                } else {// Circle
                    superShape.setShape(R.drawable.shape_circle);
                }
                shapeImageView.setImageResource(superShape.getShape());
                updateBackgroundColor();
            }
        });

        //Icon selection
        withLogoCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                superShape.setWithIcon(isChecked);
                showHideIcon();
            }
        });

        iconRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(RadioGroup group, int checkedId) {
                int iconRes = iconBtnToResource.get(checkedId);
                superShape.setIconDrawable(iconRes);
                updateIcon();
            }
        });
    }
}
