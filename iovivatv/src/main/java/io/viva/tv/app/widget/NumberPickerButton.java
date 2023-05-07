 package io.viva.tv.app.widget;
 
 import android.content.Context;
 import android.util.AttributeSet;
 import android.view.KeyEvent;
 import android.view.MotionEvent;
 import android.widget.ImageButton;
 
 class NumberPickerButton extends ImageButton
 {
   private NumberPicker mNumberPicker;
 
   public NumberPickerButton(Context context, AttributeSet attrs, int defStyle)
   {
     super(context, attrs, defStyle);
   }
 
   public NumberPickerButton(Context context, AttributeSet attrs) {
     super(context, attrs);
   }
 
   public NumberPickerButton(Context context) {
     super(context);
   }
 
   public void setNumberPicker(NumberPicker picker) {
     this.mNumberPicker = picker;
   }
 
   public boolean onTouchEvent(MotionEvent event)
   {
     cancelLongpressIfRequired(event);
     return super.onTouchEvent(event);
   }
 
   public boolean onTrackballEvent(MotionEvent event)
   {
     cancelLongpressIfRequired(event);
     return super.onTrackballEvent(event);
   }
 
   public boolean onKeyUp(int keyCode, KeyEvent event)
   {
     if ((keyCode == 23) || (keyCode == 66))
     {
       cancelLongpress();
     }
     return super.onKeyUp(keyCode, event);
   }
 
   private void cancelLongpressIfRequired(MotionEvent event) {
     if ((event.getAction() == 3) || (event.getAction() == 1))
     {
       cancelLongpress();
     }
   }
 
   private void cancelLongpress() {
     if (2114584630 == getId())
       this.mNumberPicker.cancelIncrement();
     else if (2114584632 == getId())
       this.mNumberPicker.cancelDecrement();
   }
 }

