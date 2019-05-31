package com.ydn.viewpagerwithicons;

import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;
import static android.view.Gravity.TOP;
import static android.view.Gravity.BOTTOM;
import static android.view.Gravity.LEFT;
import static android.view.Gravity.RIGHT;

class IconManager {
    private static final String TAG = "IconManager";
    private static final boolean DEBUG = false;

    private volatile List<Icon> mIcons;
    private volatile List<Icon> mIntermediateIcons;

    private int mDraggingOffset;
    private int mTouchedPoint;
    private int mScrollPos;

    private int mMarginBetweenIcons;
    private int mLeftMargin;
    private int mRightMargin;
    private int mTopMargin;
    private int mBottomMargin;

    private int mOrientation;
    private int mGravity;

    private int mCanvasHeight;
    private int mCanvasWidth;

    private int mSelectedPage;
    private int mShift;

    private static final Comparator<Icon> HEIGHT_COMPARATOR = new Comparator<Icon>() {
        @Override
        public int compare(Icon a, Icon b) {
            return a.getHeight()  - b.getHeight();
        }
    };

    private static final Comparator<Icon> WIDTH_COMPARATOR = new Comparator<Icon>() {
        @Override
        public int compare(Icon a, Icon b) {
            return a.getWidth() - b.getWidth();
        }
    };

    IconManager() {
        mIcons = new ArrayList<>();
        mIntermediateIcons = new ArrayList<>();
    }

    void reinit() {
        mShift = 0;
        mScrollPos = 0;
        mSelectedPage = 0;
        mDraggingOffset = 0;
    }

    void setSelectedPage(int selectedPage) {
        mSelectedPage = selectedPage;
    }

    void setLeftMargin(int leftMargin) {
        mLeftMargin = leftMargin;

        if (mOrientation == HORIZONTAL) {
            mTouchedPoint = mLeftMargin;
        }
    }

    void setRightMargin(int rightMargin) {
        mRightMargin = rightMargin;
    }

    void setTopMargin(int topMargin) {
        mTopMargin = topMargin;

        if (mOrientation == VERTICAL) {
            mTouchedPoint = mTopMargin;
        }
    }

    void setBottomMargin(int bottomMargin) {
        mBottomMargin = bottomMargin;
    }

    void setOrientation(int orientation) {
        mOrientation = orientation;
    }

    void setGravity(int gravity) {
        mGravity = gravity;
    }

    int getCount() {
        return mIcons.size();
    }

    List<Icon> getIcons() {
        return mIcons;
    }

    List<Icon> getIntermediateIcons() {
        return mIntermediateIcons;
    }

    void addIcon(Icon icon) {
        mIcons.add(icon);

        if (mOrientation == HORIZONTAL) {
            mTouchedPoint = mLeftMargin;
        } else {
            mTouchedPoint = mTopMargin;
        }
        arrangeIcons(0, 0 );
    }

    void addIntermediateIcon(Icon icon) {
        mIntermediateIcons.add(icon);
    }

    int getMaxIconHeight() {
        final List<Icon> merged = new ArrayList<>(mIcons);
        merged.addAll(mIntermediateIcons);

        if (merged.isEmpty()) {
            return 0;
        }
        final Icon maxIcon = Collections.max(merged, HEIGHT_COMPARATOR);
        return mOrientation == VERTICAL ? maxIcon.getHeight() + maxIcon.getTextHeight() : maxIcon.getHeight();
    }

    int getMaxIconWidth() {
        final List<Icon> merged = new ArrayList<>(mIcons);
        merged.addAll(mIntermediateIcons);

        if (merged.isEmpty()) {
            return 0;
        }
        final Icon maxIcon = Collections.max(merged, WIDTH_COMPARATOR);
        return mOrientation == HORIZONTAL ? maxIcon.getWidth() + maxIcon.getTextWidth() : maxIcon.getWidth();
    }

