package com.ydn.viewpagerwithicons;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Pair;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static android.widget.LinearLayout.VERTICAL;

public class PagerPresenter implements PagerContract.Presenter {
    private static final String TAG = "PagerPresenter";
    private static final boolean DEBUG = false;

    private final PagerContract.View mPagerView;
    private final IconManager mIconManager;

    private int mIconWidth;
    private int mIconHeight;
    private int mIconMargin;

    private int mOrientation;

    private int mIntermediateWidth;
    private int mIntermediateHeight;

    private int mSelectedWidth;
    private int mSelectedHeight;

    private int mLeftMargin;
    private int mRightMargin;
    private int mTopMargin;
    private int mBottomMargin;

    private boolean mShowSelectedCheckMark;
    private boolean mShowUnvisitedCheckMark;
    private boolean mShowVisitedCheckMark;

    private boolean mVisitedGradient;
    private boolean mUnvisitedGradient;
    private boolean mSelectedGradient;

    private boolean mVisitedIntermediateGradient;
    private boolean mUnvisitedIntermediateGradient;

    private int mIconsGravity;
    private int mVisitedTextGravity;
    private int mSelectedTextGravity;
    private int mUnvisitedTextGravity;

    private int mSelectedColor;
    private int mVisitedColor;
    private int mUnvisitedColor;

    private int mSelectedBorderColor;
    private int mVisitedBorderColor;
    private int mUnvisitedBorderColor;

    private int mSelectedCheckMarkColor;
    private int mVisitedCheckMarkColor;
    private int mUnvisitedCheckMarkColor;

    private int mSelectedNumberColor;
    private int mVisitedNumberColor;
    private int mUnvisitedNumberColor;

    private int mVisitedIntermediateColor;
    private int mUnvisitedIntermediateColor;

    private int mSelectedTextColor;
    private int mVisitedTextColor;
    private int mUnvisitedTextColor;

    private int mVisitedTextStyle;
    private int mSelectedTextStyle;
    private int mUnvisitedTextStyle;

    private int mVisitedTextSize;
    private int mSelectedTextSize;
    private int mUnvisitedTextSize;

    private int mVisitedTextMargin;
    private int mSelectedTextMargin;
    private int mUnvisitedTextMargin;

    private int mVisitedBorderWidth;
    private int mSelectedBorderWidth;
    private int mUnvisitedBorderWidth;

    private boolean mIsVisitedIntermediateDotted;
    private boolean mIsUnvisitedIntermediateDotted;

    private String[] mTitles;
    private int mSelectedPage;

    private Bitmap mVisitedBitmap;
    private Bitmap mUnvisitedBitmap;
    private Bitmap mSelectedBitmap;

    private Bitmap mVisitedIntermediateBitmap;
    private Bitmap mUnvisitedIntermediateBitmap;

    private boolean mIsSquareVisitedIcon;
    private boolean mIsSquareSelectedIcon;
    private boolean mIsSquareUnvisitedIcon;

    private boolean mShowVisitedNumber;
    private boolean mShowSelectedNumber;
    private boolean mShowUnvisitedNumber;

    private StateViewPager.OnIconClickListener mOnIconClickListener;

    PagerPresenter(@NonNull PagerContract.View viewPager) {
        mPagerView = viewPager;
        mIconManager = new IconManager();
        mPagerView.setPresenter(this);
    }

    private int getColor(int index) {
        if (index < mSelectedPage) {
            return mVisitedColor;
        } else if (index == mSelectedPage) {
            return mSelectedColor;
        }
        return mUnvisitedColor;
    }

    private int getCheckMarkColor(int index) {
        if (index < mSelectedPage) {
            return mVisitedCheckMarkColor;
        } else if (index == mSelectedPage) {
            return mSelectedCheckMarkColor;
        }
        return mUnvisitedCheckMarkColor;
    }

    private int getIntermediateColor(int index) {
        if (index < mSelectedPage) {
            return mVisitedIntermediateColor;
        }
        return mUnvisitedIntermediateColor;
    }

    private int getBorderColor(int index) {
        if (index < mSelectedPage) {
            return mVisitedBorderColor;
        } else if (index == mSelectedPage) {
            return mSelectedBorderColor;
        }
        return mUnvisitedBorderColor;
    }

    private int getNumberColor(int index) {
        if (index < mSelectedPage) {
            return mVisitedNumberColor;
        } else if (index == mSelectedPage) {
            return mSelectedNumberColor;
        }
        return mUnvisitedNumberColor;
    }

    private boolean getIconType(int index) {
        if (index < mSelectedPage) {
            return mIsSquareVisitedIcon;
        } else if (index == mSelectedPage) {
            return mIsSquareSelectedIcon;
        }
        return mIsSquareUnvisitedIcon;
    }

