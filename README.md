# Android StateViewPager 
Implementation of the compatibility library ViewPager with customizable Indicators that supports paging both vertically and horizontally.

<img src=https://user-images.githubusercontent.com/3678050/58378550-5b031600-7f9e-11e9-962e-4eca5dc0de33.png width="500" height="75">
<img src=https://user-images.githubusercontent.com/3678050/58378461-e11e5d00-7f9c-11e9-8bb5-899f243962d1.png width="350" height="35">
<img src=https://user-images.githubusercontent.com/3678050/58378535-efb94400-7f9d-11e9-8de0-4dc43cf9352f.png width="250" height="32">



<img src=https://user-images.githubusercontent.com/3678050/58378217-145eed00-7f99-11e9-84be-9ec84ce91a54.gif width="265" height="480">

## **how to use**

Add it in your root build.gradle at the end of repositories:
```
        repositories {
	    ...
	    maven { url "https://jitpack.io" }
         }	
```
Add the dependency
```
        dependencies {
            ...
            implementation 'com.android.support:recyclerview-v7:28.0.0'
            implementation 'com.google.guava:guava:27.0.1-android'
            implementation 'com.github.YablokovDmitry:StateViewPager:v1.1'
        }
```
### **use StepViewPager**
in code 
```
        StateViewPager mPager = findViewById(R.id.pager);
        mPager.setAdapter(new TestAdapter(getSupportFragmentManager()));
        
        // Setting options
        mPager.setNumberOfIcons(4)
                .setOrientation(LinearLayout.VERTICAL)
                .setMargins(10, 10, 10, 10)
                .setGravity(Gravity.LEFT)
                .setIconSize(26, 26)
                .setSelectedIconSize(25, 25)
                .setOnIconClickListener(new StateViewPager.OnIconClickListener() {
                    @Override
                    public void onIconClick(int iconNum) {
                        mPager.setPage(iconNum, true);
                    }
                })
                .setMarginBetweenIcons(0)
                .setIntermediateIconSize(3, 130)
                .setShowCheckmarks(false, false, false)
                .setShowNumbers(false, false, false)
                .setNumberColors(Color.GRAY, Color.WHITE, Color.GRAY)
                .setBorderColors(Color.parseColor("#D6DBDF"), Color.parseColor("#f0f0f0"), Color.parseColor("#D6DBDF"))
                .setGradientColors(false, false, false)
                .setTitles(new String[]{
                        "Shipping",
                        "Payment",
                        "Confirm",
                        "Done!"})
                .setIconColors(Color.parseColor("#0288D1"), Color.parseColor("#0288D1"), Color.parseColor("#f0f0f0"))
                .setRectangularIcons(false, false, false)
                .setBorderSizes(0, 7, 0)
                .setTextColors(Color.parseColor("#909090"), Color.parseColor("#0288D1"), Color.GRAY)
                .setTextStyles(Typeface.BOLD, Typeface.BOLD, Typeface.BOLD)
                .setTextGravities(Gravity.RIGHT, Gravity.RIGHT, Gravity.RIGHT)
                .setTextSizes(12, 12, 12)
                .setTextMargins(5, 5, 5)
                .setIntermediateIconColors(Color.parseColor("#0288D1"), Color.parseColor("#f0f0f0"))
                .setIntermediateIconStyles("solid", "solid");
        
        mPager.setCurrentItem(0);
      
```
in xml 
```
<com.ydn.viewpagerwithicons.StateViewPager
        android:id="@+id/pager"
        android:layout_width="match_parent"
        android:layout_height="400dp"
        android:orientation="horizontal"/>
```        
Setting options in layout are also possible
```
<com.ydn.viewpagerwithicons.StateViewPager
        android:id="@+id/pager"
        android:layout_width="match_parent"
        android:layout_height="300dp"
        android:entries="@array/titles"
        android:orientation="horizontal"

        app:topMargin="10dp"
        app:bottomMargin="10dp"
        app:leftMargin="10dp"
        app:rightMargin="10dp"
        
        app:iconHeight="30dp"
        app:iconWidth="30dp"

        app:iconsGravity="left"
        app:intermediateHeight="2dp"
        app:intermediateWidth="90dp"
        app:marginBetweenIcons="30dp"
        app:numOfIcons="3"
        app:rectangularSelectedIcon="true"

        app:selectedBorderColor="#FFA233"
        app:selectedBorderWidth="15dp"
        app:selectedColor="#e0e0e0"
        app:selectedHeight="30dp"
        app:selectedTextColor="#FFA233"
        app:selectedTextGravity="right"
        app:selectedTextMargin="10dp"
        app:selectedTextSize="14sp"
        app:selectedTextStyle="bold"
        app:selectedWidth="30dp"
        app:showVisitedCheckmark="true"

        app:unvisitedBorderColor="#909090"
        app:unvisitedBorderWidth="2dp"
        app:unvisitedColor="#e0e0e0"
        app:unvisitedIntermediateColor="#909090"
        app:unvisitedIntermediateStyle="dotted"
        app:unvisitedTextColor="#909090"

        app:unvisitedTextGravity="bottom"
        app:unvisitedTextMargin="10dp"
        app:unvisitedTextSize="14sp"
        app:unvisitedTextStyle="normal"

        app:visitedBorderColor="#FFA233"
        app:visitedBorderWidth="2dp"
        app:visitedCheckmarkColor="@android:color/holo_blue_light"

        app:visitedColor="#e0e0e0"
        app:visitedIntermediateColor="#FFA233"
        app:visitedIntermediateStyle="solid"
        app:visitedTextColor="#909090"
        app:visitedTextGravity="bottom"
        app:visitedTextMargin="10dp"
        app:visitedTextSize="14sp"
        app:visitedTextStyle="normal" />
  ```      
  
  ### **License**
```      

Copyright 2019 Dmitry Yablokov

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

```      

