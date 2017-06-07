package nstv.rxbinding.listenersFragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxCompoundButton;
import com.jakewharton.rxbinding2.widget.RxProgressBar;
import com.jakewharton.rxbinding2.widget.RxRadioGroup;
import com.jakewharton.rxbinding2.widget.RxSeekBar;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import nstv.rxbinding.R;
import nstv.rxbinding.model.SuperShape;

public class RxBindingFragment extends Fragment {
    protected static final String SHARED_PREFS = "sharedPReferences";
    protected static final String SUPER_SHAPE_KEY = "superShapeKey";

    protected SuperShape superShape;

    //UI ELEMENTS
    EditText nameEditText;
    ImageView shapeImageView;
    ProgressBar progressBar;
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
    TextView iconText;
    ImageView iconImageView;
    CheckBox withLogoCheck;
    RadioGroup iconRadioGroup;
    //SaveShape
    Button saveBtn;

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

    public static RxBindingFragment getInstance(Context context) {
        RxBindingFragment fragment = new RxBindingFragment();
        fragment.superShape = getShape(context);
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
        nameEditText = (EditText) getView().findViewById(R.id.editText_name);
        nameEditText.setText(superShape.getName());
        shapeImageView = (ImageView) getView().findViewById(R.id.imageView_shape);
        progressBar = (ProgressBar) getView().findViewById(R.id.progressBar);
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
        iconText = (TextView) getView().findViewById(R.id.text_icon);
        iconImageView = (ImageView) getView().findViewById(R.id.imageView_icon);
        withLogoCheck = (CheckBox) getView().findViewById(R.id.check_withIcon);
        withLogoCheck.setChecked(superShape.getWithIcon());
        iconRadioGroup = (RadioGroup) getView().findViewById(R.id.radioGrp_icon);
        iconRadioGroup.check(getBtnIdForIcon(superShape.getIconDrawable()));
        showHideIcon();
        //SaveShape
        saveBtn = (Button) getView().findViewById(R.id.btn_save);

        updateBackgroundColor();
    }

    protected void setUpListeners() {
        //Shape name
        RxTextView.textChanges(nameEditText)
                .map(name -> name.toString())
                .subscribe(name -> superShape.setName(name));

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

        Observable<Integer> iconObservable = RxRadioGroup.checkedChanges(iconRadioGroup)
                .map(btnId -> iconBtnToResource.get(btnId))
                .share();


        Disposable changeIconDisposabe = iconObservable
                .subscribe(iconRes -> {
                    superShape.setIconDrawable(iconRes);
                    updateIcon();
                });

        Disposable updateIconText = iconObservable
                .map(resourceId -> getActivity().getResources().getResourceName(resourceId))
                .map(resourceName -> "Icon " + resourceName)
                .subscribe(label -> iconText.setText(label));


        //Save shape
        RxView.clicks(saveBtn)
                .flatMap(Void -> {
                    showProgressBar(true);
                    return Observable.just(saveShape());
                })
                .delay(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Boolean -> {
                    showProgressBar(false);
                    Toast.makeText(getActivity(), "Shape saved", Toast.LENGTH_SHORT).show();
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

    //Save shape
    public boolean saveShape() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, 0);
        sharedPreferences.edit().putString(SUPER_SHAPE_KEY,
                new Gson().toJson(superShape,
                        new TypeToken<SuperShape>() {
                        }.getType())).apply();
        return true;
    }

    public static SuperShape getShape(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, 0);
        String shapeString = sharedPreferences.getString(SUPER_SHAPE_KEY, null);
        if (shapeString == null) {
            return new SuperShape();
        }
        return new Gson().fromJson(shapeString,
                new TypeToken<SuperShape>() {
                }.getType());
    }

    public void showProgressBar(boolean showProgressBar) {
        if (showProgressBar) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
