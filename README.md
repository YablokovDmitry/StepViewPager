# StateViewPager
Android ViewPager with customizable Indicators

<img src=https://user-images.githubusercontent.com/3678050/58373690-cd420f00-7f3a-11e9-9bdf-2be6eeb5b024.gif width="240" height="440">

### **use StepViewPager**

in code 
```
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
```
in xml 
```
<com.ydn.viewpagerwithicons.StateViewPager
        android:id="@+id/pager"
        android:layout_width="match_parent"
        android:layout_height="300dp"
        android:entries="@array/titles"
        android:orientation="horizontal"
        app:layout_constraintTop_toTopOf="parent"/>

        app:bottomMargin="0dp"
        app:iconHeight="30dp"
        app:iconWidth="30dp"

        app:iconsGravity="left"
        app:intermediateHeight="2dp"
        app:intermediateWidth="90dp"
        app:leftMargin="100dp"
        app:marginBetweenIcons="30dp"
        app:numOfIcons="3"
        app:rectangularSelectedIcon="true"
        app:rightMargin="0dp"

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

        app:topMargin="0dp"
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
