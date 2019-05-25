package com.ydn.viewpagerwithicons;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.annotation.NonNull;

public interface PagerContract {
    interface View {
        void setPresenter(@NonNull Presenter presenter);
        void drawRectangularIcon(@NonNull Icon icon);
        void drawRoundedIcon(@NonNull Icon icon);
        void drawIntermediateIcon(@NonNull Icon icon);
        void setPage(int num, boolean smoothScroll);
    }

    interface Presenter {
        void init();
        void setOnIconClickListener(StateViewPager.OnIconClickListener l);
        void dispatchDraw(int width, int height);
        void addIcon(Bitmap b);
        void addIntermediateIcon(Bitmap b);
        void onDragging(Point start, Point curr);
        int getLayoutWidth();
        int getLayoutHeight();
        void onActionDown();
        void onClick(int page, int width, int height, Point x);
        void onActionUp(int offset);
        void onPageScrolled(int scrollPos, int page, int width, int height);
        void setBorderSizes(int visited, int selected, int unvisited);
        void setIconSize(int width, int height);
        void setSelectedIconSize(int width, int height);
        void setIntermediateIconSize(int width, int height);
        void setMarginBetweenIcons(int margin);
        void setIntermediateIconStyles(String visitedStyle, String unvisitedStyle);
        void setOrientation(int orientation);
        void setGravity(int gravity);
        void setTextGravities(int visitedTextGravity, int selectedTextGravity, int unvisitedTextGravity);
        void setTitles(String[] titles);
        void setIconColors(int visited, int selected, int unvisited);
        void setNumberColors(int visited, int selected, int unvisited);
        void setGradientColors(boolean visited, boolean selected, boolean unvisited);
        void setIntermediateGradients(boolean visited, boolean unvisited);
        void setIntermediateIconColors(int visited, int unvisited);
        void setBorderColors(int visited, int selected, int unvisited);
        void setShowCheckmarks(boolean visited, boolean selected, boolean unvisited);
        void setCheckmarkColors(int visited, int selected, int unvisited);
        void setTextColors(int visited, int selected, int unvisited);
        void setTextStyles(int visited, int selected, int unvisited);
        void setMargins(int left, int right, int top, int bottom);
        void setTextMargins(int visited, int selected, int unvisited);
        void setTextSizes(int visited, int selected, int unvisited);
        void setBitmaps(Bitmap visited, Bitmap selected, Bitmap unvisited);
        void setIntermediateBitmaps(Bitmap visited, Bitmap unvisited);
        void setIconShapes(boolean isVisitedRectangular, boolean isSelectedRectangular, boolean isUnvisitedRectangular);
        void setShowIconNumbers(boolean visited, boolean selected, boolean unvisited);
        void setNumberOfIcons(int num);
    }
}