    synchronized void arrangeIcons(int canvasWidth, int canvasHeight) {
        mCanvasHeight = canvasHeight;
        mCanvasWidth = canvasWidth;

        final int maxWidth = getMaxIconWidth();
        final int maxHeight = getMaxIconHeight();

        int top = mTopMargin;
        int left = mLeftMargin;

        if (mOrientation == VERTICAL) {
            if (mGravity == LEFT) {
                left = mScrollPos + mLeftMargin;
            }
            if (mGravity == RIGHT) {
                left = mScrollPos + canvasWidth - maxWidth - mRightMargin;
            }
        } else if (mGravity == BOTTOM) {
            top = canvasHeight - maxHeight - mBottomMargin;
        }

        int posX = 0;
        int posY = 0;
        int size = mIcons.size();
        int left0 = left;
        int top0 = top;

        for (int i = 0; i < size; i++) {
            final Icon icon = mIcons.get(i);

            if (mOrientation == HORIZONTAL) {
                left = mScrollPos + mTouchedPoint + mDraggingOffset + posX;
                top = top0 + (maxHeight - icon.getHeight()) / 2;

                mIcons.get(i).locate(left, top);
                if (icon.getTextGravity() == LEFT) {
                    mIcons.get(i).locate(left + mIcons.get(i).getTextWidth(), top);
                }
            } else {
                top = mTouchedPoint + mDraggingOffset + posY + mShift + mTopMargin;
                left = left0 + (maxWidth - icon.getWidth()) / 2;

                mIcons.get(i).locate(left, top);
                if (icon.getTextGravity() == TOP && i > 0) {
                    mIcons.get(i).locate(left, top + icon.getTextHeight());
                }
            }

            int intWidth = 0;
            int intHeight = 0;

            if (i < mIntermediateIcons.size()) {
                final Icon intermediateIcon = mIntermediateIcons.get(i);
                intWidth = intermediateIcon.getWidth();
                intHeight = intermediateIcon.getHeight();

                if (mOrientation == HORIZONTAL) {
                    intermediateIcon.locate(left + icon.getWidth() + mMarginBetweenIcons + icon.getTextWidth(),
                            top0 + (maxHeight - intermediateIcon.getHeight()) / 2);
                } else {
                    intermediateIcon.locate(left0 + (maxWidth - intermediateIcon.getWidth()) / 2,
                            top + icon.getHeight() + mMarginBetweenIcons + icon.getTextHeight());
                }
            }

            posX += (icon.getWidth() + icon.getTextWidth() + mMarginBetweenIcons + intWidth + mMarginBetweenIcons);
            posY += (icon.getHeight() + icon.getTextHeight() + mMarginBetweenIcons + intHeight + mMarginBetweenIcons);
        }
    }

    int getClickedIconNum(Point point) {
        for (int i = 0; i < mIcons.size(); i++) {
            final Icon icon = mIcons.get(i);
            if (point.x >= icon.getLeft() && point.x <= icon.getLeft() + icon.getWidth()  &&
                point.y >= icon.getTop() && point.y <= icon.getTop() + icon.getHeight()) {
                return i;
            }
        }
        return -1;
    }

    void setDraggingOffset(int offset) {
        if (mIcons.isEmpty()) {
            return;
        }

        final Icon first = mIcons.get(0);
        final Icon last = mIcons.get(mIcons.size() - 1);

        if (mOrientation == VERTICAL) {
            if (offset > 0 && first.getTop() >= mTopMargin) {
                return;
            }
            if (offset < 0 && last.getTop() + last.getHeight() <= mCanvasHeight - mBottomMargin) {
                return;
            }
        } else {
            if (offset > 0 && first.getLeft() >= mLeftMargin + mSelectedPage * mCanvasWidth) {
                return;
            }
            if (offset < 0 && last.getLeft() + last.getWidth() <= (mSelectedPage + 1) * mCanvasWidth - mRightMargin) {
                return;
            }
        }
        mDraggingOffset = offset;
    }

    void startDragging() {
        if (!mIcons.isEmpty()) {
            if (mOrientation == HORIZONTAL) {
                int textShift = 0;
                if (mIcons.get(0).getTextGravity() == LEFT) {
                    textShift = mIcons.get(0).getTextWidth();
                }

                mTouchedPoint = mIcons.get(0).getLeft() - textShift - mScrollPos;
            } else {
                mTouchedPoint = mIcons.get(0).getTop() - mShift - mTopMargin;
            }
        }
    }

    void setScrollPos(int pos, int page, int width, int height) {
        if (mIcons.isEmpty()) {
            return;
        }
        if (DEBUG) Log.i(TAG, "scroll " + pos + " page = " + page + "mWidth = " + width + " height = " + height );

        if (pos % width == 0 && page < mIcons.size()) {
           final Icon icon = mIcons.get(page);

           if (mOrientation == HORIZONTAL) {
               if (icon.getWidth() > 0) {
                   if (icon.getLeft() + icon.getWidth() + icon.getTextWidth() > width * (page + 1)) {
                       mShift -= (width - icon.getWidth() - icon.getTextWidth());
                   } else if (icon.getLeft() + icon.getWidth() <= width * page) {
                       mShift += (width - icon.getWidth() - icon.getTextWidth());
                   } else if (icon.getLeft() <= width * page && icon.getLeft() + icon.getWidth() > width * page) {
                       mShift += 2 * icon.getWidth();
                   }
               }
           } else {
               if (icon.getHeight() > 0) {
                   if (icon.getTop() + icon.getHeight() > height) {
                       mShift -= 2 * (icon.getTop() + icon.getHeight() - height);
                   } else if (icon.getTop() < 0) {
                       mShift += 2 * Math.abs(icon.getTop());
                   }
               }
            }
        }

        if (mOrientation == HORIZONTAL) {
            mScrollPos = pos + mShift;
        } else {
            mScrollPos = pos;
        }
    }

    void clear() {
        if (mIcons != null) {
            mIcons.clear();
        }
        if (mIntermediateIcons != null) {
            mIntermediateIcons.clear();
        }
    }

    void setMarginBetweenIcons(int margin) {
        mMarginBetweenIcons = margin;
    }
}
