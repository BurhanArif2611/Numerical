/*
 * Copyright (C) 2014 Socratica LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.revauc.revolutionbuy.util.typeface;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Chronometer;

/**
 * Regular {@link Chronometer} that just allows usage of 'customTypeface' attribute to specify
 * font for text rendering. See {@link TypefaceInitializer} for details.
 *
 * @author Konstantin Burov (aectann@gmail.com)
 */
public class TypefaceChronometer extends Chronometer implements TypefaceView {

    public TypefaceChronometer(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypefaceInitializer.init(this, context, attrs);
    }

}
