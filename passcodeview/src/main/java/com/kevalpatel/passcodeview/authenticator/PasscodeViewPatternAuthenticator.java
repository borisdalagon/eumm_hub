/*
 * Copyright 2018 Keval Patel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kevalpatel.passcodeview.authenticator;


import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

import com.kevalpatel.passcodeview.patternCells.PatternPoint;

import java.util.ArrayList;

/**
 * Created by Keval on 14/04/18.
 *
 * @author <a href="https://github.com/kevalpatel2106">kevalpatel2106</a>
 * @see <a href="https://github.com/kevalpatel2106/PasscodeView/wiki/Authenticator">Authenticator</a>
 */
public final class PasscodeViewPatternAuthenticator implements PatternAuthenticator {

    @NonNull
    private final PatternPoint[] mCorrectPattern;

    public PasscodeViewPatternAuthenticator(@NonNull final PatternPoint[] correctPattern) {
        mCorrectPattern = correctPattern;
    }

    @WorkerThread
    @Override
    public boolean isValidPattern(@NonNull final ArrayList<PatternPoint> patternPoints) {
        //This calculations won't take much time.
        //We are not blocking the UI.
        for (int i = 0; i < mCorrectPattern.length; i++)
            if (!mCorrectPattern[i].equals(patternPoints.get(i))) return false;

        return mCorrectPattern.length == patternPoints.size();
    }
}
