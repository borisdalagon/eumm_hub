package com.ufipay.eummhub.core.classe;

import android.content.Context;

import com.kevalpatel.passcodeview.PinView;
import com.kevalpatel.passcodeview.authenticator.PasscodeViewPinAuthenticator;
import com.kevalpatel.passcodeview.indicators.CircleIndicator;
import com.kevalpatel.passcodeview.keys.KeyNamesBuilder;
import com.kevalpatel.passcodeview.keys.RoundKey;
import com.ufipay.eummhub.R;

public class CustomerMpin {



    public void correctMpin(PinView mPinView, int[] correctPattern) {

        mPinView.setPinAuthenticator(new PasscodeViewPinAuthenticator(correctPattern));

    }

    public void keyMethod(PinView mPinView){
        //Build the desired key shape and pass the theme parameters.
        //REQUIRED
        mPinView.setKey(new RoundKey.Builder(mPinView)
                .setKeyPadding(R.dimen.key_padding)
                .setKeyStrokeColorResource(R.color.colorEU)
                .setKeyStrokeWidth(R.dimen.key_stroke_width)
                .setKeyTextColorResource(R.color.colorEU)
                .setKeyTextSize(R.dimen.txt_titre));

    }

    public void indicatorMethod(PinView mPinView){
        //Build the desired indicator shape and pass the theme attributes.
        //REQUIRED
        mPinView.setIndicator(new CircleIndicator.Builder(mPinView)
                .setIndicatorRadius(R.dimen.indicator_radius)
                .setIndicatorFilledColorResource(R.color.colorEU)
                .setIndicatorStrokeColorResource(R.color.colorEU)
                .setIndicatorStrokeWidth(R.dimen.indicator_stroke_width));

        mPinView.setPinLength(PinView.DYNAMIC_PIN_LENGTH);
    }

    public void keyBoard(PinView mPinView, Context context, int value){
        //Set the name of the keys based on your locale.
        //OPTIONAL. If not passed key names will be displayed based on english locale.

        value = value % 9;

        //keyBoard_1(mPinView,context);

        if(value == 0){
            keyBoard_0(mPinView,context);
        }
        else if(value == 1){
            keyBoard_1(mPinView,context);
        }
        else if(value == 2){
            keyBoard_2(mPinView,context);
        }
        else if(value == 3){
            keyBoard_3(mPinView,context);
        }
        else if(value == 4){
            keyBoard_4(mPinView,context);
        }
        else if(value == 5){
            keyBoard_5(mPinView,context);
        }
        else if(value == 6){
            keyBoard_6(mPinView,context);
        }
        else if(value == 7){
            keyBoard_7(mPinView,context);
        }
        else if(value == 8){
            keyBoard_8(mPinView,context);
        }



        mPinView.setTitle("MPIN Code");
    }

    private void keyBoard_0(PinView mPinView, Context context){

        mPinView.setKeyNames(new KeyNamesBuilder()
                .setKeyFour(context, R.string.key_4)
                .setKeyThree(context, R.string.key_3)
                .setKeyTwo(context, R.string.key_2)
                .setKeyNine(context, R.string.key_9)
                .setKeyEight(context, R.string.key_8)
                .setKeySeven(context, R.string.key_7)
                .setKeyZero(context, R.string.key_0)
                .setKeyFive(context, R.string.key_5)
                .setKeyOne(context, R.string.key_1)
                .setKeySix(context, R.string.key_6));

    }

    private void keyBoard_1(PinView mPinView, Context context){

        mPinView.setKeyNames(new KeyNamesBuilder()
                .setKeyOne(context, R.string.key_1)
                .setKeyTwo(context, R.string.key_2)
                .setKeyThree(context, R.string.key_3)
                .setKeyFour(context, R.string.key_4)
                .setKeyFive(context, R.string.key_5)
                .setKeySix(context, R.string.key_6)
                .setKeySeven(context, R.string.key_7)
                .setKeyEight(context, R.string.key_8)
                .setKeyNine(context, R.string.key_9)
                .setKeyZero(context, R.string.key_0));


    }

