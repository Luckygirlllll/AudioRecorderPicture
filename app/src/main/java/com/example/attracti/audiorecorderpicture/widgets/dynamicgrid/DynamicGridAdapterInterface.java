package com.example.attracti.audiorecorderpicture.widgets.dynamicgrid;

/**
 * Created by Iryna on 7/5/16.
 */


/**
 * Any adapter used with DynamicGridView must implement DynamicGridAdapterInterface.
 * Adapter implementation also must has stable items id.
 * See for stable id implementation example.
 */

public interface DynamicGridAdapterInterface {

    /**
     * Determines how to reorder items dragged from <code>originalPosition</code> to <code>newPosition</code>
     */
    void reorderItems(int originalPosition, int newPosition);

    /**
     * @return return columns number for GridView. Need for compatibility
     * (@link android.widget.GridView#getNumColumns() requires api 11)
     */
    int getColumnCount();

    /**
     * Determines whether the item in the specified <code>position</code> can be reordered.
     */
    boolean canReorder(int position);

}