    private int getTextColor(int index) {
        if (index < mSelectedPage) {
            return mVisitedTextColor;
        } else if (index == mSelectedPage) {
            return mSelectedTextColor;
        }
        return mUnvisitedTextColor;
    }

    private int getTextMargin(int index) {
        if (index < mSelectedPage) {
            return mVisitedTextMargin;
        } else if (index == mSelectedPage) {
            return mSelectedTextMargin;
        }
        return mUnvisitedTextMargin;
    }

    private int getTextStyle(int index) {
        if (index < mSelectedPage) {
            return mVisitedTextStyle;
        } else if (index == mSelectedPage) {
            return mSelectedTextStyle;
        }
        return mUnvisitedTextStyle;
    }

    private int getTextSize(int index) {
        if (index < mSelectedPage) {
            return mVisitedTextSize;
        } else if (index == mSelectedPage) {
            return mSelectedTextSize;
        }
        return mUnvisitedTextSize;
    }

    private int getTextGravity(int index) {
        if (index < mSelectedPage) {
            return mVisitedTextGravity;
        } else if (index == mSelectedPage) {
            return mSelectedTextGravity;
        }
        return mUnvisitedTextGravity;
    }

    private int getBorderWidth(int index) {
        if (index < mSelectedPage) {
            return mVisitedBorderWidth;
        } else if (index == mSelectedPage) {
            return mSelectedBorderWidth;
        }
        return mUnvisitedBorderWidth;
    }

    private String getTitle(int index) {
        if (mTitles != null && mTitles.length > index && mTitles[index] != null) {
            return mTitles[index];
        }
        return "";
    }

    private boolean getShowCheckMark(int index) {
        if (index < mSelectedPage) {
            return mShowVisitedCheckMark;
        } else if (index == mSelectedPage) {
            return mShowSelectedCheckMark;
        }
        return mShowUnvisitedCheckMark;
    }

    private boolean getGradient(int index) {
        if (index < mSelectedPage) {
            return mVisitedGradient;
        } else if (index == mSelectedPage) {
            return mSelectedGradient;
        }
        return mUnvisitedGradient;
    }

    private boolean isShowNumber(int index) {
        if (index < mSelectedPage) {
            return mShowVisitedNumber;
        } else if (index == mSelectedPage) {
            return mShowSelectedNumber;
        }
        return mShowUnvisitedNumber;
    }

    private boolean isDottedIntermediateStyle(int index) {
        if (index < mSelectedPage) {
            return mIsVisitedIntermediateDotted;
        }
        return mIsUnvisitedIntermediateDotted;
    }

    private boolean getIntermediateGradient(int index) {
        if (index < mSelectedPage) {
            return mVisitedIntermediateGradient;
        }
        return mUnvisitedIntermediateGradient;
    }

    private Bitmap getBitmap(int index) {
        if (index < mSelectedPage) {
            return mVisitedBitmap;
        } else if (index == mSelectedPage) {
            return mSelectedBitmap;
        }
        return mUnvisitedBitmap;
    }

    private Bitmap getIntermediateBitmap(int index) {
        if (index < mSelectedPage) {
            return mVisitedIntermediateBitmap;
        }
        return mUnvisitedIntermediateBitmap;
    }

    private Pair<Integer, Integer> getIconSize(int index) {
        return index == mSelectedPage ? new Pair<>(mSelectedWidth, mSelectedHeight) :
                new Pair<>(mIconWidth, mIconHeight);
    }

    private Pair<Integer, Integer> getIntermediateIconSize(int index) {
        return new Pair<>(mIntermediateWidth, mIntermediateHeight);
    }

    @Override
    public void init() {
        mIconManager.reinit();
    }

    @Override
    public void setOnIconClickListener(StateViewPager.OnIconClickListener l) {
        mOnIconClickListener = l;
    }