    private void keyBoard_2(PinView mPinView, Context context){

        mPinView.setKeyNames(new KeyNamesBuilder()
                .setKeyThree(context, R.string.key_3)
                .setKeyFour(context, R.string.key_4)
                .setKeyOne(context, R.string.key_1)
                .setKeyTwo(context, R.string.key_2)
                .setKeySeven(context, R.string.key_7)
                .setKeyEight(context, R.string.key_8)
                .setKeyFive(context, R.string.key_5)
                .setKeySix(context, R.string.key_6)
                .setKeyZero(context, R.string.key_0)
                .setKeyNine(context, R.string.key_9));


    }

    private void keyBoard_3(PinView mPinView, Context context){

        mPinView.setKeyNames(new KeyNamesBuilder()
                .setKeyFour(context, R.string.key_4)
                .setKeyThree(context, R.string.key_3)
                .setKeyTwo(context, R.string.key_2)
                .setKeyOne(context, R.string.key_1)
                .setKeyEight(context, R.string.key_8)
                .setKeySeven(context, R.string.key_7)
                .setKeySix(context, R.string.key_6)
                .setKeyFive(context, R.string.key_5)
                .setKeyNine(context, R.string.key_9)
                .setKeyZero(context, R.string.key_0));

    }

    private void keyBoard_4(PinView mPinView, Context context){

        mPinView.setKeyNames(new KeyNamesBuilder()
                .setKeyNine(context, R.string.key_9)
                .setKeyZero(context, R.string.key_0)
                .setKeySeven(context, R.string.key_7)
                .setKeyEight(context, R.string.key_8)
                .setKeyFive(context, R.string.key_5)
                .setKeySix(context, R.string.key_6)
                .setKeyThree(context, R.string.key_3)
                .setKeyFour(context, R.string.key_4)
                .setKeyTwo(context, R.string.key_2)
                .setKeyOne(context, R.string.key_1));

    }

    private void keyBoard_5(PinView mPinView, Context context){

        mPinView.setKeyNames(new KeyNamesBuilder()
                .setKeySeven(context, R.string.key_7)
                .setKeyEight(context, R.string.key_8)
                .setKeyNine(context, R.string.key_9)
                .setKeyZero(context, R.string.key_0)
                .setKeyThree(context, R.string.key_3)
                .setKeySix(context, R.string.key_6)
                .setKeyFive(context, R.string.key_5)
                .setKeyOne(context, R.string.key_1)
                .setKeyTwo(context, R.string.key_2)
                .setKeyFour(context, R.string.key_4));

    }

    private void keyBoard_6(PinView mPinView, Context context){

        mPinView.setKeyNames(new KeyNamesBuilder()
                .setKeyZero(context, R.string.key_0)
                .setKeyNine(context, R.string.key_9)
                .setKeyFive(context, R.string.key_5)
                .setKeySix(context, R.string.key_6)
                .setKeyEight(context, R.string.key_8)
                .setKeySeven(context, R.string.key_7)
                .setKeyOne(context, R.string.key_1)
                .setKeyTwo(context, R.string.key_2)
                .setKeyThree(context, R.string.key_3)
                .setKeyFour(context, R.string.key_4));


    }

    private void keyBoard_7(PinView mPinView, Context context){

        mPinView.setKeyNames(new KeyNamesBuilder()
                .setKeyFive(context, R.string.key_5)
                .setKeyTwo(context, R.string.key_2)
                .setKeyZero(context, R.string.key_0)
                .setKeySix(context, R.string.key_6)
                .setKeyFour(context, R.string.key_4)
                .setKeySeven(context, R.string.key_7)
                .setKeyOne(context, R.string.key_1)
                .setKeyNine(context, R.string.key_9)
                .setKeyThree(context, R.string.key_3)
                .setKeyEight(context, R.string.key_8));

    }

    private void keyBoard_8(PinView mPinView, Context context){

        mPinView.setKeyNames(new KeyNamesBuilder()
                .setKeyEight(context, R.string.key_8)
                .setKeyThree(context, R.string.key_3)
                .setKeyFour(context, R.string.key_4)
                .setKeySix(context, R.string.key_6)
                .setKeyZero(context, R.string.key_0)
                .setKeySeven(context, R.string.key_7)
                .setKeyOne(context, R.string.key_1)
                .setKeyTwo(context, R.string.key_2)
                .setKeyNine(context, R.string.key_9)
                .setKeyFive(context, R.string.key_5));

    }




}
