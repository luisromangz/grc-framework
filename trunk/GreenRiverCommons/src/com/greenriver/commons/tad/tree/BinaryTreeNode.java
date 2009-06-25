/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/

package com.greenriver.commons.tad.tree;

/**
 *
 * @author mangelp
 */
public interface BinaryTreeNode<ID, VT> extends TreeNode<ID, VT> {

    /**
     * Gets the left-most child
     * @return the left-most child or null
     */
    public TreeNode left();

    /**
     * Gets the right-most child
     * @return the right-most child or null
     */
    public TreeNode right();
}
