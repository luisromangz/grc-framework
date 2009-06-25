/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/

package com.greenriver.commons.tad.tree;

import java.util.List;

/**
 *
 * @param <ID>
 * @param <VT>
 * @author mangelp
 */
public interface NAryTreeNode<ID, VT> extends TreeNode<ID, VT> {

    /**
     * gets the list of childs
     * @return a list of childs
     */
    public List<? extends NAryTreeNode<ID, VT>> childs();

    /**
     * Number of childs
     * @return number of childs of this node
     */
    public int size();

    /**
     * Maximum child getCapacity. 2 for a binary tree.
     * @return maximum child getCapacity
     */
    public int getCapacity();
}