    @Override
    public void dispatchDraw(int width, int height) {
        mIconManager.arrangeIcons(width, height);

        final List<Icon> icons = mIconManager.getIcons();
        final List<Icon> intermediateIcons = mIconManager.getIntermediateIcons();

        for (int i = 0; i < icons.size(); i++) {
            final Icon icon = icons.get(i);
            final Icon intermediateIcon = (i < intermediateIcons.size() - 1) ? intermediateIcons.get(i) : null;

            icon.setColor(getColor(i));
            icon.setGradient(getGradient(i));
            icon.setTextGravity(getTextGravity(i));
            icon.setTextSize(getTextSize(i));
            icon.setTextColor(getTextColor(i));
            icon.setTextStyle(getTextStyle(i));
            icon.setBorderWidth(getBorderWidth(i));
            icon.setBorderColor(getBorderColor(i));
            icon.setCheckmarkColor(getCheckMarkColor(i));
            icon.setNumberColor(getNumberColor(i));
            icon.setTextMargin(getTextMargin(i));
            icon.setText(getTitle(i));
            icon.setShowCheckmark(getShowCheckMark(i));
            icon.setSquare(getIconType(i));
            icon.setWidth(getIconSize(i).first);
            icon.setHeight(getIconSize(i).second);
            icon.setShowNumber(isShowNumber(i));
            icon.setId(i + 1);

            final Bitmap bitmap = getBitmap(i);
            final Bitmap scaledBitmap = bitmap != null ? Bitmap.createScaledBitmap(bitmap, icon.getWidth(), icon.getHeight(), false) : null;
            icon.setBitmap(scaledBitmap);

            if (icon.isSquare()) {
                mPagerView.drawRectangularIcon(icon);
            } else {
                mPagerView.drawRoundedIcon(icon);
            }

            if (intermediateIcon != null) {
                intermediateIcon.setColor(getIntermediateColor(i));
                intermediateIcon.setGradient(getIntermediateGradient(i));
                intermediateIcon.setDottedStyle(isDottedIntermediateStyle(i));
                intermediateIcon.setWidth(getIntermediateIconSize(i).first);
                intermediateIcon.setHeight(getIntermediateIconSize(i).second);

                final Bitmap interBitmap = getIntermediateBitmap(i);
                final Bitmap scaledInterBitmap = interBitmap != null ? Bitmap.createScaledBitmap(interBitmap, intermediateIcon.getWidth(), intermediateIcon.getHeight(), false) : null;
                intermediateIcon.setBitmap(scaledInterBitmap);

                mPagerView.drawIntermediateIcon(intermediateIcon);
            }
        }
    }

    @Override
    public void addIcon(Bitmap b) {
        final Icon icon = new Icon(0, 0, 0, 0);
        icon.setTextGravity(mVisitedTextGravity);
        mIconManager.addIcon(icon);
    }

    @Override
    public void addIntermediateIcon(Bitmap b) {
        final Icon icon = new Icon(0, 0, 0, 0);
        mIconManager.addIntermediateIcon(icon);
    }

    @Override
    public void onDragging(Point start, Point pt) {
        int draggingOffset = pt.x - start.x;

        if (mOrientation == VERTICAL) {
            draggingOffset =  pt.y - start.y;
        }
        mIconManager.setDraggingOffset(draggingOffset);
    }

    @Override
    public int getLayoutHeight() {
        return mIconManager.getMaxIconHeight() + mTopMargin + mBottomMargin;
    }

    @Override
    public int getLayoutWidth() {
        return mIconManager.getMaxIconWidth() + mLeftMargin + mRightMargin;
    }

    @Override
    public void onActionDown() {
        mIconManager.startDragging();
    }

    @Override
    public void onClick(int page, int width, int height, Point point) {
        int i = mIconManager.getClickedIconNum(point);

        if (mOnIconClickListener == null) {
            mPagerView.setPage(i, true);
        } else {
            mOnIconClickListener.onIconClick(i);
        }
    }

    @Override
    public void onActionUp(int offset) {
    }

    @Override
    public void onPageScrolled(int scrollPos, int page, int width, int height) {
        if (DEBUG) Log.i(TAG, "onPageScrolled " + scrollPos + " PAGE = "  + page);

        mIconManager.setScrollPos(scrollPos, page, width, height);
        mIconManager.setSelectedPage(page);
        mSelectedPage = page;
    }

    @Override
    public void setBorderSizes(int visited, int selected, int unvisited) {
        mVisitedBorderWidth = visited;
        mSelectedBorderWidth = selected;
        mUnvisitedBorderWidth = unvisited;
    }

    @Override
    public void setIconSize(int width, int height) {
        mIconWidth = width;
        mIconHeight = height;
    }

    @Override
    public void setSelectedIconSize(int width, int height) {
        mSelectedWidth = width;
        mSelectedHeight = height;
    }

    @Override
    public void setIntermediateIconSize(int width, int height) {
        mIntermediateWidth = width;
        mIntermediateHeight = height;
    }

    @Override
    public void setMarginBetweenIcons(int margin) {
        mIconMargin = margin;
        mIconManager.setMarginBetweenIcons(margin);
    }

    @Override
    public void setIntermediateIconStyles(String visitedStyle, String unvisitedStyle) {
        if (visitedStyle == null || unvisitedStyle == null) {
            return;
        }
        mIsVisitedIntermediateDotted = false;
        mIsUnvisitedIntermediateDotted = false;

        if (visitedStyle.equals("dotted")) {
            mIsVisitedIntermediateDotted = true;
        }
        if (unvisitedStyle.equals("dotted")) {
            mIsUnvisitedIntermediateDotted = true;
        }
    }

    @Override
    public void setGravity(int gravity) {
        mIconsGravity = gravity;
        mIconManager.setGravity(gravity);
    }

