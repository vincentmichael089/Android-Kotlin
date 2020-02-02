/*
 * Copyright (C) 2019 The Android Open Source Project
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

package com.google.samples.propertyanimation

import android.animation.*
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Property
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView


class MainActivity : AppCompatActivity() {

    lateinit var star: ImageView
    lateinit var rotateButton: Button
    lateinit var translateButton: Button
    lateinit var scaleButton: Button
    lateinit var fadeButton: Button
    lateinit var colorizeButton: Button
    lateinit var showerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        star = findViewById(R.id.star)
        rotateButton = findViewById<Button>(R.id.rotateButton)
        translateButton = findViewById<Button>(R.id.translateButton)
        scaleButton = findViewById<Button>(R.id.scaleButton)
        fadeButton = findViewById<Button>(R.id.fadeButton)
        colorizeButton = findViewById<Button>(R.id.colorizeButton)
        showerButton = findViewById<Button>(R.id.showerButton)

        rotateButton.setOnClickListener {
            rotater()
        }

        translateButton.setOnClickListener {
            translater()
        }

        scaleButton.setOnClickListener {
            scaler()
        }

        fadeButton.setOnClickListener {
            fader()
        }

        colorizeButton.setOnClickListener {
            colorizer()
        }

        showerButton.setOnClickListener {
            shower()
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> touchStart()
            MotionEvent.ACTION_MOVE -> touchMove()
            MotionEvent.ACTION_UP -> touchUp()
        }

        return true
    }

    private fun touchStart() {}
    private fun touchMove() {
        shower()
    }
    private fun touchUp() {}

    private fun rotater() {
        val animator = ObjectAnimator.ofFloat(star, View.ROTATION, -360F, 0F)
        animator.duration = 1000
        animator.disableViewDuringAnimation(rotateButton)
        animator.start()
    }

    //Note: The reason that the animation starts at -360 is that that allows the star to complete a
    // full circle (360 degrees) and end at 0, which is the default rotation value for a non-rotated
    // view, so it’s a good value to have at the end of the animation (in case any other action occurs
    // on that view later, expecting the default value).

    //Note: A property, to the animation system, is a field that is exposed via setters and getters,
    // either implicitly (as properties are in Kotlin) or explicitly (via the setter/getter pattern
    // in the Java programming language). There are also a special case of properties exposed via the
    // class android.util.Property which is used by the View class, which allows a type-safe approach
    // for animations, as we’ll see later. The Animator system in Android was specifically written to
    // animate properties, meaning that it can animate anything (not just UI elements) that has a setter
    // (and, in some cases, a getter)
    //
    //Note: View properties are a set of functionality added to the base View class that allows all
    // views to be transformed in specific ways that are useful in UI animations. There are properties
    // for position (called “translation”), rotation, scale, and transparency (called “alpha”).
    //
    //There are actually two different ways to access the properties, by regular setter/getter pairs,
    // like setTranslateX()/getTranslateX(), and by static android.util.Property objects, like
    // View.TRANSLATE_X (an object that has both a get() and a set() method). This lesson primarily
    // uses android.util.Propertyobjects, because it has less overhead internally along with better
    // type-safety, but you can also use the setters and getters of any object, as you will see toward
    // the end of the lab.
    //
    //Note: Property animation is, simply, the changing of property values over time. ObjectAnimator
    // was created to provide a simple set-and-forget mechanism for animating properties. For example,
    // you can define an animator that changes the “alpha” property of a View, which will alter the
    // transparency of that view on the screen.
    //


    private fun translater() {
        val animator = ObjectAnimator.ofFloat(star, View.TRANSLATION_X, 200f)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableViewDuringAnimation(translateButton)
        animator.start()
    }

    private fun scaler() {
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 4F)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 4F)

        val animator = ObjectAnimator.ofPropertyValuesHolder(star, scaleX, scaleY)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableViewDuringAnimation(scaleButton)
        animator.start()
    }

    private fun fader() {
        val animator = ObjectAnimator.ofFloat(star, View.ALPHA, 0f)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableViewDuringAnimation(fadeButton)
        animator.start()
    }

    private fun colorizer() {
        var animator = ObjectAnimator.ofArgb(star.parent,
            "backgroundColor", Color.BLACK, Color.RED)
        animator.setDuration(500)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableViewDuringAnimation(colorizeButton)
        animator.start()
    }

    private fun shower() {
        val container = star.parent as ViewGroup
        val containerW = container.width
        val containerH = container.height
        var starW: Float = star.width.toFloat()
        var starH: Float = star.height.toFloat()

        val newStar = AppCompatImageView(this)
        newStar.setImageResource(R.drawable.ic_star)
        newStar.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT)
        container.addView(newStar)

        // randomize size of star
        newStar.scaleX = Math.random().toFloat() * 1.0f + .1f
        newStar.scaleY = newStar.scaleX
        starW *= newStar.scaleX
        starH *= newStar.scaleY

        // randomize position of star
        // This code uses the width of the star to position it from half-way off the screen on the
        // left (-starW / 2) to half-way off the screen on the right (with the star positioned at
        // (containerW - starW / 2). The vertical positioning of the star will be handled later in
        // the actual animation code.
        newStar.translationX = Math.random().toFloat() *
                containerW - starW / 2

        // down movement
        val mover = ObjectAnimator.ofFloat(newStar, View.TRANSLATION_Y,
            -starH, containerH + starH) // containerH + starH means start above the container so star cant be seen on beginning
        mover.interpolator = AccelerateInterpolator(1f)

        // rotate movement
        val rotator = ObjectAnimator.ofFloat(newStar, View.ROTATION,
            (Math.random() * 1080).toFloat())
        rotator.interpolator = LinearInterpolator()

        // group movement
        val set = AnimatorSet()
        set.playTogether(mover, rotator)
        set.duration = (Math.random() * 1500 + 500).toLong()

        // clean view
        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                container.removeView(newStar)
            }
        })
        set.start()
    }

    // Property Function
    private fun ObjectAnimator.disableViewDuringAnimation(view: View) {
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                view.isEnabled = false
            }

            override fun onAnimationEnd(animation: Animator?) {
                view.isEnabled = true
            }
        })
    }

}
