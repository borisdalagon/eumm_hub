package com.ufipay.eummhub.core.activities;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;


import com.appolica.flubber.Flubber;
import com.bumptech.glide.Glide;
import com.ufipay.eummhub.R;
import com.ufipay.eummhub.core.utils.Utils;

import java.util.Locale;

public class Pager extends AppCompatActivity {

    static final String TAG = "PagerActivity";

    SectionsPagerAdapter mSectionsPagerAdapter;
    ImageButton mNextBtn;
    Button mSkipBtn;
    Button mFinishBtn;
    TextView tvTitreClr;

    ImageView zero;
    ImageView one;
    ImageView two;
    ImageView tree;
    ImageView[] indicators;

    int lastLeftValue = 0;


    RelativeLayout mRelativeLayout;
    int page = 0;   //  to track page position

    private ViewPager mViewPager;

    Pager activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN );
            getWindow().setStatusBarColor( ContextCompat.getColor( this, R.color.colorNoir2 ) );
        }

        setContentView( R.layout.activity_pager );

        Window window = Pager.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(Pager.this, R.color.colorEU));

        //Pour le Crash de l'application
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //********

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter( getSupportFragmentManager() );

        mNextBtn = (ImageButton) findViewById( R.id.intro_btn_next );
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
            mNextBtn.setImageDrawable(
                    Utils.tintMyDrawable( ContextCompat.getDrawable( this, R.drawable.ic_chevron_right_24dp ), Color.WHITE )
            );

        mSkipBtn = (Button) findViewById( R.id.intro_btn_skip );
        mFinishBtn = (Button) findViewById( R.id.intro_btn_finish );

        zero = (ImageView) findViewById( R.id.intro_indicator_0 );
        one = (ImageView) findViewById( R.id.intro_indicator_1 );
        two = (ImageView) findViewById( R.id.intro_indicator_2 );
        tree = (ImageView) findViewById( R.id.intro_indicator_3 );

        mRelativeLayout = (RelativeLayout) findViewById( R.id.main_content );


        indicators = new ImageView[]{zero, one, two, tree};

        // Set up the ViewPager with the sections adapter.

        mViewPager = findViewById( R.id.container );

        mViewPager.setAdapter( mSectionsPagerAdapter );


        mViewPager.setCurrentItem( page );
        updateIndicators( page );

        final int color1 = ContextCompat.getColor( this, R.color.colorEU );
        final int color2 = ContextCompat.getColor( this, R.color.colorEU );
        final int color3 = ContextCompat.getColor( this, R.color.colorEU );
        final int color4 = ContextCompat.getColor( this, R.color.colorEU );

        final int[] colorList = new int[]{color1, color2, color3, color4};

        final ArgbEvaluator evaluator = new ArgbEvaluator();

        mViewPager.addOnPageChangeListener( new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                /*
                color update
                 */
                int colorUpdate = (Integer) evaluator.evaluate( positionOffset, colorList[position], colorList[position == 3 ? position : position + 1] );
                mViewPager.setBackgroundColor( colorUpdate );


            }


            @Override
            public void onPageSelected(int position) {

                page = position;

                updateIndicators( page );

                switch (position) {
                    case 0:
                        mViewPager.setBackgroundColor( color1 );
                        break;
                    case 1:
                        mViewPager.setBackgroundColor( color2 );
                        break;
                    case 2:
                        mViewPager.setBackgroundColor( color3 );
                        break;
                    case 3:
                        mViewPager.setBackgroundColor( color4 );
                        break;

                    default:break;
                }


                mNextBtn.setVisibility( position == 3 ? View.GONE : View.VISIBLE );
                mFinishBtn.setVisibility( position == 3 ? View.VISIBLE : View.GONE );


            }

            @Override
            public void onPageScrollStateChanged(int state) {

                Log.d("onPageScrollState","on");

            }
        } );

        mNextBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page += 1;
                mViewPager.setCurrentItem( page, true );
            }
        } );

        mSkipBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        } );

        mFinishBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //  update 1st time pref
                Utils.saveSharedSetting( Pager.this, MainActivity.PREF_USER_FIRST_TIME, "false" );

            }
        } );

    }

    void updateIndicators(int position) {
        for (int i = 0; i < indicators.length; i++) {
            indicators[i].setBackgroundResource(
                    i == position ? R.drawable.indicator_selected : R.drawable.indicator_unselected
            );
        }
    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        int i;


        int[] bgs = new int[]{R.drawable.fondbleu8, R.drawable.fondbleu16, R.drawable.fondbleu10, R.drawable.bgr2};

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt( ARG_SECTION_NUMBER, sectionNumber );
            fragment.setArguments( args );
            return fragment;
        }


        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate( R.layout.fragment_pager, container, false );

            ImageView imgPrincipal = (ImageView) rootView.findViewById( R.id.imgPrincipal );
            LinearLayout liBlocPage1 = (LinearLayout)rootView.findViewById(R.id.li_bloc_page_1);
            LinearLayout liBlocPage2 = (LinearLayout)rootView.findViewById(R.id.li_bloc_page_2);
            LinearLayout liBlocPage3 = (LinearLayout)rootView.findViewById(R.id.li_bloc_page_3);
            RelativeLayout liBlocPage4 = (RelativeLayout)rootView.findViewById(R.id.li_bloc_page_4);

            final CardView cardLogo = (CardView)rootView.findViewById(R.id.card_logo);

            final ImageView nomLogo2 = (ImageView)rootView.findViewById(R.id.nomLogo2);
            final ImageView nomLogo3 = (ImageView)rootView.findViewById(R.id.nomLogo3);
            final ImageView nomLogo4 = (ImageView)rootView.findViewById(R.id.nomLogo4);


            LinearLayout liTitrePage2 = (LinearLayout)rootView.findViewById(R.id.li_titre_page_2);
            LinearLayout liTitrePage3 = (LinearLayout)rootView.findViewById(R.id.li_titre_page_3);

            if(getArguments() != null) {

                i = getArguments().getInt( ARG_SECTION_NUMBER );

                if (i == 1) {

                    Glide.with(this)
                            .load(R.drawable.fondbleu8)
                            .into(imgPrincipal);

                    liBlocPage1.setVisibility(View.VISIBLE);
                    liBlocPage2.setVisibility(View.GONE);
                    liBlocPage3.setVisibility(View.GONE);
                    liBlocPage4.setVisibility(View.GONE);

                    liTitrePage2.setVisibility(View.GONE);
                    liTitrePage3.setVisibility(View.GONE);

                    Flubber.with()
                            .animation(Flubber.AnimationPreset.FADE_IN)
                            .listener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animator) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animator) {


                                }

                                @Override
                                public void onAnimationCancel(Animator animator) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animator) {

                                }
                            })// Slide up animation FADE_IN_UP
                            .repeatCount(0)                              // Repeat once
                            .duration(1000)                              // Last for 1000 milliseconds(1 second)
                            .createFor(cardLogo)                             // Apply it to the view
                            .start();


                }

                if (i == 2) {

                    Glide.with(this)
                            .load(R.drawable.fondbleu16)
                            .into(imgPrincipal);

                    liBlocPage1.setVisibility(View.GONE);
                    liBlocPage2.setVisibility(View.VISIBLE);
                    liBlocPage3.setVisibility(View.GONE);
                    liBlocPage4.setVisibility(View.GONE);

                    liTitrePage2.setVisibility(View.VISIBLE);
                    liTitrePage3.setVisibility(View.GONE);

                    Flubber.with()
                            .animation(Flubber.AnimationPreset.FADE_IN_RIGHT)
                            .listener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animator) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animator) {


                                }

                                @Override
                                public void onAnimationCancel(Animator animator) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animator) {

                                }
                            })// Slide up animation FADE_IN_UP
                            .repeatCount(0)                              // Repeat once
                            .duration(2000)                              // Last for 1000 milliseconds(1 second)
                            .createFor(nomLogo2)                             // Apply it to the view
                            .start();
                }

                if (i == 3) {

                    Glide.with(this)
                            .load(R.drawable.fondbleu10)
                            .into(imgPrincipal);

                    liBlocPage1.setVisibility(View.GONE);
                    liBlocPage2.setVisibility(View.GONE);
                    liBlocPage3.setVisibility(View.VISIBLE);
                    liBlocPage4.setVisibility(View.GONE);

                    liTitrePage2.setVisibility(View.GONE);
                    liTitrePage3.setVisibility(View.VISIBLE);

                    Flubber.with()
                            .animation(Flubber.AnimationPreset.FADE_IN_RIGHT)
                            .listener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animator) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animator) {


                                }

                                @Override
                                public void onAnimationCancel(Animator animator) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animator) {

                                }
                            })// Slide up animation FADE_IN_UP
                            .repeatCount(0)                              // Repeat once
                            .duration(2000)                              // Last for 1000 milliseconds(1 second)
                            .createFor(nomLogo3)                             // Apply it to the view
                            .start();
                }

                if (i == 4) {

                    Glide.with(this)
                            .load(R.drawable.bgr2)
                            .into(imgPrincipal);

                    liBlocPage1.setVisibility(View.GONE);
                    liBlocPage2.setVisibility(View.GONE);
                    liBlocPage3.setVisibility(View.GONE);
                    liBlocPage4.setVisibility(View.VISIBLE);

                    liTitrePage2.setVisibility(View.GONE);
                    liTitrePage3.setVisibility(View.GONE);

                    Flubber.with()
                            .animation(Flubber.AnimationPreset.FADE_IN_RIGHT)
                            .listener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animator) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animator) {


                                }

                                @Override
                                public void onAnimationCancel(Animator animator) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animator) {

                                }
                            })// Slide up animation FADE_IN_UP
                            .repeatCount(0)                              // Repeat once
                            .duration(2000)                              // Last for 1000 milliseconds(1 second)
                            .createFor(nomLogo4)                             // Apply it to the view
                            .start();
                }

            }

            //imgPrincipal.setBackgroundResource( bgs[getArguments().getInt( ARG_SECTION_NUMBER ) - 1] );

            return rootView;
        }


    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {


        private SectionsPagerAdapter(FragmentManager fm) {
            super( fm );
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance( position + 1 );

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
                case 3:
                    return "SECTION 4";

                default:break;
            }
            return null;
        }

    }



    public void setLocale(String lang, Context context){

        Locale locale = new Locale(lang);
        locale.setDefault(locale);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Configuration config = new Configuration(context.getResources().getConfiguration());
            config.setLocale(locale);

            context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        }else {
            Configuration config = new Configuration();
            config.locale = locale;

            context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        }



       // SharedPrefManager.newInstance(Pager.this).setLangue(lang);
    }



}