    @Override
    public void setTextGravities(int visitedTextGravity, int selectedTextGravity, int unvisitedTextGravity) {
        mVisitedTextGravity = visitedTextGravity;
        mSelectedTextGravity = selectedTextGravity;
        mUnvisitedTextGravity = unvisitedTextGravity;
    }

    @Override
    public void setTitles(@NonNull String[] titles) {
        mTitles = checkNotNull(titles);
    }

    @Override
    public void setShowCheckmarks(boolean visited, boolean selected, boolean unvisited) {
        mShowSelectedCheckMark = selected;
        mShowVisitedCheckMark = visited;
        mShowUnvisitedCheckMark = unvisited;
    }

    @Override
    public void setIconColors(int visited, int selected, int unvisited) {
        mSelectedColor = selected;
        mVisitedColor = visited;
        mUnvisitedColor = unvisited;
    }

    @Override
    public void setNumberColors(int visited, int selected, int unvisited) {
        mVisitedNumberColor = visited;
        mSelectedNumberColor = selected;
        mUnvisitedNumberColor = unvisited;
    }

    @Override
    public void setGradientColors(boolean visited, boolean selected, boolean unvisited) {
        mVisitedGradient = visited;
        mUnvisitedGradient = unvisited;
        mSelectedGradient = selected;
    }

    @Override
    public void setIntermediateGradients(boolean visited, boolean unvisited) {
        mVisitedIntermediateGradient = visited;
        mUnvisitedIntermediateGradient = unvisited;
    }

    @Override
    public void setIntermediateIconColors(int visited, int unvisited) {
        mVisitedIntermediateColor = visited;
        mUnvisitedIntermediateColor = unvisited;
    }

    @Override
    public void setBorderColors(int visited, int selected, int unvisited) {
        mSelectedBorderColor = selected;
        mVisitedBorderColor = visited;
        mUnvisitedBorderColor = unvisited;
    }

    @Override
    public void setCheckmarkColors(int visited, int selected, int unvisited) {
        mSelectedCheckMarkColor = selected;
        mVisitedCheckMarkColor = visited;
        mUnvisitedCheckMarkColor = unvisited;
    }

    @Override
    public void setTextColors(int visited, int selected, int unvisited) {
        mSelectedTextColor = selected;
        mVisitedTextColor = visited;
        mUnvisitedTextColor = unvisited;
    }

    @Override
    public void setTextStyles(int visited, int selected, int unvisited) {
        mVisitedTextStyle = visited;
        mSelectedTextStyle = selected;
        mUnvisitedTextStyle = unvisited;
    }

    @Override
    public void setMargins(int left, int right, int top, int bottom) {
        mLeftMargin = left;
        mRightMargin = right;
        mTopMargin = top;
        mBottomMargin = bottom;

        mIconManager.setLeftMargin(left);
        mIconManager.setRightMargin(right);
        mIconManager.setTopMargin(top);
        mIconManager.setBottomMargin(bottom);
    }

    @Override
    public void setTextMargins(int visited, int selected, int unvisited) {
        mVisitedTextMargin = visited;
        mSelectedTextMargin = selected;
        mUnvisitedTextMargin = unvisited;
    }

    @Override
    public void setTextSizes(int visited, int selected, int unvisited) {
        mVisitedTextSize = visited;
        mSelectedTextSize = selected;
        mUnvisitedTextSize = unvisited;
    }

    @Override
    public void setBitmaps(Bitmap visited, Bitmap selected, Bitmap unvisited) {
        mVisitedBitmap = visited;
        mSelectedBitmap = selected;
        mUnvisitedBitmap = unvisited;
    }

    @Override
    public void setIntermediateBitmaps(Bitmap visited, Bitmap unvisited) {
        mVisitedIntermediateBitmap = visited;
        mUnvisitedIntermediateBitmap = unvisited;
    }

    @Override
    public void setIconShapes(boolean isVisitedRectangular, boolean isSelectedRectangular, boolean isUnvisitedRectangular) {
        mIsSquareSelectedIcon = isSelectedRectangular;
        mIsSquareUnvisitedIcon = isUnvisitedRectangular;
        mIsSquareVisitedIcon = isVisitedRectangular;
    }

    @Override
    public void setShowIconNumbers(boolean visited, boolean selected, boolean unvisited) {
        mShowVisitedNumber = visited;
        mShowSelectedNumber = selected;
        mShowUnvisitedNumber = unvisited;
    }

    @Override
    public synchronized void setNumberOfIcons(int num) {
        mIconManager.clear();
        mIconManager.reinit();

        for (int i = 0; i < num; i++) {
            addIcon(null);
            addIntermediateIcon(null);
        }
    }

    @Override
    public void setOrientation(int orientation) {
        mOrientation = orientation;
        mIconManager.setOrientation(orientation);
    }
}
